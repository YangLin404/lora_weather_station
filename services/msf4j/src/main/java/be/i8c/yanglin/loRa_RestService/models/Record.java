package be.i8c.yanglin.loRa_RestService.models;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Record
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String deviceId;
    private String owner;
    private String stream_id;
    private String streamUnit;
    private Long streamValueTime;
    private String sensorValue;
    @Enumerated(EnumType.STRING)
    private SensorType type;
    
    protected Record()
    {
        
    }

    public Record(String deviceId, String owner, String stream_id, String streamUnit, Long streamValueTime, SensorType type) {
        this.deviceId = deviceId;
        this.owner = owner;
        this.stream_id = stream_id;
        this.streamUnit = streamUnit;
        this.streamValueTime = streamValueTime;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getStream_id() {
        return stream_id;
    }

    public void setStream_id(String stream_id) {
        this.stream_id = stream_id;
    }

    public String getStreamUnit() {
        return streamUnit;
    }

    public void setStreamUnit(String streamUnit) {
        this.streamUnit = streamUnit;
    }

    public Long getStreamValueTime() {
        return streamValueTime;
    }

    public void setStreamValueTime(Long streamValueTime) {
        this.streamValueTime = streamValueTime;
    }

    public String getValue() {
        return sensorValue;
    }

    public void setValue(String value) {
        this.sensorValue = value;
    }

    public SensorType getType() {
        return type;
    }

    public void setType(SensorType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Record{" + "id=" + id + ", deviceId=" + deviceId + ", owner=" + owner + ", stream_id=" + stream_id + ", streamUnit=" + streamUnit + ", streamValueTime=" + streamValueTime + ", sensorValue=" + sensorValue + ", type=" + type + '}';
    }
    
    
    
}