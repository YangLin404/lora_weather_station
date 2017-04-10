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

package be.i8c.wso2.msf4j.lora.utils;

import be.i8c.wso2.msf4j.lora.models.SensorBuilder;
import be.i8c.wso2.msf4j.lora.models.SensorRecord;
import be.i8c.wso2.msf4j.lora.models.SensorType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thethingsnetwork.data.common.messages.UplinkMessage;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * It's an untilty class which decode the lora payload into SensorRecord class
 *
 * Created by yanglin on 6/04/17.
 */
@Component
public class PayloadDecoder
{

    private static final Logger logger = LogManager.getLogger(PayloadDecoder.class);

    @Value("${decoder.format}")
    private String format;

    private List<SensorType> payloadFormat;

    public PayloadDecoder()
    {

    }

    @PostConstruct
    public void init()
    {
        payloadFormat = getPayloadFormat();
    }

    public List<SensorRecord> decodePayload(UplinkMessage data)
    {
        List<SensorRecord> records = new LinkedList<>();
        logger.debug("convert raw payload to hex string: {}", Arrays.toString(data.getPayloadRaw()) );
        List<String> payloadHexString = convertPayloadToHex(data.getPayloadRaw());
        logger.debug("payloadString to be converted: {}", payloadHexString);
        int teller=0;
        SensorBuilder sensorBuilder = prepareBuilder(data);
        for (SensorType type : this.payloadFormat)
        {
            logger.debug("building sensor type: " + type + " teller: " + teller);
            SensorRecord recordToAdd = sensorBuilder.setType(type)
                    .setValue(getValueFromPayload(payloadHexString,teller))
                    .build();
            logger.debug("record to add: " + recordToAdd.simpleString());
            records.add(recordToAdd);
            teller = teller+2;
        }
        return records;
    }

    private List<SensorType> getPayloadFormat()
    {
        logger.debug("getting payloadFormat from application.properties");
        List<SensorType> types = new LinkedList<>();
        for (String s : this.format.split(","))
            types.add(SensorType.valueOf(s));
        logger.debug("payloadFormat: " + types.toString());
        return types;
    }
    /*
    private SensorRecord decodeSensor(SensorType t, Integer firstByte, Integer )
    {

    }
    */

    private List<String> convertPayloadToHex(byte[] payload)
    {
        List<String> payloadHexString = new ArrayList<>();
        for (byte b : payload)
            payloadHexString.add(String.format("%02X", b));
        return payloadHexString;
    }

    private SensorBuilder prepareBuilder(UplinkMessage uplinkMessage)
    {
        Instant instant = Instant.parse(uplinkMessage.getMetadata().getTime());
        return new SensorBuilder()
                .setDeviceId(uplinkMessage.getDevId())
                .setTimestamp(instant.toEpochMilli());
    }

    private double getValueFromPayload(List<String> payload, int teller)
    {
        logger.debug("reading value from payload");
        String hexValue = payload.get(teller) + payload.get(++teller);
        logger.debug("hex string: " + hexValue);
        int rawValue = Integer.parseInt(hexValue,16);
        logger.debug("raw value: " + rawValue);
        return rawValue/10.0d;
    }
}
