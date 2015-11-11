package dk.alexandra.organicity.controller;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;

import dk.alexandra.organicity.model.organicity.Device;
import dk.alexandra.organicity.service.AakDataService;

/**
 * Rest Service to return datasources (aka resources/entities) and readings
 */
@Path("v1")
public class DataSourceController {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String defaultView() {
        return "This api has two calls: /entities/{id}/readings and /entities. \n\n"+
        		"/entities has the following parameters: latStart (default null), latEnd (default null), longStart (default null), longEnd (default null), limit (default 100) ";
    }
    
    
    @GET
    @Path("/entities")
    @Produces(MediaType.APPLICATION_JSON)
    public String getDataSources() throws IOException {
      AakDataService service = new AakDataService();
      List<Device> devices = service.getEntities();
      ObjectMapper m = new ObjectMapper();
      return m.writeValueAsString(devices);
    }
    //TODO: check null in id.
    //check output in general
    //get correct data from ODAA
    //why are there only 3 attributes - check your conversion alg
    //add javadoc to all public facing functions
    //cache?
    //tomcat deployment?
    /* Example output
     * [
     * {"id":8063,
     * "uuid":"null76d1ee61-8062-42d9-b417-fac75998f5c3:1",
     * "name":"null76d1ee61-8062-42d9-b417-fac75998f5c3:1",
     * "last_reading_at":"2015-11-11T16:53+0100",
     * "provider":{"id":2,"uuid":null,"username":"urn:oc:entity:aarhus","avatar":"http://cliparts.co/cliparts/LTd/jL4/LTdjL4djc.jpg","url":"https://en.wikipedia.org/wiki/Aarhus","joined_at":"","location":{"city":"Aarhus","country":"Danmark","country_code":"DK"},"device_ids":null},
     * "data":{"recorded_at":"2015-11-11T16:53+0100","location":{"latitude":56.12,"longitude":10.2,"city":null,"country_code":null,"country":null},
     *   "attributes":[
     *     {"id":1,"name":"_id","unit":"<unknown>","updated_at":"2013-07-01 09:23:10","attributes_id":"urn:oc:attributeType:_id","value":1035192.0,"prev_value":1035193.0},
     *     {"id":2,"name":"Container_Vejning_ID","unit":"<unknown>","updated_at":"2013-07-01 09:23:10","attributes_id":"urn:oc:attributeType:Container_Vejning_ID","value":1043507.0,"prev_value":1043508.0},
     *     {"id":3,"name":"Indlaes_Vejning_ID","unit":"<unknown>","updated_at":"2013-07-01 09:23:10","attributes_id":"urn:oc:attributeType:Indlaes_Vejning_ID","value":2246347.0,"prev_value":2246348.0}
     *     ]
     * },
     * "entities_type":null}]
     */
    
    @GET
    @Path("/entities/{id}/readings")
    @Produces(MediaType.TEXT_PLAIN)
    public long getReadings(@PathParam("id") long id) {
      return id;
    }
}
