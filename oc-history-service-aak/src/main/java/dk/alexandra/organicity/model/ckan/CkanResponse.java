package dk.alexandra.organicity.model.ckan;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CkanResponse {
	public boolean success; //was the request succesfull
	public Result result; //the result 
	public int total; //number of rows in db
	
	public CkanResponse() { } //for json (de-)serialization

}
