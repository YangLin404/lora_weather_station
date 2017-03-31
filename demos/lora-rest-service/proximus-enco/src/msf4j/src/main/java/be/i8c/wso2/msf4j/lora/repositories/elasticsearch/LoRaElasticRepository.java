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
package be.i8c.wso2.msf4j.lora.repositories.elasticsearch;

import be.i8c.wso2.msf4j.lora.models.SensorRecord;

import javax.annotation.PostConstruct;

import be.i8c.wso2.msf4j.lora.repositories.LoRaRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

/**
 * This is a implementation of LoRaRepository for elasticsearch server
 * @author yanglin
 */
@Profile("elasticsearch")
@Repository
public class LoRaElasticRepository implements LoRaRepository
{
    private static final Logger LOGGER = LogManager.getLogger(LoRaElasticRepository.class);

    /**
     * a object to communicate with elasticsearch
     */
    @Autowired
    private LoRaElasticsearchAdapter esa;
    
    public LoRaElasticRepository()
    {
        
    }

    /**
     * This method is used to index a document into a index.
     * @param sensorRecord document to be indexed.
     * @return the indexed sensorRecord or null when index operation fails.
     */
    @Override
    public SensorRecord save(SensorRecord sensorRecord)
    {
        return esa.save(sensorRecord);
    }
}