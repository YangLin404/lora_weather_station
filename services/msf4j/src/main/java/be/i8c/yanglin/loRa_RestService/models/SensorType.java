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
package be.i8c.yanglin.loRa_RestService.models;

/**
 *
 * @author yanglin
 */
public enum SensorType {
    Binary("Binary sensor","binary_value"), Loudness("Loudness sensor","loudness_value"), Temperature("Temperature sensor","temperature_value"), Light("Light sensor","light_value"), 
    Accelerometer("Accelerometer sensor","accelerometer_value"), Pressure("Pressure sensor","pressure_value"), Humility("Humility sensor","humility_value"), 
    AirQuality("AirQuality sensor","airquality_value"), BatteryLevel("BatteryLevel sensor","batterylevel_value"), Integer("Integer sensor","integer_sensor");
    
    private String desc;
    private String valueString;
    
    SensorType(String s, String v)
    {
        this.desc = s;
        this.valueString = v;
    }
    
    public boolean compDesc(String s)
    {
        return s.equals(this.desc);
    }
    
    public String getValueString()
    {
        return this.valueString;
    }
}
