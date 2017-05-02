package be.i8c.wso2.msf4j.lora.services;

import be.i8c.wso2.msf4j.lora.models.Device;
import be.i8c.wso2.msf4j.lora.models.DownlinkRequest;
import be.i8c.wso2.msf4j.lora.models.SensorRecord;
import be.i8c.wso2.msf4j.lora.models.Uplink;
import be.i8c.wso2.msf4j.lora.repositories.LoRaRepository;
import be.i8c.wso2.msf4j.lora.services.exceptions.SaveToRepositoryException;
import be.i8c.wso2.msf4j.lora.services.exceptions.UnknownDeviceException;
import be.i8c.wso2.msf4j.lora.services.utils.DataValidator;
import be.i8c.wso2.msf4j.lora.services.utils.PayloadDecoder;
import be.i8c.wso2.msf4j.lora.services.utils.PayloadEncoder;
import be.i8c.wso2.msf4j.lora.services.utils.UplinkMessageValidator;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
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
    private Map<String,Device> devices;

    /**
     * An instance of PayloadDecoder class, used to decode the payload of uplinkMessage into SensorRecord.
     */
    @Autowired
    private PayloadDecoder decoder;

    /**
     * An instance of PayloadEncoder class, used to encode the payload into bytes for downlink message.
     */
    @Autowired
    private PayloadEncoder encoder;

    /**
     * An instance of DataValidator class, used to validate the integrity of data to be inserted.
     */
    @Autowired
    private DataValidator dataValidator;
    /**
     * An instance of UplinkMessageValidator class, used to validate the uplinkMessage.
     */
    @Autowired
    private UplinkMessageValidator uplinkMessageValidator;

    public AbstractLoRaService()
    {

    }

    public AbstractLoRaService(Map<String,Device> devices)
    {
        this.devices = devices;
    }

    public void save(Uplink uplinkMessage) throws RuntimeException
    {
        if (!uplinkMessageValidator.isDuplicatedData(uplinkMessage))
        {
            try {
                log(String.format("uplinkmessage counter %s received.(device: %s)", uplinkMessage.getCounter(),uplinkMessage.getDevId()),Level.INFO);
                Device receivedDevice = devices.get(uplinkMessage.getDevId());
                if (receivedDevice == null)
                    throw new UnknownDeviceException(uplinkMessage.getDevId());
                log("converting new uplinkmessage", Level.DEBUG);
                List<SensorRecord> records = decoder.decodePayload(uplinkMessage,devices.get(uplinkMessage.getDevId()));
                log("uplinkmessage converted.",Level.DEBUG);
                records.forEach(r -> log(r.simpleString(), Level.DEBUG));
                log(String.format("start validating %d records", records.size()),Level.DEBUG);
                records = dataValidator.validateAll(records);
                log("checking notification",Level.DEBUG);
                dataValidator.checkForNotification(records,devices.get(uplinkMessage.getDevId()), this::sendDownlink);
                if (records != null)
                {
                    log("saving records into database.",Level.INFO);
                    this.saveToRepo(records);
                    log(String.format("record with counter %d saved", uplinkMessage.getCounter()),Level.INFO);


                }
                else
                    log(String.format("all records are invalid. ignore uplinkmessage counter %d", uplinkMessage.getCounter()),Level.WARN);
            }catch (RuntimeException e)
            {
                log(e.getMessage(),Level.ERROR);
                log(Arrays.toString(e.getStackTrace()),Level.DEBUG);
            }
        }
        else
            log(String.format("duplicated data with counter %d received, ignore.", uplinkMessage.getCounter()),Level.INFO);
    }

    private void saveToRepo(List<SensorRecord> records)  throws SaveToRepositoryException
    {
        List savedRecords = repo.save(records);
        if (savedRecords != null) {
            log("saved data: \n" + savedRecords.toString(),Level.DEBUG);
        }
        else {
            throw new SaveToRepositoryException();
        }
    }

    protected void encode(DownlinkRequest downlinkRequest)
    {
        downlinkRequest.setPayload_raw(encoder.encode(downlinkRequest.getPayloadString()));
    }

    abstract public void startClient() throws Exception;
    abstract public void stopClient() throws Exception;
    abstract public void sendDownlink(DownlinkRequest request);

    abstract protected void log(String log, Level level);
}
