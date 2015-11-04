package dk.alexandra.organicity.model.ckan;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParser;

public class CkanResponse {
	public boolean success;
	public List<Field> fields;
	public List<Map<String, Object>> records;

	
	public CkanResponse(boolean success, List<Field> fields,
			List<Map<String, Object>> records) {
		this.success = success;
		this.fields = fields;
		this.records = records;
	}
	
}
