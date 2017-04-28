package be.i8c.wso2.msf4j.lora.services.exceptions;

/**
 * Created by yanglin on 27/04/17.
 */
public class SaveToRepositoryException extends RuntimeException {
    public SaveToRepositoryException()
    {
        super("saving records into database fails, please check logs of repository class for detailed information");
    }
}
