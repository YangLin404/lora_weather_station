package be.i8c.yanglin.loRa_RestService.models;

import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="Record")
@XmlAccessorType(XmlAccessType.FIELD)
public class SensorRecord implements Record
{
    @XmlTransient
    private String deviceId;
    @XmlTransient
    private String owner;
    @XmlTransient
    private String stream_id;
    @XmlTransient
    private String streamUnit;
    @XmlElement
    private long streamValueTime;
    @XmlElement
    private double sensorValue;
    @XmlElement
    private SensorType type;
    
    protected SensorRecord()
    {
        
    }

    public SensorRecord(String deviceId, String owner, String stream_id, String streamUnit, Long streamValueTime, SensorType type) {
        this.deviceId = deviceId;
        this.owner = owner;
        this.stream_id = stream_id;
        this.streamUnit = streamUnit;
        this.streamValueTime = streamValueTime;
        this.type = type;
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

    public double getValue() {
        return sensorValue;
    }
    
    public void setValue(double value) {
        this.sensorValue = value;
    }

    public SensorType getType() {
        return type;
    }
    
    public void setType(SensorType type) {
        this.type = type;
    }
    
    @Override
    public String simpleString()
    {
        return "Record{Type: " + type + ", value: " + sensorValue + "}";
    }

    @Override
    public String toString() {
        return "Record{" + ", deviceId=" + deviceId + ", owner=" + owner + ", stream_id=" + stream_id + ", streamUnit=" + streamUnit + ", streamValueTime=" + streamValueTime + ", sensorValue=" + sensorValue + ", type=" + type + '}';
    }
    
    
    
    
}