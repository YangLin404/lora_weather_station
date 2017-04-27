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
import be.i8c.wso2.msf4j.lora.models.SensorType;
import be.i8c.wso2.msf4j.lora.services.utils.PayloadDecoder;
import be.i8c.wso2.msf4j.lora.services.utils.UplinkMessageValidator;
import be.i8c.wso2.msf4j.lora.services.utils.exceptions.PayloadFormatNotDefinedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thethingsnetwork.data.mqtt.Client;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Configuration class used by the Spring IoC container as a source of bean definitions.
 * Created by yanglin on 13/04/17.
 */
@Configuration
public class AppConfig
{

    private static final Logger logger = LogManager.getLogger(AppConfig.class);
    /**
     * payload format loaded from application.properties
     */

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

    /**
     * list of deviceIds of the devices which we are expecting, loaded from application.properties
     */
    @Value("#{'${device.deviceid}'.split(';')}")
    private List<String> deviceIds;


    /**
     * list of the payloadformat for each devices, loaded form application.properties
     */
    @Value("#{'${device.format}'.split(';')}")
    private List<String> formats;

    /**
     * this bean creates a instance of prebuilt transportclient of elasticsearch API with default setting.
     * @return a instance TransportClient
     */
    @Bean
    public TransportClient transportClient()
    {
        return new PreBuiltTransportClient(Settings.EMPTY);
    }

    /**
     * this bean creates a instance payloadDecoder used for payload decoding.
     * @return a instance PayloadDecoder
     */
    @Bean
    public PayloadDecoder payloadDecoder()
    {
        return new PayloadDecoder(uplinkMessageValidator());
    }

    /**
     * this bean creates a instance of uplinkMessageValidator used for validating of uplinkMessages.
     * @return a instance PayloadDecoder
     */
    @Bean
    public UplinkMessageValidator uplinkMessageValidator()
    {
        return new UplinkMessageValidator();
    }

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

    /**
     * this bean creates a list of devices defined in application.properties.
     * @return a Map with deviceId as key and instance of Device as Value.
     */
    @Bean
    public Map<String,Device> devices()
    {
        Map<String,Device> devices = new HashMap<>();
        deviceIds.forEach(id -> {
            try {
                List<SensorType> payloadFormat = getPayloadFormat(formats.get(deviceIds.indexOf(id)));
                Device device = new Device(id,payloadFormat);
                devices.put(id,device);
            } catch (PayloadFormatNotDefinedException e) {
                logger.error(e.getMessage());
            }
        });
        return devices;
    }

    /**
     * This method is used to convert payload format in String into a list of SensorType.
     * @return A list of SensorType.
     * @throws PayloadFormatNotDefinedException When payload format in String is empty or null.
     */
    private List<SensorType> getPayloadFormat(String format) throws PayloadFormatNotDefinedException
    {
        logger.debug("getting payloadFormat from application.properties");
        if (format == null || format.isEmpty())
            throw new PayloadFormatNotDefinedException();
        List<SensorType> types = new LinkedList<>();
        for (String s : format.split(","))
            types.add(SensorType.valueOf(s));
        logger.debug("payloadFormat: " + types.toString());
        return types;
    }
}
