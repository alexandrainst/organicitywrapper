package dk.alexandra.organicity.controller;

import java.io.IOException;
import java.text.ParseException;
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
      try {
      AakDataService service = AakDataService.createService();
      List<Device> devices = service.getEntities();
      ObjectMapper m = new ObjectMapper();
      return m.writeValueAsString(devices);
      } catch (Exception e) {
        System.out.println("Exception in getDataSources: " + e.getMessage());
        throw new IOException("Exception in getDataSources: " + e.getMessage(), e);
      }
    }
    //TODO:
    //fix missing attributes
    //get correct data from ODAA
    //add javadoc to all public facing functions
    //cache! Redis?
    //tomcat deployment?
    //nice-to-have: remove warnings
    /* Example output
     * [
    {
        "data": {
            "attributes": [
                {
                    "attributes_id": "urn:oc:attributeType:_id",
                    "id": 1,
                    "name": "_id",
                    "prev_value": 1035193.0,
                    "unit": "<unknown>",
                    "updated_at": "2013-07-01T07:23Z",
                    "value": 1035192.0
                },
                {
                    "attributes_id": "urn:oc:attributeType:Container_Vejning_ID",
                    "id": 2,
                    "name": "Container_Vejning_ID",
                    "prev_value": 1043508.0,
                    "unit": "<unknown>",
                    "updated_at": "2013-07-01T07:23Z",
                    "value": 1043507.0
                },
                {
                    "attributes_id": "urn:oc:attributeType:Indlaes_Vejning_ID",
                    "id": 3,
                    "name": "Indlaes_Vejning_ID",
                    "prev_value": 2246348.0,
                    "unit": "<unknown>",
                    "updated_at": "2013-07-01T07:23Z",
                    "value": 2246347.0
                },
                {
                    "attributes_id": "urn:oc:attributeType:Vejning",
                    "id": 4,
                    "name": "Vejning",
                    "prev_value": 20.0,
                    "unit": "<unknown>",
                    "updated_at": "2013-07-01T07:23Z",
                    "value": 9.5
                },
                {
                    "attributes_id": "urn:oc:attributeType:GPSLongitude_2",
                    "id": 5,
                    "name": "GPSLongitude_2",
                    "prev_value": 10.029999732971191,
                    "unit": "<unknown>",
                    "updated_at": "2013-07-01T07:23Z",
                    "value": 10.180000305175781
                },
                {
                    "attributes_id": "urn:oc:attributeType:GPSLatitude_2",
                    "id": 6,
                    "name": "GPSLatitude_2",
                    "prev_value": 56.209999084472656,
                    "unit": "<unknown>",
                    "updated_at": "2013-07-01T07:23Z",
                    "value": 56.16999816894531
                },
                {
                    "attributes_id": "urn:oc:attributeType:FrivaegtKg",
                    "id": 7,
                    "name": "FrivaegtKg",
                    "prev_value": 0.0,
                    "unit": "<unknown>",
                    "updated_at": "2013-07-01T07:23Z",
                    "value": 0.0
                }
            ],
            "location": {
                "city": null,
                "country": null,
                "country_code": null,
                "latitude": 56.12,
                "longitude": 10.2
            },
            "recorded_at": "2015-11-12T13:31Z"
        },
        "entities_type": null,
        "id": 8063,
        "last_reading_at": "2015-11-12T13:31Z",
        "name": "urn:oc:entity:aarhus76d1ee61-8062-42d9-b417-fac75998f5c3:1",
        "provider": {
            "avatar": "http://cliparts.co/cliparts/LTd/jL4/LTdjL4djc.jpg",
            "device_ids": null,
            "id": 2,
            "joined_at": "",
            "location": {
                "city": "Aarhus",
                "country": "Danmark",
                "country_code": "DK"
            },
            "url": "https://en.wikipedia.org/wiki/Aarhus",
            "username": "Aarhus",
            "uuid": "urn:oc:entity:aarhus"
        },
        "uuid": "urn:oc:entity:aarhus76d1ee61-8062-42d9-b417-fac75998f5c3:1"
    }
]
     */
    
    @GET
    @Path("/entities/{id}/readings")
    @Produces(MediaType.TEXT_PLAIN)
    public long getReadings(@PathParam("id") long id) {
      return id;
    }
}
