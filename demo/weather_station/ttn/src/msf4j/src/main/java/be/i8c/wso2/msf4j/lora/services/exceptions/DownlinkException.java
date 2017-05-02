package be.i8c.wso2.msf4j.lora.services.exceptions;

/**
 * Created by yanglin on 20/04/17.
 */
public class DownlinkException extends RuntimeException
{
    public DownlinkException()
    {
        super("sending downlink fails.");
    }
    public DownlinkException(String s)
    {
        super("sending downlink fails: " + s);
    }
}
