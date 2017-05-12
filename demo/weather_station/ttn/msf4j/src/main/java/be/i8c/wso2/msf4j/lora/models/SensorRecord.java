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

import be.i8c.wso2.msf4j.lora.services.common.utils.DataValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;

/**
 * This is a POJO representing the lora packet
 */

@Entity
public class SensorRecord
{

    private static final Logger logger = LogManager.getLogger(DataValidator.class);
    /**
     * unique id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * id of lora device
     */
    private String deviceId;
    /**
     * owner of lora device
     */
    private String owner;
    /**
     * timestamps
     */
    private long time;
    /**
     * value of sensor
     */
    private double sensorValue;
    /**
     * type of sensor
     */
    @Enumerated(EnumType.STRING)
    private SensorType type;

    /**
     * counter of the payload which the sensor is coming from
     */
    private int counter;
    
    public SensorRecord()
    {
        
    }

    public SensorRecord(String deviceId, String owner, long time, double sensorValue, SensorType type, int counter) {
        this.deviceId = deviceId;
        this.owner = owner;
        this.time = time;
        this.sensorValue = sensorValue;
        this.type = type;
        this.counter = counter;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }
    
    public String getOwner() {
        return owner;
    }

    public Long getTime() {
        return time;
    }

    public double getSensorValue() {
        return sensorValue;
    }

    public SensorType getType() {
        return type;
    }

    public int getCounter() {
        return counter;
    }

    /**
     * validates this sensor.
     * @return same object when it is valid, null when invalid or param is null.
     */
    public boolean isValid()
    {
        logger.debug("validating record: {}", this.simpleString());
        logger.debug("range are {} - {}", type.getMin(), type.getMax());
        if (this.getSensorValue() > type.getMax() || this.getSensorValue() < type.getMin()) {
            logger.warn("record: {} is invalid. It will be filter out.", this.simpleString());
            return false;
        } else
            logger.debug("record: {} is valid.", this.simpleString());
        return true;
    }

    public String simpleString()
    {
        return "Record{Id: " + this.id + ", Type: " + type + ", value: " + sensorValue + "}";
    }

    @Override
    public String toString() {
        return this.simpleString();
    }

    public String getLongString()
    {
        return "Record{ id=" + id + ", deviceId=" + deviceId + ", owner=" + owner + ", time=" + time + ", sensorValue=" + sensorValue + ", type=" + type + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SensorRecord record = (SensorRecord) o;

        if (time != record.time)
            return false;
        if (Double.compare(record.sensorValue, sensorValue) != 0)
            return false;
        if (id != null ? !id.equals(record.id) : record.id != null)
            return false;
        return type == record.type;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (time ^ (time >>> 32));
        temp = Double.doubleToLongBits(sensorValue);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}