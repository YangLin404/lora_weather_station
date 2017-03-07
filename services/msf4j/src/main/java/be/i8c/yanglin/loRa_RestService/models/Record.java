package be.i8c.yanglin.loRa_RestService.models;

import java.util.Date;

public class Record
{
    private String deviceId;
    private String owner;
    private String stream_id;
    private String streamUnit;
    private Date streamValueTime;
    private String value;
    private SensorType type;
    
    public Record()
    {
        
    }

    public Record(String deviceId, String owner, String stream_id, String streamUnit, Date streamValueTime) {
        this.deviceId = deviceId;
        this.owner = owner;
        this.stream_id = stream_id;
        this.streamUnit = streamUnit;
        this.streamValueTime = streamValueTime;
    }
    
    protected void setValue(String v)
    {
        this.value = v;
    }
        
        
        
        
	
	
	
	
}