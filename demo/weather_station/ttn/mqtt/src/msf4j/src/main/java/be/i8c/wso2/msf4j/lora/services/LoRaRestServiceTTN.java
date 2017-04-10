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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thethingsnetwork.data.common.Connection;
import org.thethingsnetwork.data.common.messages.DataMessage;
import org.thethingsnetwork.data.common.messages.UplinkMessage;
import org.thethingsnetwork.data.mqtt.Client;

import javax.annotation.PostConstruct;
import javax.ws.rs.Path;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * This is the micro service class based on msf4j.
 * It's used to manage communication with MQTT backend of TTN by using TTN JAVA SDK.
 *
 * @since 0.1-SNAPSHOT
 *
 */
@Component
@Path("api/ttn")
public class LoRaRestServiceTTN
{
    private static final Logger logger = LogManager.getLogger(LoRaRestServiceTTN.class);

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

    private UplinkMessage previousData;

    public LoRaRestServiceTTN()
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

            });
            client.onConnected((Connection _client) -> logger.info("successfully connected to TTN server."));
            client.onMessage((String devId, DataMessage data) ->
                    {
                        UplinkMessage uplinkData = (UplinkMessage) data;
                        if (!this.isDuplicatedData(uplinkData))
                        {
                            logger.info("uplinkmessage with counter {} received.", uplinkData.getCounter());
                            previousData = uplinkData;
                            logger.debug("converting new uplinkmessage");
                            List<SensorRecord> records = decoder.decodePayload(uplinkData);
                            logger.debug("uplinkmessage converted.");
                            records.forEach(r -> logger.debug(r.simpleString()));
                            logger.debug("saving uplinkmessage to database");
                            if (repo.save(records) != null)
                            {
                                logger.info("uplinkmessage with counter {} saved.", uplinkData.getCounter());
                                logger.debug("saved data:");
                                records.forEach(r -> logger.debug(r.simpleString()));
                            }

                        }
                        else
                            logger.info("duplicated data with counter {} received, ignore.", uplinkData.getCounter());
                    });
            client.start();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private boolean isDuplicatedData(UplinkMessage data)
    {
        if (previousData == null)
            return false;
        else
        {
            String timePre = previousData.getMetadata().getTime();
            String timeCur = data.getMetadata().getTime();
            if (timeCur.equals(timePre))
            {
                byte[] payloadPre = previousData.getPayloadRaw();
                byte[] payloadCur = data.getPayloadRaw();
                if (Arrays.equals(payloadCur, payloadPre))
                    return true;
            }
            return false;
        }
    }


}
