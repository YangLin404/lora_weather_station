package be.i8c.wso2.msf4j.lora.models.proximus;

import be.i8c.wso2.msf4j.lora.models.common.DownlinkRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This is a implementation of interface DownlinkRequest for sending downlink message through Proximus.
 */
public class ProximusDownlinkRequest implements DownlinkRequest {


    @JsonIgnore
    private String payloadString;

    private String deviceId;

    /**
     * port number
     */
    private int port;

    /**
     * the Base64 encoded payload
     */
    private String binaryMessage;


    public ProximusDownlinkRequest()
    {

    }

    public ProximusDownlinkRequest(String deviceId, String payloadString)
    {
        this.deviceId = deviceId;
        this.payloadString = payloadString;
        this.port = 1;

    }

    @Override
    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getPayload_raw() {
        return binaryMessage;
    }

    @Override
    public void setPayload_raw(String payload_raw) {
        this.binaryMessage = payload_raw;
    }

    @Override
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String getPayloadString() {
        return payloadString;
    }
}
