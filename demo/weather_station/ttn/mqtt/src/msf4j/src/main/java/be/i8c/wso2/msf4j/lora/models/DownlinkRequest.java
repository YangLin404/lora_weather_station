package be.i8c.wso2.msf4j.lora.models;

/**
 * Created by yanglin on 19/04/17.
 */
public class DownlinkRequest
{
    private String payloadString;
    private String deviceId;

    public DownlinkRequest()
    {

    }

    public String getPayloadString() {
        return payloadString;
    }

    public void setPayloadString(String payloadString) {
        this.payloadString = payloadString;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
