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

import be.i8c.wso2.msf4j.lora.models.common.*;
import be.i8c.wso2.msf4j.lora.models.ttn.TTNDownlinkRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * This class is used to validate the integrity of data decoded from uplinkMessage using PayloadDecoder class and checks the required notification.
 * Created by yanglin on 10/04/17.
 */
@Component
public class DataValidator
{
    private static final Logger logger = LogManager.getLogger(DataValidator.class);

    public DataValidator()
    {

    }

    /**
     * Validates a list of sensorRecords and filter out the invalid sensorRecord(s)
     * @param records a list of sensorRecord to be validated.
     * @return List of valid sensorRecord. null when param is null or empty, as well as when all sensorRecord are invalid.
     */
    public List<SensorRecord> validateAll(List<SensorRecord> records)
    {
        if (records == null)
        {
            logger.error("list of records to be validated are null");
            return null;
        }
        else if (records.size() == 0)
        {
            logger.warn("list of record to be validated is empty");
            return null;
        }
        else
        {
            List<SensorRecord> validRecords = records.stream()
                    .filter(SensorRecord::isValid)
                    .collect(Collectors.toList());
            logger.info("validating records finished, {} record are valid.", validRecords.size());
            logger.debug("valid records are: \n {}", validRecords.toString());
            if (validRecords.size()==0) {
                logger.warn("all records are invalid.");
                return null;
            }
            return validRecords;
        }
    }

    /**
     * Validates a sensorRecord
     * @param sensorRecord sensorRecord to be validated.
     * @return same sensorRecord if valid, null if invalid.
     */
    public SensorRecord validate(SensorRecord sensorRecord)
    {
        if (sensorRecord == null)
        {
            logger.error("record to be validated is null");
            return null;
        }
        else
        {
            return sensorRecord.isValid()?sensorRecord:null;
        }
    }

    /**
     * checks if the notification is needed for specific device when its sensor values meets certain threshold .
     * @param records a list of sensorrecords to be checked.
     * @param device The device to be checked.
     * @param func The callback function when notification should be send.
     */
    public void checkForNotification(List<SensorRecord> records, Device device, Consumer<TTNDownlinkRequest> func)
    {
        records.forEach(e -> {
            if (e.getType() == SensorType.Light) {
                if (e.getSensorValue() < 95 && !(device.getNotifiedMaps().get(NotificationType.Light_low)))
                {
                    logger.debug("Device: {} light is too low, should be notified", device.getDeviceId());
                    device.getNotifiedMaps().put(NotificationType.Light_low, true);
                    TTNDownlinkRequest downlinkRequest = new TTNDownlinkRequest(device.getDeviceId(), PreDefinedPayload.TURN_ON_LED.getPayload());
                    func.accept(downlinkRequest);
                    logger.info("Device: {} light is too low, notified", device.getDeviceId());
                }
                else if (e.getSensorValue() > 95 && device.getNotifiedMaps().get(NotificationType.Light_low))
                {
                    logger.debug("Device: {} light is back to normal, should be notified", device.getDeviceId());
                    device.getNotifiedMaps().put(NotificationType.Light_low, false);
                    TTNDownlinkRequest downlinkRequest = new TTNDownlinkRequest(device.getDeviceId(), PreDefinedPayload.TURN_OFF_LED.getPayload());
                    func.accept(downlinkRequest);
                    logger.info("Device: {} light is too low, notified", device.getDeviceId());
                }
            }
        });
    }

    /**
     * checks if the notification is needed for specific device when its sensor values meets certain threshold .
     * @param record the sensorrecord to be checked.
     * @param device The device to be checked.
     * @param func The callback function when notification should be send.
     */
    public void checkForNotification(SensorRecord record, Device device, Consumer<TTNDownlinkRequest> func)
    {
        List<SensorRecord> records = new ArrayList<>();
        records.add(record);
        this.checkForNotification(records,device,func);
    }
}
