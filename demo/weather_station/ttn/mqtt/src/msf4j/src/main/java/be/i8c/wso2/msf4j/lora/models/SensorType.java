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
    Binary("binary","binary_value",0,0), Loudness("loudness","loudness_value",0,500), Temperature("temperature","temperature_sensor_value",-50,100),
    Light("light","light_value",0,150), Accelerometer("accelerometer","accelerometer_value",0,0), Pressure("pressure","pressure_value",900,1100),
    Humidity("humidity","humidity_value",0,100), AirQuality("air quality","air_quality_value",0,1000), BatteryLevel("BatteryLevel","batterylevel_value",0,100),
    Integer("integer","integer_sensor",0,0);

    /**
     * This is the description of specified type in json object of proximus
     */
    private String desc;
    private String valueString;
    private double min;
    private double max;
    
    SensorType(String s, String v, double min, double max)
    {
        this.desc = s;
        this.valueString = v;
        this.min = min;
        this.max = max;
    }
    
    public String getDesc()
    {
        return this.desc;
    }
    
    public String getValueString()
    {
        return this.valueString;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }
}
