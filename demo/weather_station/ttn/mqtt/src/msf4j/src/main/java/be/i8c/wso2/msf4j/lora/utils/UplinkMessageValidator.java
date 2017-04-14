package be.i8c.wso2.msf4j.lora.utils;

import be.i8c.wso2.msf4j.lora.models.SensorType;
import org.springframework.stereotype.Component;
import org.thethingsnetwork.data.common.messages.UplinkMessage;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yanglin on 12/04/17.
 */
@Component
public class UplinkMessageValidator
{
    private UplinkMessage previousData;

    public UplinkMessageValidator()
    {

    }

    public boolean isDuplicatedData(UplinkMessage data)
    {
        if (previousData == null) {
            this.previousData = data;
            return false;
        }
        else if (previousData.getCounter() == data.getCounter())
            return true;
        else
        {
            String timePre = previousData.getMetadata().getTime();
            String timeCur = data.getMetadata().getTime();
            if (timeCur.equals(timePre))
            {
                byte[] payloadPre = previousData.getPayloadRaw();
                byte[] payloadCur = data.getPayloadRaw();
                if (Arrays.equals(payloadCur, payloadPre))
                    return true;
            }
            previousData = data;
            return false;
        }
    }

    public boolean isRawPayloadValid(List<String> payloadHex, List<SensorType> sensorsToDecode)
    {
        return payloadHex.size() == (sensorsToDecode.size() * 2);
    }
}
