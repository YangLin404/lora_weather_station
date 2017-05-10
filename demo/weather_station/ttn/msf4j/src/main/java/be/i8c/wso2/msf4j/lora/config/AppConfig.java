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
import be.i8c.wso2.msf4j.lora.services.utils.exceptions.PayloadFormatNotDefinedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
