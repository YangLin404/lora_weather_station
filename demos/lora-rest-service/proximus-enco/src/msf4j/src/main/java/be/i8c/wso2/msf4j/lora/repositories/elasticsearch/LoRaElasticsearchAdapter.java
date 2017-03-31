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
import be.i8c.wso2.msf4j.lora.models.SensorRecord;
import be.i8c.wso2.msf4j.lora.utils.LoRaJsonConvertor;


import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.elasticsearch.ResourceAlreadyExistsException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;

/**
 * This class consists methods for communicating with elasticsearch server.
 * @author yanglin
 */
@Component
@Profile("elasticsearch")
public class LoRaElasticsearchAdapter
{
     private static final Logger LOGGER = LogManager.getLogger(LoRaElasticsearchAdapter.class);
     
     private final LoRaJsonConvertor loRaJsonConvertor = LoRaJsonConvertor.getInstance();
     
     @Value("${elasticsearch.host}")
     private String esHost;
     @Value("${elasticsearch.index}")
     private String esIndex;
     @Value("${elasticsearch.port}")
     private int esPort;
     @Value("${elasticsearch.timestampName}")
     private String esTimestampName;
     private TransportClient client;
     
     private boolean indexExist;
    private long idSequences;
     
     public LoRaElasticsearchAdapter()
     {
         
     }

    /**
     * This method is used to initialize the connection with elasticsearch server and check the existence of given index.
     * It will be fired after the construction.
     */
    @PostConstruct
     private void init()
     {
         
         LOGGER.info("initiating elasticsearchAdapter");
         LOGGER.info("connecting elasticsearch server at " + this.esHost + ":" + this.esPort);
         this.indexExist = false;
         try 
         {
            this.client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(this.esHost), this.esPort));
            LOGGER.info("connection successful");
            
            LOGGER.info("checking if index: [" + this.esIndex + "] exist.");
            IndicesExistsRequest indicesExistsRequest = new IndicesExistsRequest(this.esIndex);

            this.indexExist = this.client.admin().indices()
                              .exists(indicesExistsRequest).actionGet().isExists();
            if(this.indexExist)
            {
                LOGGER.info("index: [" + this.esIndex + "] exist.");
                this.idSequences = getLastId();
            }
            else
            {
                LOGGER.info("index: [" + this.esIndex + "] doesn't exist, it will be created at first input, idSequence set to 1");
                this.idSequences = 0;
            }
            
             
         } catch (UnknownHostException e) {
             LOGGER.error("connection to " + this.esHost + ":" + this.esPort + " fails." +"\n " + e.getMessage());
         }
         
         
         
         
     }

    /**
     * This method is used to disconnect elasticsearch.
     */
    @PreDestroy
     private void destroy()
     {
         LOGGER.info("destroying elasticsearchAdapter");
         LOGGER.info("disconnecting elasticsearch server at " + this.esHost + ":" + this.esPort);
         this.client.close();
         LOGGER.info("elasticsearch disconnected");
     }

    /**
     * This method is used to create index and add date mapping to it's timestamp
     * @param sensorRecord object which to be indexed later on.
     */
    private void createAndMapIndex(SensorRecord sensorRecord)
     {
        LOGGER.info("try creating index: [" + this. esIndex + "]");
         try {
             client.admin().indices().prepareCreate(this.esIndex)   
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
             LOGGER.info("index: [" + this.esIndex + "] already exist, not created");
         }
         LOGGER.info("index: [" + this.esIndex + "] created.");
        
     }

    /**
     * This method is used to index a doc into index.
     * @param sensorRecord This is the document which will be indexed.
     * @return  The indexed sensorRecord or null when index unsuccessfully.
     */
    public SensorRecord save(SensorRecord sensorRecord) {
        //create and map the given TimestampName into index as type date,
        //otherwise elasticseach can't recognise timestamps
        if (!indexExist)
        {
            createAndMapIndex(sensorRecord);
            indexExist = true;
        }
        sensorRecord.setId(++this.idSequences);
        LOGGER.info("trying to index doc into: " + this.esIndex + ". object: " + sensorRecord.simpleString());
        String docString = loRaJsonConvertor.convertToJsonString(sensorRecord);
        IndexResponse u =
                client.prepareIndex(esIndex, sensorRecord.getClass().getSimpleName(),Long.toString(sensorRecord.getId()))
                        .setSource(docString)
                        .get();
        DocWriteResponse.Result r = u.getResult();
        if (r == DocWriteResponse.Result.CREATED)
        {
            LOGGER.info("successful indexed object into " + this.esIndex + ". result is: " + r);
            return sensorRecord;
        }
        else
            return null;

    }

    /**
     * Find the id of the last indexed document within the index
     * @return the id of the last indexed document
     */
    private long getLastId()
    {
        LOGGER.info("Trying get last id from index " + this.esIndex );
        SearchResponse response = client.prepareSearch(this.esIndex)
                .addAggregation(
                        AggregationBuilders
                                .max("maxId")
                                .field("id")
                )
        .execute().actionGet();
        Max max = response.getAggregations().get("maxId");
        long id =  Math.round(max.getValue());
        LOGGER.info("last id is " + id);
        return id;
    }
}


