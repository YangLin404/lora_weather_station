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

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * This is a POJO representing lora packet
 */

@XmlRootElement(name="Record")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class SensorRecord
{
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * id of lora device
     */
    @XmlTransient
    private String deviceId;
    /**
     * owner of lora device
     */
    @XmlTransient
    private String owner;
    /**
     * timestamps
     */
    @XmlElement
    private long streamValueTime;
    /**
     * value of sensor
     */
    @XmlElement
    private double sensorValue;
    /**
     * type of sensor
     */
    @XmlElement
    @Enumerated(EnumType.STRING)
    private SensorType type;
    
    public SensorRecord()
    {
        
    }
    /*
    public SensorRecord(String deviceId, String owner, long streamValueTime, SensorType type) {
        this.deviceId = deviceId;
        this.owner = owner;
        this.streamValueTime = streamValueTime;
        this.type = type;
    }
    */
    public SensorRecord(String deviceId, String owner, long streamValueTime, double sensorValue, SensorType type) {
        this.deviceId = deviceId;
        this.owner = owner;
        this.streamValueTime = streamValueTime;
        this.sensorValue = sensorValue;
        this.type = type;
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
    
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    
    public String getOwner() {
        return owner;
    }
    
    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Long getStreamValueTime() {
        return streamValueTime;
    }
    
    public void setStreamValueTime(Long streamValueTime) {
        this.streamValueTime = streamValueTime;
    }

    public double getSensorValue() {
        return sensorValue;
    }
    
    public void setSensorValue(double value) {
        this.sensorValue = value;
    }

    public SensorType getType() {
        return type;
    }
    
    public void setType(SensorType type) {
        this.type = type;
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
        return "Record{ id=" + id + ", deviceId=" + deviceId + ", owner=" + owner + ", streamValueTime=" + streamValueTime + ", sensorValue=" + sensorValue + ", type=" + type + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SensorRecord record = (SensorRecord) o;

        if (streamValueTime != record.streamValueTime)
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
        result = 31 * result + (int) (streamValueTime ^ (streamValueTime >>> 32));
        temp = Double.doubleToLongBits(sensorValue);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}