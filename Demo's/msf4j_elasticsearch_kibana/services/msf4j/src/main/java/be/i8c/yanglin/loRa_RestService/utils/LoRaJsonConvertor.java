/*
 * Copyright 2017 WSO2.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.i8c.yanglin.loRa_RestService.utils;

import be.i8c.yanglin.loRa_RestService.models.Record;
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
    
    private static final Logger logger = LogManager.getLogger(LoRaJsonConvertor.class);
    private Gson gson;
    
    private LoRaJsonConvertor()
    {
        gson = new Gson();
        
    }
    
    public static LoRaJsonConvertor getInstance()
    {
        return instance;
    }
    
    public Record convert(String s)
    {
        gson = new Gson();
        Record r = gson.fromJson(s, Record.class);
        JsonObject jo = new JsonParser().parse(s).getAsJsonObject();
        SensorType sensorType = getTypeFromJSON(jo);
        r.setType(sensorType);
        r.setValue(getValueFromJSON(jo, sensorType));
                
        return r;
    }
    
    public<T> String convertToJsonString(T t)
    {
        return this.gson.toJson(t, t.getClass());
    }
    
    public Double getValueFromJSON(JsonObject j, SensorType s)
    {
        return Double.parseDouble(j.get(s.getValueString()).getAsString());
    }
    
    private SensorType getTypeFromJSON(JsonObject j)
    {
        
        String desc = j.get("streamDescription").getAsString();
        
        return Stream.of(SensorType.values())
                .filter(s -> s.compDesc(desc))
                .findFirst()
                .get();        
    }
   
}