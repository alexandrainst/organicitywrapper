package dk.alexandra.organicity.service;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

import dk.alexandra.organicity.model.ckan.CkanResponse;
import dk.alexandra.organicity.util.GetJson;

//​49004 http://www.odaa.dk/api/action/datastore_search?resource_id=76d1ee61-8062-42d9-b417-fac75998f5c3&filters={"GPSLongitude_2": 10.22, "GPSLatitude_2": 56.23}
//​488574 http://www.odaa.dk/api/action/datastore_search?resource_id=76d1ee61-8062-42d9-b417-fac75998f5c3&filters={"GPSLongitude_2": ​10.22}
public class AakCkanService {
	/**
	   * Helper method to get Aarhus resource at url (based on a CKAN Datastore resourceId)
	   * Generates a standard query (e.g. with server side default parameters)
	   * @param resourceId
	   * @return
	   */
	  public String getUrl(String resourceId) {
	    String s = "http://www.odaa.dk/api/action/datastore_search?resource_id=";
	    s += resourceId;
	    return s;
	  }
	  /**
	   * Helper method to get Aarhus resource at url (based on a CKAN Datastore resourceId)
	   * Generates a query with parameters (e.g. how many, which fields to query and such)
	   * @param resourceId
	   * @param offset
	   * @param limit
	   * @return
	   */
	  public String getUrl(String resourceId, int offset, int limit) {
	    String s = getUrl(resourceId);
	    s += "&offset=" + offset + "&limit=" + limit;
	    //System.out.println(s);
	    return s;
	  }

	  
	/**
	   * Internal utility function that gets the CKAN output from a url and parses it as a 
	   * dk.alexandra.organicity.model.CkanResponse
	   * @param url
	   * @return
	   * @throws IOException
	   */
	  public CkanResponse getCkanResponse(String url) throws IOException {
	    JsonParser jp = GetJson.get(url);
	    ObjectMapper mapper = new ObjectMapper();
	    CkanResponse response = mapper.readValue(jp,  CkanResponse.class);
	    if (response==null || !response.success)
	      throw new IOException("request failed");
	    return response;
	  }

	  
	  
	  /**
	   * Wrapper to directly get the list of data sources at the Aarhus CKAN.
	   * @return
	   * @throws IOException
	   */
	  public CkanResponse getCkanEntities() throws IOException {
	    return getCkanResponse(getUrl("_table_metadata", 0, 200));
	  }

}
