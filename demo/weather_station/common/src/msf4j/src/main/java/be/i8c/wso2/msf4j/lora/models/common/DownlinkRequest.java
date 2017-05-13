package be.i8c.wso2.msf4j.lora.models.common;

/**
 * Created by yanglin on 12/05/17.
 */
public interface DownlinkRequest {
    String getDeviceId();
    int getPort();
    String getPayload_raw();
    void setPayload_raw(String payload_raw);
    void setDeviceId(String deviceId);
    String getPayloadString();
}
