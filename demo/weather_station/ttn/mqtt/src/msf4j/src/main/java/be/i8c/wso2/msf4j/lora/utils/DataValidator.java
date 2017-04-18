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

import be.i8c.wso2.msf4j.lora.models.SensorRecord;
import be.i8c.wso2.msf4j.lora.models.SensorType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is used to validate the integrity of data decoded from uplinkMessage using PayloadDecoder class
 * Created by yanglin on 10/04/17.
 */
@Component
public class DataValidator
{
    private static final Logger logger = LogManager.getLogger(PayloadDecoder.class);

    public DataValidator()
    {

    }

    /**
     * Validates a list of sensorRecord decoded from uplinkMesssage and filter out the invalid sensorRecord(s)
     * @param records a list of sensorRecord to be validated.
     * @return List of valid sensorRecord. null when param is null or empty, as well as when all sensorRecord are invalid.
     */
    public List<SensorRecord> validateAll(List<SensorRecord> records)
    {
        if (records == null)
        {
            logger.error("list of records to be validated is null");
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
                    .filter(r -> this.validate(r) != null)
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
     * validates a single sensorRecord.
     * @param record sensorRecord to be validated.
     * @return same object when it is valid, null when invalid or param is null.
     */
    public SensorRecord validate(SensorRecord record)
    {
        if (record != null)
        {
            double min = record.getType().getMin();
            double max = record.getType().getMax();
            logger.debug("validating record: {}", record.simpleString());
            logger.debug("range are {} - {}", min, max);
            if (record.getSensorValue() > max || record.getSensorValue() < min) {
                logger.warn("record: {} is invalid. It will be filter out.", record.simpleString());
                return null;
            } else
                logger.debug("record: {} is valid.", record.simpleString());
            return record;
        }
        else
            return null;
    }


}
