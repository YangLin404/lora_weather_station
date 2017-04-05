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
package be.i8c.wso2.msf4j.lora.models;

/**
 * This is a enum which represents all types of sensor.
 *
 * @author yanglin
 */
public enum SensorType {
    Binary("binary","binary_value"), Loudness("loudness","loudness_value"), Temperature("temperature","temperature_sensor_value"), Light("light","light_value"), 
    Accelerometer("accelerometer","accelerometer_value"), Pressure("pressure","pressure_value"), Humidity("humidity","humidity_value"),
    AirQuality("air quality","air_quality_value"), BatteryLevel("BatteryLevel","batterylevel_value"), Integer("integer","integer_sensor");

    /**
     * This is the description of specified type in json object of proximus
     */
    private String desc;
    private String valueString;
    
    SensorType(String s, String v)
    {
        this.desc = s;
        this.valueString = v;
    }
    
    public String getDesc()
    {
        return this.desc;
    }
    
    public String getValueString()
    {
        return this.valueString;
    }
}
