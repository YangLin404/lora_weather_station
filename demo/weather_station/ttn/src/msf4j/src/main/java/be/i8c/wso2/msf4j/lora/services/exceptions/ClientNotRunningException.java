package be.i8c.wso2.msf4j.lora.services.exceptions;

/**
 * Created by yanglin on 28/04/17.
 */
public class ClientNotRunningException extends RuntimeException
{
    public ClientNotRunningException()
    {
        super("Client is not running, please start client first");
    }
}
