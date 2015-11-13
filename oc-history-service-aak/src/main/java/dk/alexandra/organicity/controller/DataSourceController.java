package dk.alexandra.organicity.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import dk.alexandra.organicity.model.organicity.Device;
import dk.alexandra.organicity.model.organicity.DeviceSensor;
import dk.alexandra.organicity.service.AakDataService;

/**
 * Rest Service to return datasources (aka resources/entities) and readings
 */
@Path("v1")
public class DataSourceController {

	static final String DATA_SOURCE_CACHE_KEY = "DATA_SOURCE_CACHE_KEY";

	public DataSourceController() throws IOException, ParseException {
		SimpleCache c = SimpleCache.getCache();
		boolean runUpdater = false;
		synchronized (c) {
			SimpleCache.Element cache = SimpleCache.getCache().getElement(DataSourceController.DATA_SOURCE_CACHE_KEY);
			if (cache==null) {
				cache = new SimpleCache.Element(new ArrayList<Device>());
				SimpleCache.getCache().addElement(DataSourceController.DATA_SOURCE_CACHE_KEY, cache);
				SimpleCache.getCache().doUpdate(cache); //mark for updating
				runUpdater = true;
			}
		}
		if (runUpdater)
			DataSourceListUpdater.runUpdater();
	}
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
	public String getDataSources() throws JsonGenerationException, JsonMappingException, IOException {
		try {
			SimpleCache.Element cache = SimpleCache.getCache().getElement(DATA_SOURCE_CACHE_KEY);
			if (!cache.isValid() && SimpleCache.getCache().doUpdate(cache)) {
				DataSourceListUpdater.runUpdater();
			}
			@SuppressWarnings("unchecked")
			List<Device> devices = (List<Device>) cache.value;
			ObjectMapper m = new ObjectMapper();
			String res = m.writeValueAsString(devices);

			return res;
		} catch (Exception e) {
			System.out.println("GET entities failed: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}

	}
	//TODO:
	//get correct data from ODAA
	//add javadoc to all public facing functions
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
	@Produces(MediaType.APPLICATION_JSON)
	public Response getReadings(@Context UriInfo uriInfo, @PathParam("id") long id) throws Exception {
		try {
			Map<String, List<String>> params = uriInfo.getQueryParameters();
			/*
			 * from
			 * to
			 * function
			 * rollup
			 * all_interva
			 * attribute_id
			 * offset
			 * limit
			 */
			int attribute_id = toInt(getLatestParam(params, "attribute_id"),-1);
			SimpleCache.Element cache = SimpleCache.getCache().getElement(DATA_SOURCE_CACHE_KEY);
			if (!cache.isValid() && SimpleCache.getCache().doUpdate(cache)) {
				DataSourceListUpdater.runUpdater();
			}
			AakDataService service = AakDataService.createService();
			@SuppressWarnings("unchecked")
			List<Device> devices = (List<Device>) cache.value;
			Device d = service.getDevice(devices, id);
			if (d==null)
				return Response.status(Status.NOT_FOUND).entity("[{ \"success\":\"false\", \"message\":\"Not data source with id: " + id + " could be found\"}]").build();
			DeviceSensor attribute = service.getDeviceAttribute(d, attribute_id); 
			if (attribute == null)
				return Response.status(Status.NOT_FOUND).entity("[{ \"success\":\"false\", \"message\":\"Found not find attribute with id " + attribute_id + "\"}]").build();
			
			service.getReadings(d, attribute);
			return Response.ok("[]").build();
		} catch (Exception e) {
			System.out.println("Readings failed: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}
	private int toInt(String latestParam, int defaultValue) {
		if (latestParam==null)
			return defaultValue;
		try {
			return Integer.parseInt(latestParam);
		} catch (Exception e) {
		}
		return defaultValue;
	}
	private String getLatestParam(Map<String, List<String>> params,
			String p) {
		if (params==null || p==null)
			return null;
		List<String> v = params.get(p);
		if (v==null || v.size()==0)
			return null;
		return v.get(v.size()-1);
	}
}
