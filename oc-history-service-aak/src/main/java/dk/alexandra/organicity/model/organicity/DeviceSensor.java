package dk.alexandra.organicity.model.organicity;

import java.io.Serializable;

/**
 * ResourceDiscoveryAPI copy
 */
@SuppressWarnings("serial")
public class DeviceSensor implements Serializable {
    public int id;
    public String name;
    public String unit;
    public String updated_at;
    //public String metadata_id; removed from this copy
    public String attributes_id;
    public double value;
    public double prev_value;
    
    public DeviceSensor() {}
}
