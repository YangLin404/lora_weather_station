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

import be.i8c.wso2.msf4j.lora.services.utils.PayloadDecoder;
import be.i8c.wso2.msf4j.lora.services.utils.UplinkMessageValidator;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thethingsnetwork.data.mqtt.Client;

import java.net.URISyntaxException;

/**
 * Configuration class used by the Spring IoC container as a source of bean definitions.
 * Created by yanglin on 13/04/17.
 */
@Configuration
public class AppConfig
{
    /**
     * payload format loaded from application.properties
     */
    @Value("${decoder.format}")
    private String format;

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

    @Bean
    public TransportClient transportClient()
    {
        return new PreBuiltTransportClient(Settings.EMPTY);
    }

    @Bean
    public PayloadDecoder payloadDecoder()
    {
        return new PayloadDecoder(format, uplinkMessageValidator());
    }

    @Bean
    public UplinkMessageValidator uplinkMessageValidator()
    {
        return new UplinkMessageValidator();
    }

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
