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

package be.i8c.wso2.msf4j.lora.services.ttn;

import be.i8c.wso2.msf4j.lora.models.DownlinkRequest;
import be.i8c.wso2.msf4j.lora.services.common.AbstractLoRaService;
import be.i8c.wso2.msf4j.lora.services.common.exceptions.ClientNotRunningException;
import be.i8c.wso2.msf4j.lora.services.common.exceptions.DownlinkException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The implementation of loRaService using HTTP protocol.
 * Created by yanglin on 27/04/17.
 */

@Service
@Profile("http")
public class LoRaHTTPService extends AbstractLoRaService
{
    private static final Logger logger = LogManager.getLogger(LoRaHTTPService.class);

    /**
     * The proces id of TTN HTTP integration, loaded from application.properties
     */
    @Value("${ttn.http.procesId}")
    private String procesId;

    /**
     * the appId, loaded from application.properties
     */
    @Value("${ttn.appId}")
    private String appId;

    /**
     * The accesskey, loaded from application.properties
     */
    @Value("${ttn.accessKey}")
    private String accessKey;

    /**
     * The url used to send downlinkmessage, loaded from application.properties.
     */
    @Value("${ttn.http.url}")
    private String downlinkUrl;

    /**
     * A variable determines if HTTP protocol is running or not.
     */
    private boolean isRunning;

    public LoRaHTTPService()
    {

    }

    /**
     * init method, builds the downlink url and initializes the ObjectMapper.
     */
    @PostConstruct
    private void init()
    {
        isRunning = true;
        downlinkUrl +=
                appId +
                "/" +
                procesId +
                "?key=" +
                accessKey;
        super.objectMapper = new ObjectMapper();
    }


    @Override
    public void startClient() throws Exception {
        isRunning = true;
    }

    @Override
    public void stopClient() throws Exception {
        isRunning = false;
    }

    /**
     * Sends the downlink message through HTTP protocol
     * @param request An object of TTNDownlinkRequest contains deviceid and payload to be sent.
     * @throws RuntimeException when response code are not 202 and when IOException occurred.
     */
    @Override
    public void sendDownlink(DownlinkRequest request) throws DownlinkException {
        if (!isRunning)
            throw new ClientNotRunningException();
        else
        {
            try {
                super.encode(request);
                URL url = new URL(downlinkUrl);
                logger.debug("url is: {}", url.toString());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type","application/json");
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());

                String downlinkAsJson = objectMapper.writeValueAsString(request);
                logger.debug("downlink json: {}",downlinkAsJson);
                wr.writeBytes(downlinkAsJson);
                wr.flush();
                wr.close();

                int responseCode = con.getResponseCode();
                if (!(responseCode == 202))
                    throw new DownlinkException("response from ttn server: " + responseCode);
                logger.debug(responseCode);
                con.disconnect();
            } catch (IOException e ) {
                throw new DownlinkException(e.getMessage());
            }
        }
    }

    @Override
    protected void log(String log, Level level) {
        logger.log(level,log);
    }
}
