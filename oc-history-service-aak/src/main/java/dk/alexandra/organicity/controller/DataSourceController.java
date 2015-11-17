package dk.alexandra.organicity.controller;

import java.io.IOException;
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
import dk.alexandra.organicity.model.organicity.DeviceReadings;
import dk.alexandra.organicity.model.organicity.DeviceSensor;
import dk.alexandra.organicity.service.AakDataService;

/*
 * TODO: add     DataSourceListUpdater.initialize();
 *        move ai to aarhus
 */


/**
 * Rest Service to return datasources (aka resources/entities) and readings
 * Returns json format as specified by the Organicity API
 */
@Path("v1")
public class DataSourceController {

	static final String DATA_SOURCE_CACHE_KEY = "DATA_SOURCE_CACHE_KEY";

	public DataSourceController() {
    DataSourceListUpdater.initialize();		
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
		return "This api has two calls: /entities/{id}/readings and /entities.";
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

	@GET
	@Path("/entities/{id}/readings")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getReadings(@Context UriInfo uriInfo, @PathParam("id") long id) throws Exception {
		try {
			Map<String, List<String>> params = uriInfo.getQueryParameters();
			/*
			 * from, to, function, rollup, all_interva, attribute_id, offset, limit
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
			
			DeviceReadings readings = service.getReadings(d, attribute);
      ObjectMapper m = new ObjectMapper();
      String res = m.writeValueAsString(readings);
			return Response.ok(res).build();
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
