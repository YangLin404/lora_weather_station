package be.i8c.wso2.msf4j.lora.services.exceptions;

/**
 * Created by yanglin on 25/04/17.
 */
public class UnknownDeviceException extends RuntimeException {
    public UnknownDeviceException(String deviceId)
    {
        super("Device with {} could not be found in config file.");
    }
}
