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

package be.i8c.yanglin.loRa_RestService.utils;

import be.i8c.yanglin.loRa_RestService.models.SensorRecord;
import be.i8c.yanglin.loRa_RestService.models.SensorType;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Date;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author yanglin
 */
public class LoRaJsonConvertor 
{
    private static LoRaJsonConvertor instance = new LoRaJsonConvertor();
    
    private static final Logger LOGGER = LogManager.getLogger(LoRaJsonConvertor.class);
    private Gson gson;
    
    private LoRaJsonConvertor()
    {
        gson = new Gson();
        
    }
    
    public static LoRaJsonConvertor getInstance()
    {
        return instance;
    }
    
    public SensorRecord convert(String s)
    {
        LOGGER.info("converting json object");
        LOGGER.debug("object: " + s);
        SensorRecord r = gson.fromJson(s, SensorRecord.class);
        JsonObject jo = new JsonParser().parse(s).getAsJsonObject();
        SensorType sensorType = getTypeFromJSON(jo);
        r.setType(sensorType);
        r.setValue(getValueFromJSON(jo, sensorType));
        LOGGER.info("json object successfully converted to " + r.simpleString());
        LOGGER.debug("converted Record object: " + r.toString());
        return r;
    }
    
    public<T> String convertToJsonString(T t)
    {
        return this.gson.toJson(t, t.getClass());
    }
    
    public Double getValueFromJSON(JsonObject j, SensorType s)
    {
        LOGGER.info("getting value from JSONobject: " + j);
        LOGGER.debug("getting sensortype: [" + s + "] value from JSON: " + j);
        return Double.parseDouble(j.get(s.getValueString()).getAsString());
    }
    
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
    
    private boolean compDesc(String desc, String typeDesc)
    {
        LOGGER.debug("comparing [" + desc + "] to [" + typeDesc + "]");
        return desc.toLowerCase().contains(typeDesc);
    }
   
}