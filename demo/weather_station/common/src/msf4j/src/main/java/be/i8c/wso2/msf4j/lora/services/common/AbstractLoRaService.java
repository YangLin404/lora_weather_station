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

package be.i8c.wso2.msf4j.lora.services.common;

import be.i8c.wso2.msf4j.lora.models.common.Device;
import be.i8c.wso2.msf4j.lora.models.common.DownlinkRequest;
import be.i8c.wso2.msf4j.lora.models.common.SensorRecord;
import be.i8c.wso2.msf4j.lora.models.ttn.TTNUplink;
import be.i8c.wso2.msf4j.lora.repositories.LoRaRepository;
import be.i8c.wso2.msf4j.lora.services.common.exceptions.DownlinkException;
import be.i8c.wso2.msf4j.lora.services.common.exceptions.SaveToRepositoryException;
import be.i8c.wso2.msf4j.lora.services.common.utils.DataValidator;
import be.i8c.wso2.msf4j.lora.services.common.utils.PayloadDecoder;
import be.i8c.wso2.msf4j.lora.services.common.utils.PayloadEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * An abstract loraService class which can be implemented for Proximus or The thingsnetwork depends on the usage.
 * LoRaService is used to handle the uplinkmessages, send the downlink messages and manages the client which is used in communication with TTN back-end.
 * Created by yanglin on 27/04/17.
 */
public abstract class AbstractLoRaService
{

    /**
     * Repository class used to communicate with database.
     */
    @Autowired
    private LoRaRepository repo;


    /**
     * a list of predefined devices.
     */
    @Autowired
    protected Map<String,Device> devices;

    /**
     * An instance of PayloadDecoder class, used to decode the payload of uplinkMessage into SensorRecord.
     */
    @Autowired
    protected PayloadDecoder decoder;

    /**
     * An instance of PayloadEncoder class, used to encode the payload into bytes for downlink message.
     */
    @Autowired
    protected PayloadEncoder encoder;

    /**
     * An instance of DataValidator class, used to validate the integrity of data to be inserted.
     */
    @Autowired
    protected DataValidator dataValidator;

    /**
     * Jackson ObjectMapper, used to serialize object into Json.
     */
    protected ObjectMapper objectMapper;

    public AbstractLoRaService()
    {

    }

    /**
     * Constroctor with predefined Devices in application.properties.
     * @param devices predefined Devices in application.properties
     */
    public AbstractLoRaService(Map<String,Device> devices)
    {
        this.devices = devices;
    }

    /**
     * This method is used to handle the TTNUplinkMessage coming from TTN. First, the raw payload will be convert into a list of Records.
     * After validating and checking for notification, the valid list of sensor records will be passed through to Repository class for persistence.
     * @param TTNUplinkMessage the TTNUplinkMessage coming from TTN.
     */
    public abstract void save(TTNUplink TTNUplinkMessage) throws RuntimeException;

    public abstract void save(String s) throws RuntimeException;

    /**
     * this method passes a list of valid sensor records to the repository class for serialization.
     * @param records a list of valid sensor records.
     * @throws SaveToRepositoryException when parameter records are null.
     */
    protected void saveToRepo(List<SensorRecord> records)  throws SaveToRepositoryException
    {
        List savedRecords = repo.save(records);
        if (savedRecords != null) {
            log("saved data: \n" + savedRecords.toString(),Level.DEBUG);
        }
        else {
            throw new SaveToRepositoryException();
        }
    }
    /**
     * this method passes the valid sensor records to the repository class for serialization.
     * @param record a list of valid sensor records.
     * @throws SaveToRepositoryException when parameter records are null.
     */
    protected void saveToRepo(SensorRecord record)  throws SaveToRepositoryException
    {
        SensorRecord savedRecord = repo.save(record);
        if (savedRecord != null) {
            log("saved data: \n" + savedRecord.toString(),Level.DEBUG);
        }
        else {
            throw new SaveToRepositoryException();
        }
    }

    /**
     * This method encodes the human readable downlink payload into an array of bytes.
     * @param downlinkRequest The downlink message to be sent.
     */
    protected void encode(DownlinkRequest downlinkRequest)
    {
        downlinkRequest.setPayload_raw(encoder.encode(downlinkRequest.getPayloadString()));
    }

    /**
     * Let service start listening on uplinkmessages and allow to send downlink messages.
     * @throws Exception
     */
    abstract public void startClient() throws Exception;

    /**
     * Let service stop listening on uplinkmessages and disallow to send downlink messages.
     * @throws Exception
     */
    abstract public void stopClient() throws Exception;

    /**
     * Send the downlink message.
     * @param request The DownlinkRequest, either ProximusDownlinkRequest or TTNDownlinkRequest
     */
    abstract public void sendDownlink(DownlinkRequest request) throws DownlinkException;

    abstract protected void log(String log, Level level);
}
