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
import java.util.stream.Stream;

/**
 *
 * @author yanglin
 */
public class JSONConvertor 
{
    private static JSONConvertor instance = new JSONConvertor();
    private Gson gson;
    
    private JSONConvertor()
    {
        gson = new Gson();
        
    }
    
    public static JSONConvertor getInstance()
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
    
    public String getValueFromJSON(JsonObject j, SensorType s)
    {
        return j.get(s.getValueString()).getAsString();
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