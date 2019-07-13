package dmx.OpenLyght;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class Patch {
	private ArrayList<String> channels = new ArrayList<String>();
	private HashMap<String, String> attributes = new HashMap<String, String>();
	private String name;
	
	public Patch(JSONObject data){
		name = data.getString("name");
		
		JSONArray ch = data.getJSONArray("channels");
		for(int i = 0; i < ch.length(); i++){
			String s = ch.getString(i);
			if(s.equalsIgnoreCase("null")) channels.add(null);
				else channels.add(s);
		}
		
		JSONObject attrs = data.getJSONObject("attributes");
		String[] names = JSONObject.getNames(attrs);
		if(names != null) {
			for(String s : names){
				attributes.put(s.toUpperCase(), attrs.getString(s));
			}
		}
		System.out.println("..." + name + " loaded");
	}
	
	public String getAttributeByName(String name){
		/*for(int i = 0; i < attributes.length(); i++){
			JSONObject obj = attributes.getJSONObject(i);
			if(name.equalsIgnoreCase(obj.getString("key")))
				return obj.getString("value");
		}
		return null;*/
		return attributes.get(name.toUpperCase());
	}
	
	public int size(){
		return channels.size();
	}
	public String getName(){
		return name;
	}
	public String getChannel(int channel){
		return channels.get(channel);
	}
	public ArrayList<String> getChannels(){
		return channels;
	}
}
