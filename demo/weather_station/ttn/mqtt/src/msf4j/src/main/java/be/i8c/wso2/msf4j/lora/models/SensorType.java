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
    Temperature("temperature","temperature_sensor_value",-50,100,10.0d), Light("light","light_value",0,150, 10.0d),
    Pressure("pressure","pressure_value",900,1100, 10.0d), Humidity("humidity","humidity_value",0,100, 10.0d),
    AirQuality("air quality","air_quality_value",0,1000,10.0d), BatteryLevel("BatteryLevel","batterylevel_value",0,100,1000.0d);

    /**
     * This is the description of specified type in json object of proximus
     */
    private String desc;
    private String valueString;
    private double min;
    private double max;
    private double factor;
    
    SensorType(String s, String v, double min, double max, double factor)
    {
        this.desc = s;
        this.valueString = v;
        this.min = min;
        this.max = max;
        this.factor = factor;
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

    public double getFactor() {return factor;}
}
