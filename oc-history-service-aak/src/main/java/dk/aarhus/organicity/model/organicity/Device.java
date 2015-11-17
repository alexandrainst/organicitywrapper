package dk.aarhus.organicity.model.organicity;

import java.io.Serializable;

/**
 * ResourceDiscoveryAPI copy
 */
@SuppressWarnings("serial")
public class Device implements Serializable {
    public int id;
    public String uuid;
    public String name;
    public String last_reading_at;
    public DeviceOwner provider;
    public DeviceData data;
    public Kit entities_type;
    
    public Device(int id, String uuid, String name) {
    	this.id = id;
    	this.uuid = uuid;
    	this.name = uuid;
    }
    
    public Device() {}
}

