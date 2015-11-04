package dk.alexandra.organicity.model.ckan;

public class Field {
	enum Type { float8, text, int4, name, oid} //TODO: this list should be extended
	
	public static Type string2type(String t) {
		//TODO: Organicity should only support some known subset - rest should map to text/string
		if (t.equals("name"))
			return Type.text;
		if (t.equals("oid"))
			return Type.int4;
		
		for (Type tt : Type.values())
			if (tt.toString().equals(t))
				return tt;
		return null;
	}

	public static String type2string(Type tt) {
		return tt.toString();
	}
	
	public Type type;
	public String id;

	public Field(Type type, String id) {
		this.type = type;
		this.id = id;
	}
}
