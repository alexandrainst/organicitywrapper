package dk.alexandra.organicity.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

import dk.alexandra.organicity.model.organicity.Device;
import dk.alexandra.organicity.model.organicity.DeviceData;
import dk.alexandra.organicity.model.organicity.DeviceOwner;
import dk.alexandra.organicity.model.organicity.Location;
import dk.alexandra.organicity.util.DateFormatter;
import dk.alexandra.organicity.util.GetJson;

public class AakDataService {
	private String getUrl(String resourceId) {
		String s = "http://www.odaa.dk/api/action/datastore_search?resource_id=";
		s += resourceId;
		return s;
	}
	private String getUrl(String resourceId, int limit) {
		String s = getUrl(resourceId);
		s += "&limit=" + limit;
		return s;
	}
	
	private DeviceOwner getAarhusOwner() {
		return new DeviceOwner(2, "urn:oc:entity:aarhus","urn:oc:entity:aarhus", 
				"http://cliparts.co/cliparts/LTd/jL4/LTdjL4djc.jpg",  "https://en.wikipedia.org/wiki/Aarhus",
				"",  new Location("Aarhus", "Danmark", "DK"));
	}
	@SuppressWarnings("unchecked")
	public List<Device> getEntities() throws IOException {
		JsonParser jp = GetJson.get(getUrl("_table_metadata", 200));
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> d = mapper.readValue(jp, Map.class);
		if (!d.containsKey("success"))
			throw new IOException("failed to read metadata from request");
		boolean dd = (Boolean) d.get("success");
		if (!dd)
			throw new IOException("read metadata failed at source");	
		d = (Map<String, Object>) d.get("result");
		List<Object> list = (List<Object>) d.get("records");
		List<Device> res = new ArrayList<Device>();
		for (Object o : list) {
			Map<String, Object> rec = (Map<String, Object>) o;
			String source_uuid = (String) rec.get("name"); 
			if (source_uuid.equals("76d1ee61-8062-42d9-b417-fac75998f5c3")) {
				//tomming
				System.out.println("tomings data found");
				int id = 1;
				DeviceOwner provider = getAarhusOwner();
				String name = provider.uuid + source_uuid + ":" + id;
				Device device = new Device(id + 8062,  name, name); 
				DeviceData data = getLastMeasurement(source_uuid);
			}
		}
		return res;
	}
	private DeviceData getLastMeasurement(String source_uuid) throws IOException {
		JsonParser jp = GetJson.get(getUrl(source_uuid, 2));
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> d = mapper.readValue(jp, Map.class);
		if (!d.containsKey("success"))
			throw new IOException("failed to read metadata from request");
		boolean dd = (Boolean) d.get("success");
		if (!dd)
			throw new IOException("read metadata failed at source");	
		DeviceData res = new DeviceData(DateFormatter.getCurrentTime(), null);
		
		return null;
	}
}
