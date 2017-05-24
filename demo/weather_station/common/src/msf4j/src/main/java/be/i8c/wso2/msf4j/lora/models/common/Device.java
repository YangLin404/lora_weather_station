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

package be.i8c.wso2.msf4j.lora.models.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a loRa device.
 * It's holds a map of notifications which already sent out, this map is used to prevent duplicated notifications.
 * Created by yanglin on 21/04/17.
 */
public class Device
{
    /**
     * The unique id of device.
     */
    private final String deviceId;

    private final List<SensorType> payloadFormat;

    /**
     * A map of notifications which already sent out.
     */
    private Map<NotificationType,Boolean> notifiedMap;

    /**
     * Constructor used by spring DI.
     * It initializes the notifiedMap and fills it with default values.
     * @param deviceId the deviceId of the device
     * @param payloadFormat the payload format of the device
     */
    public Device(String deviceId, List<SensorType> payloadFormat)
    {
        this.deviceId = deviceId;
        this.payloadFormat = payloadFormat;
        notifiedMap = new HashMap<>();
        Arrays.stream(NotificationType.values()).forEach(n -> this.notifiedMap.put(n,false));
    }

    public Map<NotificationType, Boolean> getNotifiedMaps()
    {
        return this.notifiedMap;
    }

    public String getDeviceId()
    {
        return deviceId;
    }

    public List<SensorType> getPayloadFormat() {
        return payloadFormat;
    }
}
