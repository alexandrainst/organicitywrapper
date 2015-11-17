package dk.aarhus.organicity.model.organicity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ResourceDiscoveryAPI copy
 */
@SuppressWarnings("serial")
public class DeviceData implements Serializable {
    public String recorded_at;
    public DataLocation location;
    public List<DeviceSensor> attributes;
    
    public DeviceData(String recorded_at, DataLocation location) {
    	this.recorded_at = recorded_at;
    	this.location = location;
    	this.attributes = new ArrayList<DeviceSensor>();
    }

    public DeviceData() {}
}
