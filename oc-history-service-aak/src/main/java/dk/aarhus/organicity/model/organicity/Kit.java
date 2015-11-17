package dk.aarhus.organicity.model.organicity;

import java.io.Serializable;

/**
 * ResourceDiscoveryAPI copy
 */
@SuppressWarnings("serial")
public class Kit implements Serializable {
    public int id;
    public String uuid;
    public String slug;
    public String name;
    public String description;
    public String created_at;
    public String updated_at;
    
    public Kit() {}
}
