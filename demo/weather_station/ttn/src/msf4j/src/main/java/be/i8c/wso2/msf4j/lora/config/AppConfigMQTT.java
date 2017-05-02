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

package be.i8c.wso2.msf4j.lora.config;

import be.i8c.wso2.msf4j.lora.models.Device;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.thethingsnetwork.data.mqtt.Client;

import java.net.URISyntaxException;
import java.util.Map;

/**
 * Spring Application configuration class for implementation using mqtt protocol.
 * Created by yanglin on 27/04/17.
 */

@Configuration
@Profile("mqtt")
public class AppConfigMQTT {


    private static final Logger logger = LogManager.getLogger(AppConfigMQTT.class);
    /**
     * the broker address, loaded from application.properties
     */
    @Value("${ttn.region}")
    private String region;

    /**
     * the appId, loaded from application.properties
     */
    @Value("${ttn.appId}")
    private String appId;

    /**
     * the accesskey, loaded from application.properties
     */
    @Value("${ttn.accessKey}")
    private String accessKey;

    @Autowired
    private Map<String,Device> devices;

    /**
     * this bean creates a instance of mqtt Client of TTN with parameters loaded from application.properties.
     * MQTT Client is required for communication with TTN back-end.
     * @return a instance of MQTT Client of TTN.
     */

    @Bean
    public Client client()
    {
        try {
            return new Client(region,appId,accessKey);
        } catch (URISyntaxException e) {
            throw new BeanCreationException("ttn mqtt client", e.getMessage(), e);
        }
    }
}
