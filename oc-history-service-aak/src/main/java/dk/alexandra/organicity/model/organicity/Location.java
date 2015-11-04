package dk.alexandra.organicity.model.organicity;

import java.io.Serializable;

/**
 * ResourceDiscoveryAPI copy
 */
@SuppressWarnings("serial")
public class Location implements Serializable {
    public String city;
    public String country;
    public String country_code;
    
    public Location(String city, String country, String country_code) {
    	this.city = city;
    	this.country = country;
    	this.country_code = country_code;
    }
}
