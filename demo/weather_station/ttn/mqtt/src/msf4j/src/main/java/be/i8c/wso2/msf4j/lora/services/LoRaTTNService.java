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

import be.i8c.wso2.msf4j.lora.models.SensorRecord;
import be.i8c.wso2.msf4j.lora.repositories.LoRaRepository;
import be.i8c.wso2.msf4j.lora.utils.PayloadDecoder;
import be.i8c.wso2.msf4j.lora.utils.PayloadValidator;
import be.i8c.wso2.msf4j.lora.utils.UplinkMessageValidator;
import be.i8c.wso2.msf4j.lora.utils.exceptions.PayloadFormatException;
import be.i8c.wso2.msf4j.lora.utils.exceptions.PayloadFormatNotDefinedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thethingsnetwork.data.common.Connection;
import org.thethingsnetwork.data.common.messages.DataMessage;
import org.thethingsnetwork.data.common.messages.UplinkMessage;
import org.thethingsnetwork.data.mqtt.Client;

import javax.annotation.PostConstruct;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * It's used to manage communication with MQTT backend of TTN by using TTN JAVA SDK.
 *
 */
@Service
public class LoRaTTNService
{
    private static final Logger logger = LogManager.getLogger(LoRaTTNService.class);

    private Client client;


    @Value("${ttn.region}")
    private String region;

    @Value("${ttn.appId}")
    private String appId;

    @Value("${ttn.accessKey}")
    private String accessKey;

    @Autowired
    private LoRaRepository repo;
    @Autowired
    private PayloadDecoder decoder;
    @Autowired
    private PayloadValidator payloadValidator;
    @Autowired
    private UplinkMessageValidator uplinkMessageValidator;

    public LoRaTTNService()
    {

    }

    @PostConstruct
    public void init()
    {
        try {
            client = new Client(region, appId, accessKey);
            client.onError((Throwable _error) ->
            {
                logger.error(_error.getMessage());
                logger.debug(Arrays.toString(_error.getStackTrace()));

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
                        records = payloadValidator.validateAll(records);
                        if (records != null) {
                            logger.info("saving records into database.");
                            List savedRecords = repo.save(records);
                            if (savedRecords != null) {
                                logger.info("uplinkmessage with counter {} saved.", uplinkData.getCounter());
                                logger.debug("saved data: \n {}", savedRecords.toString());
                            }
                        } else
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
            logger.debug(Arrays.toString(e.getStackTrace()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.debug(Arrays.toString(e.getStackTrace()));
        }
    }

    public void startClient() throws Exception {
        logger.info("starting mqtt client");
        this.client.start();
        logger.info("mqtt client started");
    }

    public void stopClient() throws Exception {
        logger.info("stopping mqtt client");
        this.client.end();
        logger.info("mott client stopped");
    }
}
