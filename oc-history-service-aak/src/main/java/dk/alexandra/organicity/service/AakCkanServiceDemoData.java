package dk.alexandra.organicity.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dk.alexandra.organicity.model.ckan.CkanResponse;
import dk.alexandra.organicity.model.ckan.Field;
import dk.alexandra.organicity.model.ckan.Result;

public class AakCkanServiceDemoData extends AakCkanService {
	  public CkanResponse getCkanEntities() throws IOException {
		    return super.getCkanResponse(getUrl("_table_metadata", 0, 200, null, null));
		  }

	  public CkanResponse getCkanResponse(String url) throws IOException {
		  return getTestCkanResponse();
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
	    r.put("GPSLongitude_2", new Double(10.18));
	    r.put("Indlaes_Vejning_ID", new Integer(2246347));
	    r.put("Opd_Dato", "2013-07-01 00:00:00.0");
	    r.put("Dato", "2013-07-01 00:00:00.0");      
	    r.put("Container_Vejning_Faktura_Opstil_id", null);      
	    r.put("Tid", "09:23:10");      
	    r.put("Vejning", new Float(9.5));      
	    r.put("_id", new Integer(1035192));            
	    r.put("FrivaegtKg", new Float(0.0));
	    r.put("GPSLatitude_2", new Double(56.17));
	    ckan.result.records.add(r);
	    r = new HashMap<String,Object>();
	    r.put("Opd_Init", "SYSRS");
	    r.put("Container_Vejning_ID", new Integer(1043508));
	    r.put("GPSLongitude_2", new Double(10.03));
	    r.put("Indlaes_Vejning_ID", new Integer(2246348));
	    r.put("Opd_Dato", "2013-07-01 00:00:00.0");
	    r.put("Dato", "2013-07-01 00:00:00.0");      
	    r.put("Container_Vejning_Faktura_Opstil_id", null);      
	    r.put("Tid", "09:23:20");      
	    r.put("Vejning", new Float(20.0));      
	    r.put("_id", new Integer(1035193));            
	    r.put("FrivaegtKg", new Float(0.0));
	    r.put("GPSLatitude_2", new Double(56.21));
	    ckan.result.records.add(r);
	    return ckan;
	  }

}
