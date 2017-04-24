package be.i8c.wso2.msf4j.lora.models;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yanglin on 21/04/17.
 */
public class Device
{
    private String deviceId;
    private Map<NotificationType,Boolean> notifiedMap;

    public Device(String deviceId)
    {
        this.deviceId = deviceId;
        notifiedMap = new HashMap<>();
        Arrays.stream(NotificationType.values()).forEach(n -> this.notifiedMap.put(n,false));
    }

    public Map<NotificationType, Boolean> getNotifiedMaps()
    {
        return this.notifiedMap;
    }

    public String getDeviceId()
    {
        return deviceId;
    }
}
