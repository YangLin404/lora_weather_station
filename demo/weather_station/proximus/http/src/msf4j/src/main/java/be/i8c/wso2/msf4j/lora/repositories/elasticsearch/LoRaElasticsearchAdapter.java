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
import be.i8c.wso2.msf4j.lora.models.ProximusSensor;
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
 * An adapter class that communicates with elasticsearch server through elasticsearch JAVA API.
 *
 * @author yanglin
 */
@Component
@Profile("elasticsearch")
public class LoRaElasticsearchAdapter
{
     private static final Logger LOGGER = LogManager.getLogger(LoRaElasticsearchAdapter.class);
     
     private final LoRaJsonConvertor loRaJsonConvertor = LoRaJsonConvertor.getInstance();

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
     @Value("${elasticsearch.port}")
     /**
      * The port number of elasticsearch server. It will be automatically read from application.properties
      */
     private int esPort;
    /**
     * The name of field to be mapped as date. It will be automatically read from application.properties
     */
    @Value("${elasticsearch.timestampName}")
     private String esTimestampName;

    /**
     * An object which communicates with elasticsearch server
     */
    private TransportClient client;
    /**
     * a boolean indicate whether index is exist or not.
     */
    private boolean indexExist;
    /**
     * The id of last indexed document, it indicates which id should be given to the next document.
     */
    private long idSequences;
     
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
     * This method is used to disconnect the elasticsearch server.
     * It will be automatically called just before the destruction of this class.
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
     * This method is used to create index and add date mapping to it's timestamp field.
     * This method will be called by first insertion of lora packet if the given index doesn't exist.
     * @param proximusSensor lora packet which will be indexed after creation of index.
     */
    private void createAndMapIndex(ProximusSensor proximusSensor)
     {
        LOGGER.info("try creating index: [" + this. esIndex + "]");
         try {
             client.admin().indices().prepareCreate(this.esIndex)   
               .addMapping(proximusSensor.getClass().getSimpleName(), "{\n" +
               "    \"" + proximusSensor.getClass().getSimpleName() + "\": {\n" +
               "      \"properties\": {\n" +
               "        \"" + this.esTimestampName + "\": {\n" +
               "          \"type\": \"date\"\n" +
               "        }\n" +
               "      }\n" +
               "    }\n" +
               "  }")
                .get();
         } catch (ResourceAlreadyExistsException e) {
             LOGGER.error("index: [" + this.esIndex + "] already exist, not created");
         }
         LOGGER.info("index: [" + this.esIndex + "] created.");
        
     }

    /**
     * This method is used to index a object into a specified index.
     * @param proximusSensor a object of proximusSensor which will be indexed.
     * @return  An object of proximusSensor or null when index unsuccessfully.
     */
    public ProximusSensor save(ProximusSensor proximusSensor) {
        //create and map the given TimestampName into index as type date,
        //otherwise elasticseach can't recognise timestamps
        if (!indexExist)
        {
            createAndMapIndex(proximusSensor);
            indexExist = true;
        }
        proximusSensor.setId(++this.idSequences);
        LOGGER.info("trying to index doc into: " + this.esIndex + ". object: " + proximusSensor.simpleString());
        String docString = loRaJsonConvertor.convertToJsonString(proximusSensor);
        IndexResponse u =
                client.prepareIndex(esIndex, proximusSensor.getClass().getSimpleName(),Long.toString(proximusSensor.getId()))
                        .setSource(docString)
                        .get();
        DocWriteResponse.Result r = u.getResult();
        if (r == DocWriteResponse.Result.CREATED)
        {
            LOGGER.info("successful indexed object into " + this.esIndex + ". result is: " + r);
            return proximusSensor;
        }
        else
            return null;

    }

    /**
     * Find the id of the last indexed document within the specified index.
     * @return the id of the last indexed document.
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


