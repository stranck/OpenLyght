package dmx.OpenLyght.Utils;

import org.json.JSONObject;

public class Variable {
	
	private String value;
	private String name;
	
	public Variable(JSONObject data){
		value = data.getString("value");
		name = data.getString("name");
		System.out.println("--> " + toString());
	}
	
	public String apply(String s){
		//System.out.println(s.replaceAll("%" + name + "%", value));
		return s.replaceAll("%" + name + "%", value);
	}
	
	@Override
	public String toString(){
		return "{\"name\" : \"" + name +  "\",\t\"value\" : \"" + value + "\"}";
	}
}
