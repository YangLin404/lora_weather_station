/*
 * Copyright 2017 WSO2.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.i8c.yanglin.loRa_RestService.repositories;

import be.i8c.yanglin.loRa_RestService.utils.LoRaJsonConvertor;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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
 *
 * @author yanglin
 */
@Component
public class ElasticsearchAdapter<T>
{
     private static final Logger LOGGER = LogManager.getLogger(ElasticsearchAdapter.class);
     
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
     
     public ElasticsearchAdapter()
     {
         
     }
     
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
                LOGGER.info("index: [" + this.esIndex + "] exist.");
            else
                LOGGER.info("index: [" + this.esIndex + "] doesn't exist, it will be created at first input");
            
             
         } catch (UnknownHostException e) {
             LOGGER.error("connection to " + this.esHost + ":" + this.esPort + " fails." +"\n " + e.getMessage());
         }
         
         
         
         
     }
     
     @PreDestroy
     private void destroy()
     {
         LOGGER.info("destroying elasticsearchAdapter");
         LOGGER.info("disconnecting elasticsearch server at " + this.esHost + ":" + this.esPort);
         this.client.close();
         LOGGER.info("elasticsearch disconnected");
     }
     
     public boolean index(T t)
     {
         //create and map the given TimestampName into index as type date,
         //otherwise elasticseach can't recognise timestamps
         if (!indexExist) 
         {
            createAndMapIndex(t);
            indexExist = true;
         }
         LOGGER.info("trying to index doc into: " + this.esIndex + ". object: " + t.toString());
         String docString = loRaJsonConvertor.convertToJsonString(t);
         IndexResponse u = 
                 client.prepareIndex(esIndex, t.getClass().getSimpleName())
                        .setSource(docString)
                        .get();
         DocWriteResponse.Result r = u.getResult();
         
         LOGGER.info("successful indexed object into " + this.esIndex + ". result is: " + r);
         return true;
     }
     
     private void createAndMapIndex(T t)
     {
        LOGGER.info("try creating index: [" + this. esIndex + "]");
         try {
             client.admin().indices().prepareCreate(this.esIndex)   
               .addMapping(t.getClass().getSimpleName(), "{\n" +                
               "    \"" + t.getClass().getSimpleName() + "\": {\n" +
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
}


