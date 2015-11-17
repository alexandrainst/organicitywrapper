package test.dk.aarhus.organicity.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Ignore;
import org.junit.Test;

import dk.aarhus.organicity.model.ckan.CkanResponse;
import dk.aarhus.organicity.model.organicity.Device;
import dk.aarhus.organicity.service.AakDataService;

public class TestJson {

	@Ignore
	@Test
	public void testJson() throws JsonParseException, IOException{
		String in = "{ \"success\": \"true\" }";
	    JsonFactory jfactory = new JsonFactory();
		JsonParser jp = jfactory.createJsonParser(in);
		ObjectMapper mapper = new ObjectMapper();
		CkanResponse response = mapper.readValue(jp,  CkanResponse.class);
		assertTrue("succes was not set", response.success);
	}

	@Test
	public void testJsonLong() throws JsonParseException, IOException{
		String in = "{\"help\": \"http://www.odaa.dk/api/3/action/help_show?name=datastore_search\", "
				+ "\"success\": true, \"result\": {\"resource_id\": \"_table_metadata\", \"fields\": "
				+ "[{\"type\": \"text\", \"id\": \"_id\"}, {\"type\": \"name\", \"id\": \"name\"}, "
				+ "{\"type\": \"oid\", \"id\": \"oid\"}, {\"type\": \"name\", \"id\": \"alias_of\"}], "
				+ "\"records\": [{\"_id\": \"0661bc9e79befc2d\", \"alias_of\": null, \"name\": "
				+ "\"7642639b-2aa3-43f4-a474-7aaccfc10618\", \"oid\": 628490}, {\"_id\": \"9dd7c013c021a146\", "
				+ "\"alias_of\": null, \"name\": \"2a82a145-0195-4081-a13c-b0e587e9b89c\", \"oid\": 628475}], "
				+ "\"limit\": 2, \"_links\": {\"start\": \"/api/action/datastore_search?limit=2&resource_id=_table_metadata\", "
				+ "\"next\": \"/api/action/datastore_search?offset=2&limit=2&resource_id=_table_metadata\"}, \"total\": 173}}";
	    JsonFactory jfactory = new JsonFactory();
		JsonParser jp = jfactory.createJsonParser(in);
		ObjectMapper mapper = new ObjectMapper();
		CkanResponse response = mapper.readValue(jp,  CkanResponse.class);
		assertTrue("succes was not set", response.success);
		assertTrue("result was not set", response.result!=null);
		assertTrue("fields was not set", response.result.fields!=null);
		assertEquals("fields had wrong length", response.result.fields.size(), 4);
		assertEquals("results had wrong length", response.result.records.size(), 2);
		assertEquals("results[0] had wrong value", response.result.records.get(0).get("_id"), "0661bc9e79befc2d");
	}
	
	@Test
	public void testList2Json() throws JsonGenerationException, JsonMappingException, IOException, ParseException {
	  List<Device> list = new ArrayList<Device>();
	  AakDataService.USE_DEMO_DATA = true;
	  AakDataService s = AakDataService.createService();
    
	  Device d = new Device(1, "1", "1");
	  d.data = s.getLastMeasurement("");
	  list.add(d);
	  ObjectMapper mapper = new ObjectMapper();
    String js = mapper.writeValueAsString(list);
    System.out.println(js);
	}
}
