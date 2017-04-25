package be.i8c.wso2.msf4j.lora.models;

/**
 * Pre-defined payload.
 * Created by yanglin on 24/04/17.
 */
public enum PreDefinedPayload
{
    TURN_ON_LED("-1"), TURN_OFF_LED("0");

    private String payload;

    PreDefinedPayload(String payload)
    {
        this.payload = payload;
    }

    public String getPayload()
    {
        return payload;
    }
}
