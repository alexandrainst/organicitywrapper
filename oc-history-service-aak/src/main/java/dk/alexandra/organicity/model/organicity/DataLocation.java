package dk.alexandra.organicity.model.organicity;

import java.io.Serializable;

/**
 * ResourceDiscoveryAPI copy
 */
@SuppressWarnings("serial")
public class DataLocation implements Serializable {
	public double latitude;
    public double longitude;
    public String city;
    public String country_code;
    public String country;

    public DataLocation(double latitude, double longitude) {
    	this.latitude = latitude;
    	this.longitude = longitude;
	}

}
