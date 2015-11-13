package dk.alexandra.organicity.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dk.alexandra.organicity.model.ckan.CkanResponse;
import dk.alexandra.organicity.model.ckan.Field;
import dk.alexandra.organicity.model.organicity.DataLocation;
import dk.alexandra.organicity.model.organicity.Device;
import dk.alexandra.organicity.model.organicity.DeviceData;
import dk.alexandra.organicity.model.organicity.DeviceOwner;
import dk.alexandra.organicity.model.organicity.DeviceSensor;
import dk.alexandra.organicity.model.organicity.Location;
import dk.alexandra.organicity.util.DateFormatter;

/**
 * See example output at bottom of class
 * 
 * @author neerbek
 *
 */
public class AakDataService {
  AakCkanService ckanService; //Used for getting data from (the Aarhus) Ckan

  public AakDataService(AakCkanService ckanService) {
    setCkanService(ckanService);
  }

  public void setCkanService(AakCkanService ckanService) {
    this.ckanService = ckanService;
  }
  /**
   * A utility function that generates a default "Aarhus" owner for all data sources. To be used when generating 
   * the Organicity response
   * @return
   */
  private DeviceOwner getAarhusOwner() {
    return new DeviceOwner(2, "urn:oc:entity:aarhus","Aarhus", 
        "http://cliparts.co/cliparts/LTd/jL4/LTdjL4djc.jpg",  "https://en.wikipedia.org/wiki/Aarhus",
        "",  new Location("Aarhus", "Danmark", "DK"));
  }

  /**
   * Gets the list of entities (data sources/resouces) from the Aarhus CKAN and reformats those into the Organicity format
   * @return
   * @throws IOException
   * @throws ParseException 
   */
  public List<Device> getEntities() throws IOException, ParseException {
    CkanResponse response = ckanService.getCkanResponse(ckanService.getUrl(Record.SourceUuid, 0, 50, null, Record.FieldName.Opd_Dato + " Desc"));
    Map<String, Record> sources = new HashMap<String, Record>();
    for (Map<String, Object> rec : response.result.records) {
      Record record = new Record(rec);
      if (sources.containsKey(record.getKey()))
        record = sources.get(record.getKey());
      else
        sources.put(record.getKey(), record);
      record.updateMeasurements(rec);
    }

    List<Device> res = new ArrayList<Device>();
    int id = 1;
    for (Record record : sources.values()) {
      DeviceOwner provider = getAarhusOwner();
      String name = provider.uuid + Record.SourceUuid + ":" + record.getKey();
      Device device = new Device(id + Record.SourceUuidOffset,  name, name);
      device.provider = provider;
      DeviceData data;
      data = getLastMeasurement(record);
      if (data!=null)
        device.last_reading_at = data.recorded_at;
      device.data = data;
      res.add(device);
      id++;
    }
    return res;
  }

  private DeviceData getLastMeasurement(Record record) throws ParseException
  {
    DataLocation loc = new DataLocation(record.latitude, record.longitude);
    DeviceData res = new DeviceData(DateFormatter.getCurrentTime(), loc);

    Map<String, Object> lastMeasurement = record.lastMeasurements;
    Map<String, Object> lastLastMeasurement = record.lastLastMeasurements;

    int fieldId = 1;
    for (Record.FieldName f : Record.FieldName.values()) {
      DeviceSensor d = new DeviceSensor();
      d.id = fieldId;
      d.name = f.toString();
      d.unit = "<unknown>";
      d.updated_at = formatDate((String) lastMeasurement.get("Opd_Dato"), (String) lastMeasurement.get("Tid"));  
      d.attributes_id = "urn:oc:attributeType:" + f;
      Object o = lastMeasurement.get(f);
      Double value = convertDouble(o);
      if (value==null)
        continue;
      d.value = value;
      if (lastLastMeasurement!=null) {
        value = convertDouble(lastLastMeasurement.get(f));
        if (value!=null)
          d.prev_value = value;  //lastlast parsing to null is not considered an exception
      }
      res.attributes.add(d);
      fieldId++;
    }
    return res;
  }

  /**
   * Helper function that takes a resourceId and returns the last two measurements in the data stream for this resourceId
   * The Organicity API expects this measurements in the list of data sources.
   * 
   * @param source_uuid
   * @return
   * @throws IOException
   * @throws ParseException 
   */
  public DeviceData getLastMeasurement(String source_uuid) throws IOException, ParseException {
    long start = System.currentTimeMillis();
    CkanResponse ckan =  ckanService.getCkanResponse(ckanService.getUrl(source_uuid, 0, 2, null, null));
    System.out.println("CKAN response in: " + ((double) (System.currentTimeMillis() -start)/1000));
    return getLastMeasurementImpl(ckan);
  }

  /**
   * Internal impl to getLastMeasurement.
   * 
   * @param ckan
   * @return
   * @throws IOException
   * @throws ParseException 
   */
  private DeviceData getLastMeasurementImpl(CkanResponse ckan) throws IOException, ParseException {
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
    else if (o instanceof Float)
      res = new Double((Float) o);
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

  private String formatDate(String date, String time) throws ParseException {
    if (date==null || time==null)
      throw new RuntimeException("Cannot format date with empty string");
    return DateFormatter.formatToOrganicityDate(date.substring(0,11) + time);
  }

  public static boolean USE_DEMO_DATA = false;
  public static AakDataService createService() {
    if (USE_DEMO_DATA)
      return new AakDataService(new AakCkanServiceDemoData());
    return new AakDataService(new AakCkanService());
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