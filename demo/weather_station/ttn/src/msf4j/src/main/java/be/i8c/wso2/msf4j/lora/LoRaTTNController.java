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

import be.i8c.wso2.msf4j.lora.models.DownlinkRequest;
import be.i8c.wso2.msf4j.lora.services.LoRaTTNService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;

/**
 *
 * This is the micro service class based on msf4j. It is used to handle the http request such as start and stop the mqtt client.
 *
 * @since 0.1-SNAPSHOT
 *
 */
@Component
@Path("/api/ttn")
public class LoRaTTNController {
    private static final Logger logger = LogManager.getLogger(LoRaTTNController.class);


    @Autowired
    private LoRaTTNService service;


    public LoRaTTNController() {

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
     * A post method to send the downlink message to specific devices
     * @param payload An instance of DownlinkRequest which contains the device id and payload to be sent out.
     * @return code 204 when downlink message successfully sent out, code 500 when exception occurred.
     */
    @POST
    @Path("/manage/downlink")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response downlink(DownlinkRequest payload) {
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
}
