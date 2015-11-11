package dk.alexandra.organicity.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

import dk.alexandra.organicity.model.ckan.CkanResponse;
import dk.alexandra.organicity.model.ckan.Field;
import dk.alexandra.organicity.model.ckan.Result;
import dk.alexandra.organicity.model.organicity.DataLocation;
import dk.alexandra.organicity.model.organicity.Device;
import dk.alexandra.organicity.model.organicity.DeviceData;
import dk.alexandra.organicity.model.organicity.DeviceOwner;
import dk.alexandra.organicity.model.organicity.DeviceSensor;
import dk.alexandra.organicity.model.organicity.Location;
import dk.alexandra.organicity.util.DateFormatter;
import dk.alexandra.organicity.util.GetJson;

/**
 * See example output at bottom of class
 * 
 * @author neerbek
 *
 */
public class AakDataService {
  /**
   * Helper method to get Aarhus resource at url (based on a CKAN Datastore resourceId)
   * Generates a standard query (e.g. with server side default parameters)
   * @param resourceId
   * @return
   */
  private String getUrl(String resourceId) {
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
  private String getUrl(String resourceId, int offset, int limit) {
    String s = getUrl(resourceId);
    s += "&offset=" + offset + "&limit=" + limit;
    //System.out.println(s);
    return s;
  }

  /**
   * A utility function that generates a default "Aarhus" owner for all data sources. To be used when generating 
   * the Organicity response
   * @return
   */
  private DeviceOwner getAarhusOwner() {
    return new DeviceOwner(2, "urn:oc:entity:aarhus","urn:oc:entity:aarhus", 
        "http://cliparts.co/cliparts/LTd/jL4/LTdjL4djc.jpg",  "https://en.wikipedia.org/wiki/Aarhus",
        "",  new Location("Aarhus", "Danmark", "DK"));
  }

  /**
   * Gets the list of entities (data sources/resouces) from the Aarhus CKAN and reformats those into the Organicity format
   * @return
   * @throws IOException
   */
  public List<Device> getEntities() throws IOException {
    CkanResponse response = getCkanEntities();
    List<Device> res = new ArrayList<Device>();
    for (Map<String, Object> rec : response.result.records) {
      String source_uuid = (String) rec.get("name"); 
      if (source_uuid.equals("76d1ee61-8062-42d9-b417-fac75998f5c3")) {
        //tomming
        System.out.println("tomings data found");
        int id = 1;
        DeviceOwner provider = getAarhusOwner();
        String name = provider.uuid + source_uuid + ":" + id;
        Device device = new Device(id + 8062,  name, name);
        device.provider = provider;
        DeviceData data = getLastMeasurement(source_uuid);
        if (data!=null)
          device.last_reading_at = data.recorded_at;
        device.data = data;
        res.add(device);
      }
    }		
    return res;
  }

  /**
   * Internal utility function that gets the CKAN output from a url and parses it as a 
   * dk.alexandra.organicity.model.CkanResponse
   * @param url
   * @return
   * @throws IOException
   */
  private CkanResponse getCkanResponse(String url) throws IOException {
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
  
  /**
   * Helper function that takes a resourceId and returns the last two measurements in the data stream for this resourceId
   * The Organicity API expects this measurements in the list of data sources.
   * 
   * @param source_uuid
   * @return
   * @throws IOException
   */
  private DeviceData getLastMeasurement(String source_uuid) throws IOException {
    long start = System.currentTimeMillis();
    //CkanResponse ckan =  getCkanResponse(getUrl(source_uuid, 0, 2));
    CkanResponse ckan =  getTestCkanResponse();
    System.out.println("CKAN response in: " + ((double) (System.currentTimeMillis() -start)/1000));
    return getLastMeasurementImpl(ckan);
  }
  
  public CkanResponse getTestCkanResponse()
  {
    CkanResponse ckan = new CkanResponse();
    ckan.success = true;
    ckan.total = 1000;
    ckan.result = new Result();
    ckan.result.fields = new ArrayList<Field>();
    ckan.result.fields.add(new Field("int4", "_id"));
    ckan.result.fields.add(new Field("int4", "Container_Vejning_ID"));
    ckan.result.fields.add(new Field("int4", "Indlaes_Vejning_ID"));
    ckan.result.fields.add(new Field("text", "Dato"));
    ckan.result.fields.add(new Field("text", "Tid"));
    ckan.result.fields.add(new Field("float8", "Vejning"));
    ckan.result.fields.add(new Field("text", "Opd_Init"));
    ckan.result.fields.add(new Field("text", "Opd_Dato"));
    ckan.result.fields.add(new Field("float8", "GPSLongitude_2"));
    ckan.result.fields.add(new Field("float8", "GPSLatitude_2"));
    ckan.result.fields.add(new Field("int4", "Container_Vejning_Faktura_Opstil_id"));
    ckan.result.fields.add(new Field("float8", "FrivaegtKg"));
    ckan.result.records = new ArrayList<Map<String,Object>>();
    Map<String,Object> r = new HashMap<String,Object>();
    r.put("Opd_Init", "SYSRS");
    r.put("Container_Vejning_ID", new Integer(1043507));
    r.put("GPSLongitude_2", new Float(10.18));
    r.put("Indlaes_Vejning_ID", new Integer(2246347));
    r.put("Opd_Dato", "2013-07-01 00:00:00.0");
    r.put("Dato", "2013-07-01 00:00:00.0");      
    r.put("Container_Vejning_Faktura_Opstil_id", null);      
    r.put("Tid", "09:23:10");      
    r.put("Vejning", new Float(9.5));      
    r.put("_id", new Integer(1035192));            
    r.put("FrivaegtKg", new Float(0.0));
    r.put("GPSLatitude_2", new Float(56.17));
    ckan.result.records.add(r);
    r = new HashMap<String,Object>();
    r.put("Opd_Init", "SYSRS");
    r.put("Container_Vejning_ID", new Integer(1043508));
    r.put("GPSLongitude_2", new Float(10.03));
    r.put("Indlaes_Vejning_ID", new Integer(2246348));
    r.put("Opd_Dato", "2013-07-01 00:00:00.0");
    r.put("Dato", "2013-07-01 00:00:00.0");      
    r.put("Container_Vejning_Faktura_Opstil_id", null);      
    r.put("Tid", "09:23:20");      
    r.put("Vejning", new Float(20.0));      
    r.put("_id", new Integer(1035193));            
    r.put("FrivaegtKg", new Float(0.0));
    r.put("GPSLatitude_2", new Float(56.21));
    ckan.result.records.add(r);
    return ckan;
  }
  /**
   * Internal impl to getLastMeasurement. Made public to allow tests
   * 
   * We should consider moving this (and other) function to an impl. class
   * @param ckan
   * @return
   * @throws IOException
   */
  public DeviceData getLastMeasurementImpl(CkanResponse ckan) throws IOException {
    if (ckan==null || ckan.result == null || ckan.result.records==null || ckan.result.fields==null)
      throw new IOException("Missing fields in CKAN response - cannot form organicity response");
    DataLocation loc = new DataLocation(56.12, 10.2); //TODO: these values are hardcoded for now
    DeviceData res = new DeviceData(DateFormatter.getCurrentTime(), loc);

    Map<String, Object> lastMeasurement = new HashMap<String, Object>();
    Map<String, Object> lastLastMeasurement = new HashMap<String, Object>();
    if (ckan.result.records.size()>1)
      lastLastMeasurement = ckan.result.records.get(1); 
    if (ckan.result.records.size()>0)
      lastMeasurement = ckan.result.records.get(0);
    else
      throw new IOException("Expected at least one measurement, found none - aborting"); //we cannot know data type if there are no measurements

    int fieldId = 1;
    for (Field f : ckan.result.fields) {
      DeviceSensor d = new DeviceSensor();
      d.id = fieldId;
      d.name = f.id;
      d.unit = "<unknown>";
      d.updated_at = formatDate((String) lastMeasurement.get("Opd_Dato"), (String) lastMeasurement.get("Tid"));  
      d.attributes_id = "urn:oc:attributeType:" + f.id;
      Object o = lastMeasurement.get(f.id);
      Double value = convertDouble(o);
      if (value==null)
        continue;
      d.value = value;
      if (lastLastMeasurement!=null) {
        value = convertDouble(lastLastMeasurement.get(f.id));
        if (value!=null)
          d.prev_value = value;  //lastlast parsing to null is not considered an exception
      }
      res.attributes.add(d);
      fieldId++;
    }
    return res;
  }
  
  /**
   * Tries to convert attribute value (string) to an integer/double - returns null if fails
   * @param val
   * @return
   */
  private Double convertDouble(Object o)  
  {
    Double res = null;
    if (o==null)
      return null;
    if (o instanceof Integer)
      res = new Double((Integer) o);
    else if (o instanceof Double)
      res = (Double) o;
    else if (o instanceof String) {
      String val = (String) o;
      val = val.replaceAll(" \"'", "");
      try
      {
         res = Double.parseDouble(val);
      }
      catch (NumberFormatException nfe)
      {
      }
    }
    else {
      //unknown object type
    }
    return res;

  }
  
  private String formatDate(String date, String time) {
    if (date==null || time==null)
      throw new RuntimeException("Cannot format date with empty string");
    return date.substring(0,11) + time;
  }
}
/* example output that we should match from: http://explorer-api.organicity.smartcitizen.me:8090/v1/entities
 *
 * We should match the class Device
{

    "id": ​1085782247,
    "uuid": "urn:oc:entity:london:smartphone:phone:2",
    "name": "urn:oc:entity:london:smartphone:phone:2",
    "last_reading_at": "2015-11-09T23:09Z",
    "provider": 

{

    "id": ​0,
    "uuid": "urn:oc:entity:london",
    "username": "London",
    "avatar": "http://cliparts.co/cliparts/LTd/jL4/LTdjL4djc.jpg",
    "url": "https://en.wikipedia.org/wiki/London",
    "joined_at": "",
    "location": 

    {
        "city": "London",
        "country": "United Kingdom",
        "country_code": "UK"
    },
    "device_ids": [ ]

},
"data": 
{

    "recorded_at": "2015-11-09T23:09Z",
    "location": 

{

    "latitude": ​38.25880788,
    "longitude": ​21.74640886,
    "city": null,
    "country_code": null,
    "country": null

},
"attributes": 
[

{

    "id": ​1303015757,
    "name": "atmosphericPressure",
    "unit": "bar",
    "updated_at": "2015-11-09T23:09Z",
    "attributes_id": "urn:oc:atributeType:atmosphericPressure",
    "value": ​1014.47998046875,
    "prev_value": ​0.0

},
{

    "id": ​1657779520,
    "name": "relativeHumidity",
    "unit": "percent",
    "updated_at": "2015-11-09T23:09Z",
    "attributes_id": "urn:oc:attributeType:relativeHumidity",
    "value": ​41.99514389038086,
    "prev_value": ​0.0

},
{

    "id": ​478089795,
    "name": "soundPressureLevel:ambient",
    "unit": "decibel",
    "updated_at": "2015-11-09T23:09Z",
    "attributes_id": "urn:oc:atributeType:soundPressureLevel:ambient",
    "value": ​37.399101460943584,
    "prev_value": ​0.0

},

            {
                "id": ​809555331,
                "name": "temperature:ambient",
                "unit": "degreeCelsius",
                "updated_at": "2015-11-09T23:09Z",
                "attributes_id": "urn:oc:atributeType:temperature:ambient",
                "value": ​24.650035858154297,
                "prev_value": ​0.0
            }
        ]
    },
    "entities_type": null

},
 */