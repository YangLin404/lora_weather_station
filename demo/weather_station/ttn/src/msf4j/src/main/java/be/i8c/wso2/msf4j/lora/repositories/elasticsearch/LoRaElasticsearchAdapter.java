/*
  * Copyright (c) 2017, i8c N.V. (Integr8 Consulting; http://www.i8c.be)
  * All Rights Reserved.
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package be.i8c.wso2.msf4j.lora.repositories.elasticsearch;
import be.i8c.wso2.msf4j.lora.models.SensorBuilder;
import be.i8c.wso2.msf4j.lora.models.SensorRecord;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import be.i8c.wso2.msf4j.lora.models.SensorType;
import be.i8c.wso2.msf4j.lora.repositories.elasticsearch.exceptions.NoneNodeConnectedException;
import com.google.gson.Gson;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.elasticsearch.ResourceAlreadyExistsException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Value;

/**
 * An adapter class that communicates with elasticsearch server through elasticsearch JAVA API.
 *
 * @author yanglin
 */
@Component
@Profile("elasticsearch")
public class LoRaElasticsearchAdapter
{
     private static final Logger logger = LogManager.getLogger(LoRaElasticsearchAdapter.class);

    /**
     * The hostname of server where elasticseach can be found. It will be automatically read from application.properties
     */
    @Value("${elasticsearch.host}")
     private String esHost;
    /**
     * The name of index. It will be automatically read from application.properties
     */
     @Value("${elasticsearch.index}")
     private String esIndex;

    /**
     * The port number of elasticsearch server. It will be automatically read from application.properties
     */
     @Value("${elasticsearch.port}")
     private int esPort;
    /**
     * The name of field to be mapped as date. It will be automatically read from application.properties
     */
    @Value("${elasticsearch.timestampName}")
     private String esTimestampName;

    /**
     * An object which communicates with elasticsearch server
     */
    @Autowired
    private TransportClient transportClient;
    /**
     * a boolean indicate whether index is exist or not.
     */
    private Map<String, Boolean> indexExist;
    /**
     * The id of last indexed document, it indicates which id should be given to the next document.
     */
    private long idSequences;

    private Gson gson;
     
     public LoRaElasticsearchAdapter()
     {
         
     }

    /**
     * This method is used to initialize the connection with elasticsearch server and check the existence of given index.
     * It will be automatically executed once this class is constructed.
     */
    @PostConstruct
     private void init()
     {
         
         logger.debug("initiating elasticsearchAdapter");
         this.gson = new Gson();
         this.indexExist = new HashMap<>();
         if (this.transportClient.transportAddresses().isEmpty())
             try {
                 addNodeConnection(this.esHost, this.esPort);
             } catch (UnknownHostException e)
             {
                 logger.error("could not connect to node at {}:{}", this.esHost, this.esPort);
             }
         if (!this.doesIndexExist(this.esIndex)) {
             createAndMapIndex(new SensorBuilder().setType(SensorType.Temperature).setValue(20).build());
         }

     }

    /**
     * This method is used to add TransportAddres of a elasticsearch's node to transportClient.
     * The transportClient will than try to connect to those node(s).
     * @param host Hostname of Elasticsearch's node to be added.
     * @param port Port number of Elasticsearch's node to be added.
     * @throws UnknownHostException When combination of host and port doesn't is unknown.
     */
     public void addNodeConnection(String host, int port) throws UnknownHostException
     {
         if (this.transportClient.transportAddresses().isEmpty()) {
             logger.debug("add elasticsearch server at " + host + ":" + port);
             this.transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
             logger.info("the connection with elasticsearch server is successfully established");
         }

     }

    /**
     * Check the existence of given index in current elasticsearch's node.
     * @param indexToBeChecked the name of index to be checked.
     * @return true if exist, false if not exist.
     * @throws NoneNodeConnectedException when there are no elasticsearch's node is connected.
     */
     public boolean doesIndexExist(String indexToBeChecked) throws NoneNodeConnectedException
     {
         if (!this.transportClient.connectedNodes().isEmpty()) {
             logger.debug("checking if index: [" + indexToBeChecked + "] exist.");
             if (indexExist.containsKey(indexToBeChecked) && indexExist.get(indexToBeChecked))
             {
                 logger.debug("index: [{}] exists.", indexToBeChecked);
                 return true;
             }
             else {
                 IndicesExistsRequest indicesExistsRequest = new IndicesExistsRequest(indexToBeChecked);
                 boolean exist = this.transportClient.admin().indices()
                         .exists(indicesExistsRequest).actionGet().isExists();
                 logger.debug("{} exists", exist);
                 this.indexExist.put(indexToBeChecked, exist);
                 if (exist) {
                     logger.debug("index: [" + indexToBeChecked + "] exist.");
                     this.idSequences = getLastId();
                 } else {
                     logger.debug("index: [" + this.esIndex + "] doesn't exist, it will be created at first index, idSequence set to 0");
                     this.idSequences = 0;
                 }
                 return exist;
             }
         }
         else {
             logger.warn("None of Nodes are connected, existence of index: {} will not be checked", indexToBeChecked);
             throw new NoneNodeConnectedException();
         }
     }

    /**
     * This method is used to disconnect the elasticsearch server.
     * It will be automatically called just before the destruction of this class.
     */
    @PreDestroy
     private void destroy()
     {
         logger.info("destroying elasticsearchAdapter");
         logger.debug("disconnecting elasticsearch server at " + this.esHost + ":" + this.esPort);
         this.transportClient.close();
         logger.info("elasticsearch disconnected");
     }

    /**
     * This method is used to create index and add date mapping to it's timestamp field.
     * This method will be called by first insertion of lora packet if the given index doesn't exist.
     * @param sensorRecord lora packet which will be indexed after creation of index.
     */
    private void createAndMapIndex(SensorRecord sensorRecord)
     {
        logger.info("try creating index: [" + this. esIndex + "]");
         try {
             transportClient.admin().indices().prepareCreate(this.esIndex)
               .addMapping(sensorRecord.getClass().getSimpleName(), "{\n" +
               "    \"" + sensorRecord.getClass().getSimpleName() + "\": {\n" +
               "      \"properties\": {\n" +
               "        \"" + this.esTimestampName + "\": {\n" +
               "          \"type\": \"date\"\n" +
               "        }\n" +
               "      }\n" +
               "    }\n" +
               "  }")
                .get();
         } catch (ResourceAlreadyExistsException e) {
             logger.warn("index: [" + this.esIndex + "] already exist, not created");
             return;
         }
         logger.info("index: [" + this.esIndex + "] created.");
        
     }

    /**
     * This method is used to index a object into a specified index.
     * @param sensorRecord a object of sensorRecord which will be indexed.
     * @return  An object of sensorRecord or null when index unsuccessfully.
     */
    public SensorRecord index(SensorRecord sensorRecord) {
        //create and map the given TimestampName into index as type date,
        //otherwise elasticseach can't recognise timestamps
            sensorRecord.setId(++this.idSequences);
            logger.debug("trying to index doc into: " + this.esIndex + ". object: " + sensorRecord.simpleString());
            String docString = this.gson.toJson(sensorRecord, sensorRecord.getClass());
            IndexResponse u =
                    transportClient.prepareIndex(esIndex, sensorRecord.getClass().getSimpleName(), Long.toString(sensorRecord.getId()))
                            .setSource(docString)
                            .get();
            DocWriteResponse.Result r = u.getResult();
            if (r == DocWriteResponse.Result.CREATED) {
                logger.info("successful indexed object with id {}", sensorRecord.getId());
                return sensorRecord;
            } else {
                logger.error("index object into" + this.esIndex + " fails. object: " + sensorRecord.simpleString());
                return null;
            }

    }
    /**
     * This method is used to index a list of objects into a specified index.
     * @param records a list of objects of sensorRecord which will be indexed.
     * @return  a list of objects of sensorRecord or null when index unsuccessfully.
     */
    public List<SensorRecord> index(List<SensorRecord> records)
    {
        try {
            if (this.transportClient.transportAddresses().isEmpty())
                addNodeConnection(this.esHost, this.esPort);
            if (!this.doesIndexExist(this.esIndex)) {
                createAndMapIndex(records.get(0));
            }
            logger.debug("try to index records: \n{}", records.stream().map(SensorRecord::simpleString).collect(Collectors.joining(",\n ")));
            BulkRequestBuilder bulkRequest = transportClient.prepareBulk();
            logger.debug("add doc into bulk request");
            records.forEach(r ->
            {
                logger.debug("adding doc {}", r.simpleString());
                addDocToBulkRequest(bulkRequest, r);
            });
            logger.debug("executing bulk request");
            BulkResponse bulkResponse = bulkRequest.get();
            if (bulkResponse.hasFailures()) {
                logger.error("bulk index fails. failure message: {}", bulkResponse.buildFailureMessage());
                return null;
            } else {
                String indexedIds = records.stream()
                        .map(e -> Long.toString(e.getId()))
                        .collect(Collectors.joining(", "));
                logger.info("successful indexed {} object with ids: [{}].", records.size(), indexedIds);
                logger.debug("indexed objects are: ", records.toString());
                return records;
            }
        }catch (UnknownHostException | NoneNodeConnectedException e)
        {
            logger.error("could not connect to node at {}:{}", this.esHost, this.esPort);
            return null;
        }
    }

    /**
     * This method is used to add record to a given bulkrequestBuilder object,
     * which will be used to build a bulk request later for bulk indexing.
     * @param bulkRequest bulkRequestBuilder class.
     * @param record record to be added.
     */
    private void addDocToBulkRequest(BulkRequestBuilder bulkRequest, SensorRecord record)
    {
        record.setId(++this.idSequences);
        String docString = this.gson.toJson(record, record.getClass());
        bulkRequest.add(
                transportClient.prepareIndex(esIndex, record.getClass().getSimpleName(), Long.toString(record.getId()))
                        .setSource(docString));
    }

    /**
     * Find the id of the last indexed document within the specified index.
     * @return the id of the last indexed document.
     */
    private long getLastId()
    {
        logger.info("Trying get last id from index " + this.esIndex );
        SearchResponse response = transportClient.prepareSearch(this.esIndex)
                .addAggregation(
                        AggregationBuilders
                                .max("maxId")
                                .field("id")
                )
        .execute().actionGet();
        Max max = response.getAggregations().get("maxId");
        long id =  Math.round(max.getValue());
        logger.info("last id is " + id);
        return id;
    }

}


