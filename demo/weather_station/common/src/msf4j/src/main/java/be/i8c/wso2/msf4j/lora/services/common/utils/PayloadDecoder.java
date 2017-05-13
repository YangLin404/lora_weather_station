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

package be.i8c.wso2.msf4j.lora.services.common.utils;

import be.i8c.wso2.msf4j.lora.models.common.Device;
import be.i8c.wso2.msf4j.lora.models.common.SensorBuilder;
import be.i8c.wso2.msf4j.lora.models.common.SensorRecord;
import be.i8c.wso2.msf4j.lora.models.common.SensorType;
import be.i8c.wso2.msf4j.lora.models.ttn.Uplink;
import be.i8c.wso2.msf4j.lora.services.common.utils.exceptions.PayloadFormatException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * It's an untilty class which decodes the lora payload en converts into SensorRecord class
 *
 * Created by yanglin on 6/04/17.
 */
@Component
public class PayloadDecoder
{

    private static final Logger logger = LogManager.getLogger(PayloadDecoder.class);

    /**
     * This class validates the integrity of raw payload.
     */
    @Autowired
    private UplinkMessageValidator validator;


    /**
     * Constructor used by Spring for initialization.
     * @param validator An instance of uplinkMessageValidator class
     */
    public PayloadDecoder(UplinkMessageValidator validator)
    {
        this.validator = validator;
    }

    /**
     * Decodes an incoming uplinkMessage into a list of SensorRecords.
     * @param data The uplinkMessage to be decoded.
     * @return A list of SensorRecords when decoding succeed.
     * @throws PayloadFormatException when raw payload of uplinkMessage doesn't match the field payloadFormat.
     */
    public List<SensorRecord> decodePayload(Uplink data, Device device) throws PayloadFormatException
    {
        List<SensorRecord> records = new LinkedList<>();
        logger.debug("convert raw payload to hex string: {}", Arrays.toString(data.getPayloadRaw()) );
        List<String> payloadHexString = convertPayloadToHex(data.getPayloadRaw());
        logger.debug("payloadString to be converted: {}", payloadHexString);
        if (validator.isRawPayloadValid(payloadHexString, device.getPayloadFormat()))
        {
            int teller = 0;
            SensorBuilder sensorBuilder = prepareBuilder(data);
            for (SensorType type : device.getPayloadFormat()) {
                logger.debug("building sensor type: " + type + " teller: " + teller);
                SensorRecord recordToAdd = sensorBuilder.setType(type)
                        .setValue(getValueFromPayload(payloadHexString, teller,type))
                        .build();
                logger.debug("record to add: " + recordToAdd.simpleString());
                records.add(recordToAdd);
                teller = teller + 2;
            }
            return records;
        }
        else
        {
            logger.error("size of payload string doesnt match wanted sensorTypes");
            throw new PayloadFormatException();
        }
    }

    /**
     * Converts raw payload from Bytes to a list of hexadecimal String.
     * @param payload Raw payload in a array of bytes.
     * @return A list of Hexadecimal String.
     */
    private List<String> convertPayloadToHex(byte[] payload)
    {
        List<String> payloadHexString = new ArrayList<>();
        for (byte b : payload) {
            payloadHexString.add(String.format("%02X", b));
        }
        return payloadHexString;
    }

    /**
     * This method prepares the SensorBuilder.
     * It is needed because the payload of same uplinkMessage will have same deviceId and timestamp.
     * @param uplinkMessage uplinkMessage te be decoded.
     * @return An instance of SensorBuilder with pre-set deviceId and timestamp.
     */
    private SensorBuilder prepareBuilder(Uplink uplinkMessage)
    {
        Instant instant = Instant.parse(uplinkMessage.getMetadata().getTime());
        return new SensorBuilder()
                .setDeviceId(uplinkMessage.getDevId())
                .setTimestamp(instant.toEpochMilli())
                .setCounter(uplinkMessage.getCounter());
    }

    /**
     * This method translates the hexadecimal String into value of sensor.
     * @param payload List of hexadecimal Strings of payload.
     * @param teller index of hexadecimal String to be read.
     * @param  type Type of sensor
     * @return the value of sensor.
     */
    private double getValueFromPayload(List<String> payload, int teller, SensorType type)
    {
        logger.debug("reading value from payload");
        String hexValue = payload.get(teller) + payload.get(++teller);
        int rawValue = Integer.parseInt(hexValue, 16);
        double actualValue = rawValue / type.getFactor();
        logger.debug("hex string: " + hexValue);
        logger.debug("raw value: " + rawValue);
        if (type == SensorType.BatteryLevel)
        {
            logger.info("battery voltage: {}", actualValue);
            return ((double) (Math.round(((actualValue - 3.3000d) / 0.0090d) * 100))) / 100;
        }
        else {
            return actualValue;
        }
    }


}
