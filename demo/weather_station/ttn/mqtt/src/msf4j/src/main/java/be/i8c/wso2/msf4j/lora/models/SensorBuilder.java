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

import java.time.Instant;

/**
 * Created by yanglin on 7/04/17.
 */
public class SensorBuilder
{
    private String deviceId="unknow";
    private String owner="unknow";
    private long timestamp = Instant.now().toEpochMilli();
    private double value;
    private SensorType type;

    public SensorBuilder()
    {

    }

    public SensorBuilder setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
        return this;
    }

    public SensorBuilder setOwner(String owner)
    {
        this.owner = owner;
        return this;
    }

    public SensorBuilder setTimestamp(long timestamp)
    {
        this.timestamp=timestamp;
        return this;
    }

    public SensorBuilder setValue(double value)
    {
        this.value = value;
        return this;
    }

    public SensorBuilder setType(SensorType type)
    {
        this.type = type;
        return this;
    }

    public SensorRecord build()
    {
        if (type == null)
            throw new IllegalArgumentException("type should not be null");
        return new SensorRecord(this.deviceId,this.owner,this.timestamp,this.value,this.type);
    }

    public void flush()
    {
        deviceId="unknow";
        owner="unknow";
        timestamp = Instant.now().toEpochMilli();
        value=0;
        type = null;
    }

}
