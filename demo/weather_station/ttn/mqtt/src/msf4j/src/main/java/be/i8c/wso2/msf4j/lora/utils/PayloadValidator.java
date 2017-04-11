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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yanglin on 10/04/17.
 */
@Component
public class PayloadValidator
{
    private static final Logger logger = LogManager.getLogger(PayloadDecoder.class);

    public PayloadValidator()
    {

    }

    public List<SensorRecord> validate(List<SensorRecord> records)
    {
        List<SensorRecord> validRecords = records.stream()
                .filter(this::validate)
                .collect(Collectors.toList());
        logger.info("validating record finished, {} record are valid.", validRecords.size());
        logger.debug("valid records are: \n {}", validRecords.toString());
        return validRecords;

    }

    private boolean validate(SensorRecord record)
    {
        double min = record.getType().getMin();
        double max = record.getType().getMax();
        logger.debug("validating record: {}", record.simpleString());
        logger.debug("range are {} - {}", min,max);
        if (record.getSensorValue()>max || record.getSensorValue()< min)
        {
            logger.warn("record: {} is invalid. It will be filter out.", record.simpleString());
            return false;
        }
        else
            logger.debug("record: {} is valid.", record.simpleString());
            return true;
    }
}
