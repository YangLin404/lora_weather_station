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

package be.i8c.wso2.msf4j.lora.services;
import be.i8c.wso2.msf4j.lora.models.DownlinkRequest;
import be.i8c.wso2.msf4j.lora.models.SensorRecord;
import be.i8c.wso2.msf4j.lora.repositories.LoRaRepository;
import be.i8c.wso2.msf4j.lora.utils.PayloadDecoder;
import be.i8c.wso2.msf4j.lora.utils.DataValidator;
import be.i8c.wso2.msf4j.lora.utils.PayloadEncoder;
import be.i8c.wso2.msf4j.lora.utils.UplinkMessageValidator;
import be.i8c.wso2.msf4j.lora.utils.exceptions.PayloadFormatException;
import be.i8c.wso2.msf4j.lora.utils.exceptions.PayloadFormatNotDefinedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thethingsnetwork.data.common.Connection;
import org.thethingsnetwork.data.common.messages.DataMessage;
import org.thethingsnetwork.data.common.messages.DownlinkMessage;
import org.thethingsnetwork.data.common.messages.UplinkMessage;
import org.thethingsnetwork.data.mqtt.Client;

import javax.annotation.PostConstruct;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * This service class is used to manage communication with MQTT backend of TTN by using TTN JAVA SDK and as well used to manage the client class.
 *
 */
@Service
public class LoRaTTNService
{
    private static final Logger logger = LogManager.getLogger(LoRaTTNService.class);

    /**
     * MQTT client of TTN JAVA SDK, used to communicate with MQTT backend of TTN.
     */
    private Client client;

    /**
     * Repository class used to communicate with database.
     */
    @Autowired
    private LoRaRepository repo;
    /**
     * An instance of PayloadDecoder class, used to decode the payload of uplinkMessage into SensorRecord.
     */
    @Autowired
    private PayloadDecoder decoder;
    /**
     * An instance of DataValidator class, used to validate the integrity of data to be inserted.
     */

    @Autowired
    private PayloadEncoder encoder;

    @Autowired
    private DataValidator dataValidator;
    /**
     * An instance of UplinkMessageValidator class, used to validate the uplinkMessage.
     */
    @Autowired
    private UplinkMessageValidator uplinkMessageValidator;

    /**
     * Constructor used by spring for dependency injection
     * @param mqttClient An instance of mqttClient of TTN JAVA SDK.
     */
    public LoRaTTNService(Client mqttClient)
    {
        this.client = mqttClient;
    }

    /**
     * The initialization method of class LoRaTTNService.
     * It registers a set of handlers to mqttClient of TTN JAVA SDK and start the client.
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
                if (!uplinkMessageValidator.isDuplicatedData(uplinkData))
                {
                    try {
                        logger.info("uplinkmessage with counter {} received.", uplinkData.getCounter());
                        logger.debug("converting new uplinkmessage");
                        List<SensorRecord> records = decoder.decodePayload(uplinkData);
                        logger.debug("uplinkmessage converted.");
                        records.forEach(r -> logger.debug(r.simpleString()));
                        logger.debug("start validating {} records", records.size());
                        records = dataValidator.validateAll(records);
                        if (records != null)
                        {
                            logger.info("saving records into database.");
                            List savedRecords = repo.save(records);
                            if (savedRecords != null) {
                                logger.info("uplinkmessage with counter {} saved.", uplinkData.getCounter());
                                logger.debug("saved data: \n {}", savedRecords.toString());
                            }
                            else
                                logger.error("saving records into database fails, please check logs of repository class for detailed information");
                        }
                        else
                            logger.warn("all records are invalid. ignore uplinkmessage counter {}", uplinkData.getCounter());
                    }catch (PayloadFormatException e)
                    {
                        logger.error(e.getMessage());
                        logger.debug(Arrays.toString(e.getStackTrace()));
                    }catch (PayloadFormatNotDefinedException e)
                    {
                        logger.error(e.getMessage());
                        logger.debug(Arrays.toString(e.getStackTrace()));
                    }
                }
                else
                    logger.info("duplicated data with counter {} received, ignore.", uplinkData.getCounter());
            });
            client.start();
        }catch (URISyntaxException e) {
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * Start the mqtt client.
     * @throws Exception when mqtt client cannot be started or it is already connected.
     */
    public void startClient() throws Exception {
        logger.info("starting mqtt client");
        this.client.start();
        logger.info("mqtt client started");
    }

    /**
     * Stop the mqtt client.
     * @throws Exception when mqtt client connot be stopped.
     */
    public void stopClient() throws Exception {
        logger.info("stopping mqtt client");
        this.client.end();
        logger.info("mqtt client stopped");
    }

    public void sendDownlink(DownlinkRequest request) throws Exception
    {
        logger.debug("payload string are: {}", request.getPayloadString());
        byte[] _payload = encoder.encode(request.getPayloadString());
        logger.debug("payload are: " + Arrays.toString(_payload));
        DownlinkMessage d = new DownlinkMessage(1, _payload);
        this.client.send(request.getDeviceId(),d);
    }
}
