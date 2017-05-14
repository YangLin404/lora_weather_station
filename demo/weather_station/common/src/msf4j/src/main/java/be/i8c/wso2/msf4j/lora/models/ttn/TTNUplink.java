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

package be.i8c.wso2.msf4j.lora.models.ttn;

import org.thethingsnetwork.data.common.Metadata;
import org.thethingsnetwork.data.common.messages.DataMessage;
import org.thethingsnetwork.data.common.messages.UplinkMessage;

import java.util.Arrays;
import java.util.Base64;
import java.util.Map;

/**
 * A wrapper class of UplinkMessage class of TTN JAVA SDK.
 * Created by yanglin on 28/04/17.
 */
public class TTNUplink implements DataMessage {

    private final String app_id;

    private final String dev_id;

    private final String hardware_serial;

    private final boolean is_Retry;
    private final int port;
    private final int counter;
    private String payload_raw;
    private final byte[] payload;
    private Map<String, Object> payload_fields;
    private final Metadata metadata;

    public TTNUplink(UplinkMessage uplinkMessage) {
        this.app_id = uplinkMessage.getAppId();
        this.dev_id = uplinkMessage.getDevId();
        this.hardware_serial = uplinkMessage.getHardwareSerial();
        this.is_Retry = uplinkMessage.isRetry();
        this.port = uplinkMessage.getPort();
        this.counter = uplinkMessage.getCounter();
        this.payload = uplinkMessage.getPayloadRaw();
        this.metadata = uplinkMessage.getMetadata();

    }

    public String getAppId() {
        return app_id;
    }

    public String getDevId() {
        return dev_id;
    }

    public String getHardwareSerial() {
        return hardware_serial;
    }

    public boolean isRetry() {
        return is_Retry;
    }

    public int getPort() {
        return port;
    }

    public int getCounter() {
        return counter;
    }

    public byte[] getPayloadRaw() {
        if (payload == null)
            return Base64.getDecoder().decode(payload_raw);
        else
            return payload;
    }

    public Map<String, Object> getPayloadFields() {
        return payload_fields;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    public String toString() {
        return "TTNUplink{" +
                "appId='" + app_id + '\'' +
                ", devId='" + dev_id + '\'' +
                ", hardwareSerial='" + hardware_serial + '\'' +
                ", isRetry=" + is_Retry +
                ", port=" + port +
                ", counter=" + counter +
                ", payloadRaw='" + payload_raw + '\'' +
                ", payload=" + Arrays.toString(payload) +
                ", metadata=" + metadata +
                '}';
    }
}
