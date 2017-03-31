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

package be.i8c.wso2.msf4j.lora.Services;


import be.i8c.wso2.msf4j.lora.models.SensorRecord;
import be.i8c.wso2.msf4j.lora.models.SensorType;
import be.i8c.wso2.msf4j.lora.repositories.LoRaRepository;
import be.i8c.wso2.msf4j.lora.utils.LoRaJsonConvertor;

import java.util.Calendar;

import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * This is Microservice source class
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

    /**
     * This method return a sensorRecord in xml format
     * @return sensorRecord
     */
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_XML)
    public SensorRecord getXml() {
        // TODO: Implementation for HTTP GET request
        LOGGER.debug("get invoked");
        SensorRecord r = new SensorRecord("x", "x", "1", "x", Calendar.getInstance().getTimeInMillis(), SensorType.Light);
        r.setSensorValue(20.0);
        
        return r;
    }

    /**
     * This method return a sensorRecord in json format
     * @return sensorRecord
     */
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public SensorRecord getJson() {
        // TODO: Implementation for HTTP GET request
        LOGGER.debug("get invoked");
        SensorRecord r = new SensorRecord("x", "x", "1", "x", Calendar.getInstance().getTimeInMillis(), SensorType.Light);
        r.setSensorValue(20.0);

        return r;
    }

    /**
     * This method is used to save object into database
     * @param o object to be saved
     * @return ok when save successful, code 500 when save fails.
     */
    @POST
    @Path("/")
    public Response post(Object o) 
    {
        // TODO: Implementation for HTTP POST request
        LOGGER.debug("post invoked. data: " + o);
        SensorRecord r = LoRaJsonConvertor.getInstance().convert(o.toString());
        SensorRecord result = repo.save(r);
        if (result != null)
            return Response.ok().build();
        else
            return Response.serverError().build();
    }

    @POST
    @Path("/testing")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(SensorRecord sensorRecord)
    {
        LOGGER.info("testing post invoked. data: " + sensorRecord.toString());
        if (sensorRecord.getType() == null)
        {
            LOGGER.warn("data is empty: " + sensorRecord.toString());
            return Response.serverError().build();
        }
        else
        {
            SensorRecord result = repo.save(sensorRecord);
            if (result != null)
                return Response.ok().build();
            else
                return Response.serverError().build();
        }
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
