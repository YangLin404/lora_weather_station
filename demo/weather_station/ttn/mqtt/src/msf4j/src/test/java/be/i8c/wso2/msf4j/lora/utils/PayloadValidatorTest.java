package be.i8c.wso2.msf4j.lora.utils;

import be.i8c.wso2.msf4j.lora.models.SensorBuilder;
import be.i8c.wso2.msf4j.lora.models.SensorRecord;
import be.i8c.wso2.msf4j.lora.models.SensorType;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by yanglin on 11/04/17.
 */
public class PayloadValidatorTest
{
    private final SensorType sensorTypeToTest = SensorType.Humidity;

    @InjectMocks
    private PayloadValidator validator;

    private static SensorBuilder sensorBuilder;

    private List<SensorRecord> ValidRecords;

    @BeforeClass
    public static void init()
    {
        sensorBuilder = new SensorBuilder();
    }

    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        sensorBuilder.setType(sensorTypeToTest);
    }

    private void setupValidList()
    {
        ValidRecords = new LinkedList<>();
        for (int i=0; i<5; i++)
            ValidRecords.add(sensorBuilder.setValue(sensorTypeToTest.getMin()+1).build());
    }

    @After
    public void reset()
    {
        sensorBuilder.flush();
    }

    @Test
    public void validate__RecordWithValueInRange_ShouldReturnSameRecord()

    {
        SensorRecord record = sensorBuilder
                .setValue(sensorTypeToTest.getMin()+1)
                .build();
        SensorRecord actual = validator.validate(record);
        assertEquals(record, actual);
    }

    @Test
    public void validate_RecordWithValueLowerThanMin_shouldReturnNull()
    {
        SensorRecord record = sensorBuilder
                .setValue(sensorTypeToTest.getMin()-1)
                .build();
        SensorRecord actual = validator.validate(record);
        assertNull("record should be invalid.",actual);
    }

    @Test
    public void validate_RecordWithValueGreaterThanMax_ShouldReturnNull()
    {
        SensorRecord record = sensorBuilder
                .setValue(sensorTypeToTest.getMax()+1)
                .build();
        SensorRecord actual = validator.validate(record);
        assertNull("record should be invalid.",actual);
    }

    @Test
    public void validate_ParamIsNull_ShouldReturnNull()
    {
        SensorRecord record = null;
        SensorRecord actual = validator.validate(record);
        assertNull(actual);
    }

    @Test
    public void validateAll_ListOfValidRecords_ShouldReturnSameList()
    {
        this.setupValidList();
        List<SensorRecord> actual = validator.validateAll(ValidRecords);
        assertEquals(ValidRecords, actual);
    }

    @Test
    public void validateAll_ListOfRecordsWithSomeInvalidRecord_ShouldReturnListOfValidRecord()
    {
        this.setupValidList();
        List<SensorRecord> invalidList = new LinkedList<>(ValidRecords);
        invalidList.add(sensorBuilder.setValue(sensorTypeToTest.getMax()+1).build());

        List<SensorRecord> expected = ValidRecords;
        List<SensorRecord> actual = validator.validateAll(invalidList);

        assertEquals(expected, actual);
    }

    @Test
    public void validateAll_EmptyList_ShouldReturnNull()
    {
        this.setupValidList();
        List<SensorRecord> emptyList = new LinkedList<>();

        List<SensorRecord> actual = validator.validateAll(emptyList);

        assertNull(actual);
    }

    @Test
    public void validateAll_ListWithOnlyInvalidRecord_ShouldReturnNull()
    {
        List<SensorRecord> invalidRecords = new LinkedList<>();
        invalidRecords.add(sensorBuilder.setValue(sensorTypeToTest.getMax()+1).build());
        invalidRecords.add(sensorBuilder.setValue(sensorTypeToTest.getMax()+2).build());
        invalidRecords.add(sensorBuilder.setValue(sensorTypeToTest.getMax()+3).build());

        List<SensorRecord> actual = validator.validateAll(invalidRecords);

        assertNull(actual);
    }

    @Test
    public void validateAll_ParamIsNull_ShouldReturnNull()
    {
        List<SensorRecord> actual = validator.validateAll(null);

        assertNull(actual);
    }




}
