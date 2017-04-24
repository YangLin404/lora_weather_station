package be.i8c.wso2.msf4j.lora.models;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a loRa device.
 * It's holds a map of notifications which already sent out, it is used to prevent duplicated notifications.
 * Created by yanglin on 21/04/17.
 */
public class Device
{
    /**
     * The unique id of device.
     */
    private String deviceId;

    /**
     * A map of notifications which already sent out.
     */
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
