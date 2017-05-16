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

package be.i8c.wso2.msf4j.lora.services.ttn;

import be.i8c.wso2.msf4j.lora.models.common.DownlinkRequest;
import be.i8c.wso2.msf4j.lora.models.ttn.TTNUplink;
import be.i8c.wso2.msf4j.lora.services.common.AbstractLoRaService;
import be.i8c.wso2.msf4j.lora.services.common.exceptions.DownlinkException;
import be.i8c.wso2.msf4j.lora.services.common.exceptions.UnknownDeviceException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.thethingsnetwork.data.common.Connection;
import org.thethingsnetwork.data.common.messages.DataMessage;
import org.thethingsnetwork.data.common.messages.DownlinkMessage;
import org.thethingsnetwork.data.common.messages.UplinkMessage;
import org.thethingsnetwork.data.mqtt.Client;

import javax.annotation.PostConstruct;
import java.net.URISyntaxException;
import java.util.*;

/**
 * The implementation of loRaService using MQTT protocol.
 */

@Service
@Profile("mqtt")
public class LoRaMQTTService extends AbstractLoRaService
{
    private static final Logger logger = LogManager.getLogger(LoRaMQTTService.class);

    /**
     * MQTT client of TTN JAVA SDK, used to communicate with MQTT backend of TTN.
     */
    @Autowired
    private Client client;

    public LoRaMQTTService(Client mqttClient)
    {
        this.client = mqttClient;
    }

    /**
     * The initialization method of class LoRaMQTTService.
     * It registers a set of handlers to mqttClient of TTN JAVA SDK and then start the client.
     * registered handlers are:
     * - error handler: handles when error occurred.
     * - uplink event handler: handles incoming uplink message
     */
    @PostConstruct
    public void init()
    {
        try {
            client.onError((Throwable _error) ->
            {
                logger.error(_error.getMessage());
                logger.error(Arrays.toString(_error.getStackTrace()));

            });
            client.onConnected((Connection _client) -> logger.info("successfully connected to TTN server."));
            client.onMessage((String devId, DataMessage data) ->
            {
                UplinkMessage uplinkData = (UplinkMessage) data;
                TTNUplink TTNUplink = new TTNUplink(uplinkData);
                super.save(TTNUplink);
            });
            client.start();
        }catch (URISyntaxException | UnknownDeviceException e) {
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void save(String s) throws RuntimeException {

    }

    /**
     * Start the mqtt client.
     * @throws Exception when mqtt client cannot be started or it is already connected.
     */
    @Override
    public void startClient() throws Exception {
        logger.info("starting mqtt client");
        this.client.start();
        logger.info("mqtt client started");
    }

    /**
     * Stop the mqtt client.
     * @throws Exception when mqtt client cannot be stopped.
     */
    @Override
    public void stopClient() throws Exception {
        logger.info("stopping mqtt client");
        this.client.end();
        logger.info("mqtt client stopped");
    }

    /**
     * used to send downlink message to specific device.
     * @param request An object of TTNDownlinkRequest contains deviceid and payload to be sent.
     */
    @Override
    public void sendDownlink(DownlinkRequest request) throws DownlinkException
    {
        logger.debug("payload string are: {}", request.getPayloadString());
        super.encode(request);
        logger.debug("payload are: {}", request.getPayload_raw());
        DownlinkMessage d = new DownlinkMessage(request.getPort(), request.getPayload_raw());
        try {
            this.client.send(request.getDeviceId(),d);
        } catch (Exception e) {
            throw new DownlinkException();
        }
    }

    @Override
    protected void log(String log, Level level) {
        logger.log(level,log);
    }
}
