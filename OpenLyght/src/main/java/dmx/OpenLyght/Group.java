package dmx.OpenLyght;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class Group {
	 private ArrayList<Channel> channels = new ArrayList<Channel>();
	 private String name, description;
	 private boolean status = false;
	 
	 public Group(JSONObject data){
		 name = data.getString("name");
		 if(data.has("description")) description = data.getString("description");
		 status = data.getBoolean("defaultStatus");
		 
		 JSONArray channels = data.getJSONArray("channels");
		 for(int i = 0; i < channels.length(); i++)
			 this.channels.add(App.utils.getChannel(channels.getString(i)));
		 
		 System.out.println(toString() + " loaded");
	 }
	 
	 public Group(ArrayList<Channel> channels, String name){
		 this.channels = channels;
		 this.name = name;
	 }
	 
	 public void setDescription(String description){
		 this.description = description;
	 }
	 public void setStatus(boolean status){
		 this.status = status;
	 }
	 
	 public void merge(Group g){
		 //System.out.println("Merging" + g.toString());
		 channels.addAll(g.channels);
	 }
	 
	 public ArrayList<Channel> getChannels(){
		 return channels;
	 }
	 public String getName(){
		 return name;
	 }
	 public String getDescription(){
		 return description;
	 }
	 public boolean getStatus(){
		 return status;
	 }
	 
	 @Override
	 public String toString(){
		 return "{name: " + name + ", description: " + description + ", channelNO: " + channels.size() + "}";
	 }
}
