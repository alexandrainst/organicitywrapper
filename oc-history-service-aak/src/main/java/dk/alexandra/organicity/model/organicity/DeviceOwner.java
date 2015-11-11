package dk.alexandra.organicity.model.organicity;

import java.io.Serializable;
import java.util.List;

/**
 * ResourceDiscoveryAPI copy
 */
@SuppressWarnings("serial")
public class DeviceOwner implements Serializable {
    public int id;
    public String uuid;
    public String username;
    public String avatar;
    public String url;
    public String joined_at;
    public Location location;
    public List<Integer> device_ids;
    
    public DeviceOwner(int id, String uuid, String username, String avatar, String url, 
    		String joind_at, Location location) {
    	this.id = id;
    	this.username = username;
    	this.avatar = avatar;
    	this.url= url;
    	this.joined_at = joind_at;
    	this.location = location;
    }
    
    public DeviceOwner() {}
    
}
