package be.i8c.wso2.msf4j.lora.models;

/**
 * Created by yanglin on 24/04/17.
 */
public enum PreDefinedDownlink
{
    TURN_ON_LED("-1"), TURN_OFF_LED("0");

    private String payload;

    PreDefinedDownlink(String payload)
    {
        this.payload = payload;
    }

    public String getPayload()
    {
        return payload;
    }
}
