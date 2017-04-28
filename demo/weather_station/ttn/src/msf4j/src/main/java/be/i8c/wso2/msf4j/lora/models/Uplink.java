package be.i8c.wso2.msf4j.lora.models;

import org.thethingsnetwork.data.common.Metadata;
import org.thethingsnetwork.data.common.messages.DataMessage;
import org.thethingsnetwork.data.common.messages.UplinkMessage;

import java.util.Arrays;
import java.util.Base64;
import java.util.Map;

/**
 * Created by yanglin on 28/04/17.
 */
public class Uplink implements DataMessage {

    private String app_id;

    private String dev_id;

    private String hardware_serial;

    private boolean is_Retry;
    private int port;
    private int counter;
    private String payload_raw;
    private byte[] payload;
    private Map<String, Object> payload_fields;
    private Metadata metadata;

    public Uplink(UplinkMessage uplinkMessage) {
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
        return "Uplink{" +
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
