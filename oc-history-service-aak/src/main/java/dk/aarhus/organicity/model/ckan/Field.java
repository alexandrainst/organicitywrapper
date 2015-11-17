package dk.aarhus.organicity.model.ckan;

public class Field {
	public String type;
	public String id;

	public Field(String type, String id) {
		this.type = type;
		this.id = id;
	}
	public Field() { } //for json (de-)serialization
}
