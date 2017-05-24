package be.i8c.wso2.msf4j.lora.services.ttn;

import be.i8c.wso2.msf4j.lora.models.common.Device;
import be.i8c.wso2.msf4j.lora.models.common.SensorRecord;
import be.i8c.wso2.msf4j.lora.models.ttn.TTNUplink;
import be.i8c.wso2.msf4j.lora.services.common.AbstractLoRaService;
import be.i8c.wso2.msf4j.lora.services.common.exceptions.UnknownDeviceException;
import org.apache.logging.log4j.Level;

import java.util.List;

/**
 * Created by yanglin on 24/05/17.
 */
public abstract class AbstractTTNService extends AbstractLoRaService {

    private TTNUplink previousData;

    @Override
    public void save(TTNUplink TTNUplinkMessage) throws RuntimeException {
        if (!this.isDuplicatedData(TTNUplinkMessage))
        {
            log(String.format("uplinkmessage counter %s received.(device: %s)", TTNUplinkMessage.getCounter(), TTNUplinkMessage.getDevId()), Level.INFO);
            Device receivedDevice = super.devices.get(TTNUplinkMessage.getDevId());
            if (receivedDevice == null)
                throw new UnknownDeviceException(TTNUplinkMessage.getDevId());
            log("converting new uplinkmessage", Level.DEBUG);
            List<SensorRecord> records = super.decoder.decodePayload(TTNUplinkMessage,devices.get(TTNUplinkMessage.getDevId()));
            log("uplinkmessage converted.",Level.DEBUG);
            records.forEach(r -> log(r.simpleString(), Level.DEBUG));
            log(String.format("start validating %d records", records.size()),Level.DEBUG);
            records = super.dataValidator.validateAll(records);
            log("checking notification",Level.DEBUG);
            super.dataValidator.checkForNotification(records,devices.get(TTNUplinkMessage.getDevId()), this::sendDownlink);
            if (records != null)
            {
                this.saveToRepo(records);
                log(String.format("record with counter %d saved", TTNUplinkMessage.getCounter()),Level.INFO);
            }
            else
                log(String.format("all records are invalid. ignore uplinkmessage counter %d", TTNUplinkMessage.getCounter()),Level.WARN);
        }
        else
            log(String.format("duplicated data with counter %d received, ignore.", TTNUplinkMessage.getCounter()),Level.INFO);
    }

    /**
     * This method is used to check whether incoming uplinkMessage is duplicate compared to the previous received uplinkMessage or not.
     * It checks on the timestamps of 2 uplinkmessages
     * @param data incoming uplinkMessage
     * @return true if uplinkMessage is duplicate
     */
    public boolean isDuplicatedData(TTNUplink data)
    {
        /*
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
        */
        if (previousData == null)
        {
            this.previousData = data;
            return false;
        }
        if(data.getMetadata().getTime().equals(this.previousData.getMetadata().getTime()))
            return true;
        else {
            this.previousData = data;
            return false;
        }
    }
}
