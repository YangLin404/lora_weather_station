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

package be.i8c.wso2.msf4j.lora.utils;

import be.i8c.wso2.msf4j.lora.models.SensorType;
import org.springframework.stereotype.Component;
import org.thethingsnetwork.data.common.messages.UplinkMessage;

import java.util.Arrays;
import java.util.List;

/**
 * This class is used to validate the incoming uplinkMessages.
 * Created by yanglin on 12/04/17.
 */
@Component
public class UplinkMessageValidator
{
    private UplinkMessage previousData;

    public UplinkMessageValidator()
    {

    }

    /**
     * This method is used to check whether incoming uplinkMessage is duplicate compared to the previous received uplinkMessage or not.
     * @param data incoming uplinkMessage
     * @return true if uplinkMessage is duplicate
     */
    public boolean isDuplicatedData(UplinkMessage data)
    {
        if (previousData == null) {
            this.previousData = data;
            return false;
        }
        else if (!(data.getDevId().equals(data.getDevId())))
            return false;
        else if (previousData.getCounter() == data.getCounter())
            return true;
        else
        {
            String timePre = previousData.getMetadata().getTime();
            String timeCur = data.getMetadata().getTime();
            if (timeCur.equals(timePre))
            {
                byte[] payloadPre = previousData.getPayloadRaw();
                byte[] payloadCur = data.getPayloadRaw();
                if (Arrays.equals(payloadCur, payloadPre))
                    return true;
            }
            previousData = data;
            return false;
        }
    }

    /**
     * This method is used to check whether the raw payload is valid or not.
     * @param payloadHex The list of hex string converted from raw payload.
     * @param sensorsToDecode The list of sensorTypes to be expected.
     * @return true if payload is valid, false if invalid.
     */
    public boolean isRawPayloadValid(List<String> payloadHex, List<SensorType> sensorsToDecode)
    {
        return payloadHex.size() >= (sensorsToDecode.size() * 2);
    }
}
