package dk.aarhus.organicity.service;

import java.text.ParseException;
import java.util.Map;

import dk.aarhus.organicity.util.DateFormatter;

/**
 * Wrapper for different Aak data sources (records in CKAN speak)
 * @author neerbek
 *
 */
public class AakRecycleDataSource {
  public static final int SourceUuidOffset = 806200; //HACK: offset for source id to be unique - for now this is unique
  public static String SourceUuid = "76d1ee61-8062-42d9-b417-fac75998f5c3"; 
  //Expected fieldnames from CKAN
  public enum FieldName { Opd_Init, Container_Vejning_ID, GPSLongitude_2, Indlaes_Vejning_ID, Opd_Dato,
    Dato, Container_Vejning_Faktura_Opstil_id, Tid, Vejning, _id, FrivaegtKg, GPSLatitude_2 }

  String key;
  double latitude; //using float because this is what we get from ckan
  double longitude;
  Map<String, Object> lastMeasurements = null;
  Map<String, Object> lastLastMeasurements = null;

  public AakRecycleDataSource(Map<String, Object> rec) {
    String s = FieldName.GPSLatitude_2.toString();
    latitude = (Double) rec.get(s);
    key = rec.get(s) + ":";
    s = FieldName.GPSLongitude_2.toString();
    longitude = (Double) rec.get(s);
    key += rec.get(s); //using values rather than longitude because of possible conversion problems 
  }

  public String getKey()
  {
    return key;
  }

  public void updateMeasurements(Map<String, Object> rec)
  {
    if (lastMeasurements==null)
      lastMeasurements = rec;
    else if (lastLastMeasurements==null)
      lastLastMeasurements = rec;
    //if both last and lastLast are filled then do nothing
  }

  public static String getLongitudeFromKey(String key2) {
    int i = key2.indexOf(':');
    return key2.substring(i+1);
  }

  public static String getLatitudeFromKey(String key2) {
    int i = key2.indexOf(':');
    return key2.substring(0,i);
  }

  public static String getMeasurementDate(Map<String, Object> measurement) throws ParseException {
    String date = (String) measurement.get(FieldName.Opd_Dato.toString());
    String time = (String) measurement.get(FieldName.Tid.toString());
    if (date==null || time==null)
      throw new ParseException("Cannot format date with empty string", 0);
    return DateFormatter.formatToOrganicityDate(date.substring(0,11) + time);
  }

}
