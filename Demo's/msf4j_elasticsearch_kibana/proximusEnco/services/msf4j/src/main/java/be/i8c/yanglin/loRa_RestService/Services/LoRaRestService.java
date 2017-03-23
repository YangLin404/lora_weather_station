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

package be.i8c.yanglin.loRa_RestService.Services;


import be.i8c.yanglin.loRa_RestService.models.SensorRecord;
import be.i8c.yanglin.loRa_RestService.models.SensorType;
import be.i8c.yanglin.loRa_RestService.repositories.LoRaRepository;
import be.i8c.yanglin.loRa_RestService.utils.LoRaJsonConvertor;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import org.springframework.stereotype.Component;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;

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

    private static final Logger LOGGER = LogManager.getLogger(LoRaRestService.class);
    
    @Autowired
    private LoRaRepository repo;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_XML)
    public SensorRecord get() {
        // TODO: Implementation for HTTP GET request
        LOGGER.debug("get invoked");
        SensorRecord r = new SensorRecord("x", "x", "1", "x", Calendar.getInstance().getTimeInMillis(), SensorType.Light);
        r.setValue(20.0);
        
        return r;
    }

    @POST
    @Path("/")
    public Response post(Object o) 
    {
        // TODO: Implementation for HTTP POST request
        LOGGER.debug("post invoked. data: " + o);
        SensorRecord r = LoRaJsonConvertor.getInstance().convert(o.toString());
        boolean result = repo.insert(r);
        if (result) 
            return Response.ok().build();
        else
            return Response.serverError().build();
    }
    @PUT
    @Path("/")
    public void put() {
        // TODO: Implementation for HTTP PUT request
        LOGGER.debug("put invoked.");
    }

    @DELETE
    @Path("/")
    public void delete() {
        // TODO: Implementation for HTTP DELETE request
        LOGGER.debug("delete invoked.");
    }
}
