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
import be.i8c.wso2.msf4j.lora.models.ProximusSensor;
import be.i8c.wso2.msf4j.lora.models.SensorRecord;
import be.i8c.wso2.msf4j.lora.models.Uplink;
import be.i8c.wso2.msf4j.lora.services.exceptions.DownlinkException;
import be.i8c.wso2.msf4j.lora.services.utils.ProximusJsonConvertor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * Created by yanglin on 11/05/17.
 */

@Service
@Profile("proximus")
public class LoRaProximusHTTPService extends AbstractLoRaService {

    private static final Logger logger = LogManager.getLogger(LoRaProximusHTTPService.class);

    @Autowired
    private ProximusJsonConvertor jsonConvertor;

    private boolean isRunning;

    @PostConstruct
    private void init()
    {
        isRunning = true;

    }

    @Override
    public void startClient() throws Exception {
        isRunning = true;
    }

    @Override
    public void stopClient() throws Exception {
        isRunning = false;
    }

    @Override
    public void save(String jsonString) throws RuntimeException
    {
        SensorRecord sensorRecord = jsonConvertor.convertFromProximus(jsonString);
        super.saveToRepo(sensorRecord);
    }

    @Override
    public void sendDownlink(DownlinkRequest request) throws DownlinkException {

    }

    @Override
    protected void log(String log, Level level) {
        logger.log(level,log);
    }
}
