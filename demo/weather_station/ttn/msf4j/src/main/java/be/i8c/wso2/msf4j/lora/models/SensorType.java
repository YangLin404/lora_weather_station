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
    Temperature(-50,100,10.0d), Light(0,150, 10.0d),
    Pressure(900,1100, 10.0d), Humidity(0,100, 10.0d),
    AirQuality(0,1000,10.0d), BatteryLevel(0,101,1000.0d);

    /**
     * This is the description of specified type in json object of proximus
     */
    private final double min;
    private final double max;
    private final double factor;
    
    SensorType(double min, double max, double factor)
    {
        this.min = min;
        this.max = max;
        this.factor = factor;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getFactor() {return factor;}
}
