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

package be.i8c.wso2.msf4j.lora.services.common.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 * This class is used to encode the payload into byte format for downlink message.
 *
 * Created by yanglin on 19/04/17.
 */
@Component
public class PayloadEncoder
{
    private static final Logger logger = LogManager.getLogger(PayloadEncoder.class);

    public PayloadEncoder()
    {

    }

    /**
     * encode String format payload into byte format.
     * @param payloadString payload in String format.
     * @return array of byte
     * @throws IllegalArgumentException when payload string is null or empty.
     */
    public String encode(String payloadString)
    {
        if (payloadString == null || payloadString.isEmpty())
            throw new IllegalArgumentException("payload string cannot be empty");
        logger.debug("encoding: {}", payloadString);
        List<String> payloadstringList = Arrays.asList(payloadString.split(" "));
        logger.debug("splited string are: {}", payloadstringList.toString());
        byte[] payload = new byte[payloadstringList.size()];
        payloadstringList.forEach(e -> Arrays.fill(payload, Byte.parseByte(e,16)));
        logger.debug("encoded bytes are: {}", Arrays.toString(payload));
        String base64 = Base64.getEncoder().encodeToString(payload);
        logger.debug("encoded base64 is: {}", base64);
        return base64;
    }
}
