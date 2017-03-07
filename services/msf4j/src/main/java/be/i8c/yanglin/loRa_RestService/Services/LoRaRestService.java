/*
 * Copyright (c) 2016, WSO2 Inc. (http://wso2.com) All Rights Reserved.
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

package be.i8c.yanglin.loRa_RestService.services;


import be.i8c.yanglin.loRa_RestService.models.Record;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Map;
import org.springframework.stereotype.Component;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the Microservice resource class.
 * See <a href="https://github.com/wso2/msf4j#getting-started">https://github.com/wso2/msf4j#getting-started</a>
 * for the usage of annotations.
 *
 * @since 0.1-SNAPSHOT
 */
@Component
@Path("/service")
public class LoRaRestService 
{

    private static final Logger logger = LoggerFactory.getLogger(LoRaRestService.class);

    @GET
    @Path("/")
    public String get() {
        // TODO: Implementation for HTTP GET request
        logger.debug("get invoked");
        return "Hello from WSO2 MSF4J";
    }

    @POST
    @Path("/")
    public void post(Object o) 
    {
        // TODO: Implementation for HTTP POST request
        JsonObject obj = new JsonParser().parse(o.toString()).getAsJsonObject();
        logger.info("post invoked. data: " + obj);
    }

    @PUT
    @Path("/")
    public void put() {
        // TODO: Implementation for HTTP PUT request
        logger.debug("put invoked.");
    }

    @DELETE
    @Path("/")
    public void delete() {
        // TODO: Implementation for HTTP DELETE request
        logger.debug("delete invoked.");
    }
}