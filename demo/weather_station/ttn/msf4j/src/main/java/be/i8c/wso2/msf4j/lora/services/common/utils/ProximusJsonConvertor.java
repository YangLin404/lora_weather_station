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

package be.i8c.wso2.msf4j.lora.services.utils;


import be.i8c.wso2.msf4j.lora.models.ProximusSensor;
import be.i8c.wso2.msf4j.lora.models.SensorBuilder;
import be.i8c.wso2.msf4j.lora.models.SensorRecord;
import be.i8c.wso2.msf4j.lora.models.SensorType;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.stream.Stream;

/**
 * This class is used to convert json object into model class ProximusSensor and vice versa.
 *
 * Note: It's necessary to use this class to convert the json object from Proximus,
 * because the structure of json object from Proximus is different per SensorType.
 *
 * @author yanglin
 */

@Component
@Profile("proximus")
public class ProximusJsonConvertor {

    private static final Logger LOGGER = LogManager.getLogger(ProximusJsonConvertor.class);

    private Gson gson;

    @PostConstruct
    private void init()
    {
        gson = new Gson();
    }

    /**
     * This method is used to convert a json object forwarded from Proximus-enco into a ProximusSensor object.
     * @param s json object to be converted.
     * @return a ProximusSensor object
     */
    public SensorRecord convertFromProximus(String s)
    {
        LOGGER.info("converting json object");
        LOGGER.debug("object: " + s);
        ProximusSensor r = gson.fromJson(s, ProximusSensor.class);
        JsonObject jo = new JsonParser().parse(s).getAsJsonObject();
        SensorType sensorType = getTypeFromJSON(jo);
        SensorBuilder sensorBuilder = new SensorBuilder(r);
        SensorRecord record = sensorBuilder
                .setType(sensorType)
                .setValue(getValueFromJSON(jo,sensorType))
                .build();
        LOGGER.info("json object successfully converted to " + record.simpleString());
        LOGGER.debug("converted Record object: " + record.toString());
        return record;
    }

    /**
     * This method is used to convert a ProximusSensor object into json object in string format.
     * @param t ProximusSensor object to be convert
     * @return json object in string format
     */
    public String convertToJsonString(ProximusSensor t)
    {
        return this.gson.toJson(t, t.getClass());
    }

    /**
     * This method is used to found sensor value from a json object forwarded from Proximus
     * @param j json object received from Proximus-enco
     * @param s Type of sensor
     * @return value of sensor
     */
    private Double getValueFromJSON(JsonObject j, SensorType s)
    {
        LOGGER.info("getting value from JSONobject: " + j);
        LOGGER.debug("getting sensortype: [" + s + "] value from JSON: " + j);
        return Double.parseDouble(j.get(s.getValueString()).getAsString());
    }

    /**
     * This method is used to found sensor type from a json object forwarded from Proximus
     * @param j json object forwarded from Proximus
     * @return type of sensor
     */
    private SensorType getTypeFromJSON(JsonObject j)
    {
        LOGGER.info("getting sensortype from JSONobject: " + j);
        String desc = j.get("streamDescription").getAsString();
        LOGGER.debug("streamDescription: [" + desc + "]");
        SensorType sensorType =
                Stream.of(SensorType.values())
                        .filter(s -> this.compDesc(desc,s.getDesc()))
                        .findFirst()
                        .get();
        LOGGER.debug("found sensortype : [" + sensorType + "]");
        return sensorType;
    }

    /**
     * This method is used to check if first string contains second string
     * @param desc first string
     * @param typeDesc second string
     * @return boolean
     */
    private boolean compDesc(String desc, String typeDesc)
    {
        LOGGER.debug("comparing [" + desc + "] to [" + typeDesc + "]");
        return desc.toLowerCase().contains(typeDesc);
    }
}
