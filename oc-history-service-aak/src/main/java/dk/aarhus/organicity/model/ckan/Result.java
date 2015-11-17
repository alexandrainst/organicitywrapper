package dk.aarhus.organicity.model.ckan;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {
	public List<Field> fields;
	public List<Map<String, Object>> records;

	public Result() { } //for json (de-)serialization
}
