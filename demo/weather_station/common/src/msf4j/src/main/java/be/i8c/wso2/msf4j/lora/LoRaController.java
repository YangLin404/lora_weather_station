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

package be.i8c.wso2.msf4j.lora;

import be.i8c.wso2.msf4j.lora.models.proximus.ProximusDownlinkRequest;
import be.i8c.wso2.msf4j.lora.models.ttn.TTNDownlinkRequest;
import be.i8c.wso2.msf4j.lora.models.ttn.TTNUplink;
import be.i8c.wso2.msf4j.lora.services.common.AbstractLoRaService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;

/**
 * This is the micro service class based on msf4j. It is used to handle the http request.
 * Currently available API are:
 * - start the MQTT client
 * - stop the MQTT client
 * - send downlinkmessage to specific device
 * @since 0.1-SNAPSHOT
 *
 */
@Component
@Path("/lora/api")
public class LoRaController {
    private static final Logger logger = LogManager.getLogger(LoRaController.class);


    @Autowired
    private AbstractLoRaService service;

    @Autowired
    private Environment env;

    private boolean isHttp;
    private boolean isProximus;


    public LoRaController() {

    }

    @PostConstruct
    public void init()
    {
        logger.debug(Arrays.toString(env.getActiveProfiles()));
        isHttp = Arrays.asList(env.getActiveProfiles()).contains("http");
        isProximus = Arrays.asList(env.getActiveProfiles()).contains("proximus");
        logger.debug("is http?: {}", isHttp);
        logger.debug("is proximus?: {}", isProximus);
    }

    /**
     * A post method to start the mqtt client.
     *
     * @return code 204 when successfully starts the mqtt client, code 500 when exception occurred.
     */
    @POST
    @Path("/manage/startClient")
    public Response startClient() {
        try {
            service.startClient();
            return Response.accepted().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    /**
     * A post method to stop the mqtt client.
     *
     * @return code 204 when successfully starts the mqtt client, code 500 when exception occurred.
     */
    @POST
    @Path("/manage/stopClient")
    public Response stopClient() {
        try {
            service.stopClient();
            return Response.accepted().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    /**
     * A post method to send the downlink message to specific devices through ttn
     * @param payload An instance of TTNDownlinkRequest which contains the device id and payload to be sent out.
     * @return code 204 when downlink message successfully sent out, code 500 when exception occurred.
     */
    @POST
    @Path("/proximus/downlink")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response downlink(TTNDownlinkRequest payload) {
        try {
            service.sendDownlink(payload);
            return Response.accepted().build();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
            return Response.serverError().
                    entity(e.getMessage()).
                    build();
        }
    }


    /**
     * A post method to send the downlink message to specific devices through proximus
     * @param payload An instance of ProximusDownlinkRequest which contains the device id and payload to be sent out.
     * @return code 204 when downlink message successfully sent out, code 500 when exception occurred.
     */
    @POST
    @Path("/proximus/downlink")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response downlink(ProximusDownlinkRequest payload) {
        try {
            service.sendDownlink(payload);
            return Response.accepted().build();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
            return Response.serverError().
                    entity(e.getMessage()).
                    build();
        }
    }

    @POST
    @Path("/ttn/uplink")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response uplink(TTNUplink TTNUplinkMessage)
    {
        if (isHttp) {
            try {
                service.save(TTNUplinkMessage);
                return Response.accepted().build();
            }catch (RuntimeException e)
            {
                logger.error(e.getMessage());
                logger.debug(Arrays.toString(e.getStackTrace()));
                return Response.serverError().entity(e.getMessage()).build();
            }
        }
        else
        {
            logger.debug("http message incoming, but http integration not enable.");
            return Response.accepted().build();
        }
    }

    @POST
    @Path("/proximus/uplink")
    public Response post(Object o)
    {
        if (isProximus) {
            try {
                service.save(o.toString());
                return Response.accepted().build();
            }catch (RuntimeException e)
            {
                logger.error(e.getMessage());
                logger.debug(Arrays.toString(e.getStackTrace()));
                return Response.serverError().entity(e.getMessage()).build();
            }

        }
        else
        {
            logger.debug("proximus uplink incoming, but profile proximus not enable.");
            return Response.accepted().build();
        }


    }

}
