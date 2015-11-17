package dk.aarhus.organicity.model.organicity;

import java.util.ArrayList;
import java.util.List;

public class DeviceReadings {
  public int entity_id;
  public int attribute_id;
  public String function;
  public String rollup;
  public String from;
  public String to;
  public List<List<Object>> readings = new ArrayList<List<Object>>(); //This is a list of <Date,Value> readings. Since we do not want attribute names (per the Organicity API) on the list elements we have to use this strange construct to make it work.
  
  public DeviceReadings() { }
  public DeviceReadings(int sourceId, int attributeId) {
    this.entity_id = sourceId;
    this.attribute_id = attributeId;
  }
  
  public void addReading(String date, Double value){
    List<Object> reading = new ArrayList<Object>(2);
    reading.add(date);
    reading.add(value);
    readings.add(reading);
  }
}
