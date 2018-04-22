package dmx.OpenLyght.Utils;

import org.json.JSONObject;

public class Variable {
	
	private String value;
	private String name;
	
	public Variable(JSONObject data){
		value = data.getString("value");
		name = data.getString("name");
	}
	
	public String apply(String s){
		//System.out.println("Checking variable" + s.replaceAll("%" + name + "%", value));
		//System.out.println(("\"{[:\t\n%" + name + "%]}\"").replaceAll("%" + name + "%", value));
		return s.replaceAll("%" + name + "%", value);
	}
}
