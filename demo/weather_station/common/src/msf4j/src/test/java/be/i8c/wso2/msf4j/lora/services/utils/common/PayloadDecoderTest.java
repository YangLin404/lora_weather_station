package be.i8c.wso2.msf4j.lora.services.utils.common;

import be.i8c.wso2.msf4j.lora.models.common.Device;
import be.i8c.wso2.msf4j.lora.models.common.SensorBuilder;
import be.i8c.wso2.msf4j.lora.models.common.SensorRecord;
import be.i8c.wso2.msf4j.lora.models.common.SensorType;
import be.i8c.wso2.msf4j.lora.models.ttn.TTNUplink;
import be.i8c.wso2.msf4j.lora.services.common.utils.PayloadDecoder;
import be.i8c.wso2.msf4j.lora.services.common.utils.exceptions.PayloadFormatException;
import be.i8c.wso2.msf4j.lora.services.common.utils.exceptions.PayloadFormatNotDefinedException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.thethingsnetwork.data.common.Metadata;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.when;


/**
 * Created by yanglin on 14/04/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class PayloadDecoderTest
{
    private final String PAYLOAD_STRING_FORMAT="Temperature,Humidity,Pressure,Light";

    private static List<SensorType> payloadFormat;

    private static List<SensorRecord> LIST_OF_SENSOR_A = new LinkedList<>();

    private byte[] RAWPAYLOAD_A = {1, 6, 1, 85, 39, -70, 3, -44};

    private static SensorBuilder sensorBuilder;

    private static Device device;
    @Mock
    private TTNUplink TTNUplinkMessage;
    @Mock
    private Metadata metadata;

    @InjectMocks
    private PayloadDecoder payloadDecoder;

    @BeforeClass
    public static void setupClass()
    {
        sensorBuilder = new SensorBuilder();

        LIST_OF_SENSOR_A.add(sensorBuilder.setType(SensorType.Temperature).setValue(26.2).setTimestamp(1).build());
        LIST_OF_SENSOR_A.add(sensorBuilder.setType(SensorType.Humidity).setValue(34.1).setTimestamp(1).build());
        LIST_OF_SENSOR_A.add(sensorBuilder.setType(SensorType.Pressure).setValue(1017.0).setTimestamp(1).build());
        LIST_OF_SENSOR_A.add(sensorBuilder.setType(SensorType.Light).setValue(98.0).setTimestamp(1).build());

        payloadFormat = new LinkedList<>();
        payloadFormat.add(SensorType.Temperature);
        payloadFormat.add(SensorType.Humidity);
        payloadFormat.add(SensorType.Pressure);
        payloadFormat.add(SensorType.Light);

        device = new Device("test",payloadFormat);

    }

    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void decodePayload_RawPayloadIsValid_ShouldReturnListOfSensorRecord() throws PayloadFormatException, PayloadFormatNotDefinedException {
        List<SensorRecord> excepted = LIST_OF_SENSOR_A;

        when(TTNUplinkMessage.getPayloadRaw()).thenReturn(RAWPAYLOAD_A);
        when(TTNUplinkMessage.getMetadata()).thenReturn(metadata);
        when(metadata.getTime()).thenReturn(Instant.EPOCH.plusMillis(1).toString());

        List<SensorRecord> actual = payloadDecoder.decodePayload(TTNUplinkMessage,device);

        assertEquals(excepted,actual);
    }


}