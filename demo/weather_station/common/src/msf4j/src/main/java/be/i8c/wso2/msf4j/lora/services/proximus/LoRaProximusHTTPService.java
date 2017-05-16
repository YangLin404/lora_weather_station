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

package be.i8c.wso2.msf4j.lora.services.proximus;

import be.i8c.wso2.msf4j.lora.models.common.Device;
import be.i8c.wso2.msf4j.lora.models.common.DownlinkRequest;
import be.i8c.wso2.msf4j.lora.models.common.SensorRecord;
import be.i8c.wso2.msf4j.lora.services.common.AbstractLoRaService;
import be.i8c.wso2.msf4j.lora.services.common.exceptions.ClientNotRunningException;
import be.i8c.wso2.msf4j.lora.services.common.exceptions.DownlinkException;
import be.i8c.wso2.msf4j.lora.services.common.exceptions.UnknownDeviceException;
import be.i8c.wso2.msf4j.lora.services.proximus.utils.ProximusJsonConvertor;
import be.i8c.wso2.msf4j.lora.services.proximus.utils.ProximusAuthenticator;
import com.google.api.client.auth.oauth2.TokenResponseException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * LoRaService implementation for Proximus.
 */

@Service
@Profile("proximus")
public class LoRaProximusHTTPService extends AbstractLoRaService {

    private static final Logger logger = LogManager.getLogger(LoRaProximusHTTPService.class);

    /**
     * an instance of JsonConvertor, used to convert proximus uplink message into class SensorRecord
     */
    @Autowired
    private ProximusJsonConvertor jsonConvertor;

    /**
     * an instance of ProximusAuthenticator, used to retrieve the access token.
     */
    @Autowired
    private ProximusAuthenticator authenticator;

    /**
     * the url to make a HTTP call to send the downlink message
     */
    @Value("${proximus.downlinkUrl}")
    private String downlinkUrl;

    /**
     * A variable determines if http client is running or not.
     */
    private boolean isRunning;

    @PostConstruct
    private void init()
    {
        isRunning = true;
        this.downlinkUrl += "/devices";
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
     * this method pass through the proximus uplinkmessage to repository class for persistence.
     * @param jsonString the json object received by proximus
     * @throws RuntimeException when error occurs which persistence or the device of uplinkmessage cannot be found.
     */
    @Override
    public void save(String jsonString) throws RuntimeException
    {
        SensorRecord sensorRecord = jsonConvertor.convertFromProximus(jsonString);
        Device receivedDevice = super.devices.get(sensorRecord.getDeviceId());
        if (receivedDevice == null)
            throw new UnknownDeviceException(sensorRecord.getDeviceId());
        if (super.dataValidator.validate(sensorRecord) == null)
            logger.warn("record is invalid. ignore proximus uplink");
        else {
            super.dataValidator.checkForNotification(sensorRecord, receivedDevice, this::sendDownlink);
            super.saveToRepo(sensorRecord);
            logger.info("proximus uplink data saved");

        }
    }

    /**
     * sends downlink message to device through proximus
     * @param request The DownlinkRequest, either ProximusDownlinkRequest or TTNDownlinkRequest
     * @throws DownlinkException
     */
    @Override
    public void sendDownlink(DownlinkRequest request) throws DownlinkException {
        if (!isRunning)
            throw new ClientNotRunningException();
        else
        {
            try {
                String accessToken = authenticator.getAccessToken();
                super.encode(request);
                URL url = new URL(this.buildDownlinkUrl(request));
                logger.debug("url is: {}", url.toString());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type","application/json");
                con.setRequestProperty("Authorization", "Bearer "+accessToken);
                con.setRequestProperty("Accept", "application/json");
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());

                String downlinkAsJson = super.objectMapper.writeValueAsString(request);
                logger.debug("downlink json: {}",downlinkAsJson);
                wr.writeBytes(downlinkAsJson);
                wr.flush();
                wr.close();

                int responseCode = con.getResponseCode();
                if (!(responseCode == 202))
                    throw new DownlinkException("response from ttn server: " + responseCode);
                logger.debug(responseCode);
                con.disconnect();
            }catch (TokenResponseException e) {
                throw new DownlinkException();
            } catch (IOException e ) {
                throw new DownlinkException(e.getMessage());
            }
        }
    }

    /**
     * the method builds the downlink url for sending downlink message through proximus
     * @param request downlink message to be sent.
     * @return downlink url
     */
    private String buildDownlinkUrl(DownlinkRequest request)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.downlinkUrl)
                .append("/")
                .append(request.getDeviceId())
                .append("/")
                .append("/downlink")
                .append("/lora");
        return stringBuilder.toString();
    }

    @Override
    protected void log(String log, Level level) {
        logger.log(level,log);
    }
}
