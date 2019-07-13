package dmx.OpenLyght;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class Group {
	private ArrayList<Fixture> fixture = new ArrayList<Fixture>();
	private String name, description;
	private boolean status = false;
	
	public Group(JSONObject data){
		name = data.getString("name");
		if(data.has("description")) description = data.getString("description");
		status = data.getBoolean("defaultStatus");
		 
		JSONArray fixtures = data.getJSONArray("fixtures");
		for(int i = 0; i < fixtures.length(); i++)
			this.fixture.addAll(App.utils.getFixtures(fixtures.getString(i)));
		
		System.out.println(toString() + " loaded");
	}
	 
	public Group(ArrayList<Fixture> fixture, String name){
		this.fixture = fixture;
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
		fixture.addAll(g.fixture);
	}
	public Group clearDuplicates(){
		fixture = App.removeDuplicates(fixture);
		//System.out.println("rm " + fixture.size());
		return this;
	}
	
	/**
	 * Get channels by a ; separated arraylist of names
	 */
	/*public ArrayList<Channel> getChannelsByNames(String name){
		ArrayList<Channel> ch = new ArrayList<Channel>();
		String[] sp = name.replaceAll("\n", "").split(";");
		for(String s : sp)
			ch.addAll(getChannelsByName(s));
		return ch;
	}*/
	/**
	 * Get channels from an arraylist of names
	 */
	public ArrayList<Channel> getChannelsByNames(JSONArray names){
		ArrayList<Channel> ch = new ArrayList<Channel>();
		for(int i = 0; i < names.length(); i++)
			ch.addAll(getChannelsByName(names.getString(i)));
		return App.removeDuplicates(ch);
	}
	/**
	 * Get channels from an arraylist of names
	 */
	public ArrayList<Channel> getChannelsByNames(ArrayList<String> names){
		ArrayList<Channel> ch = new ArrayList<Channel>();
		for(String s : names)
			ch.addAll(getChannelsByName(s));
		return App.removeDuplicates(ch);
	}
	public ArrayList<Channel> getChannelsByName(String name){
		return Fixture.getChannelsByFullName(name, fixture);
	}
	
	public ArrayList<Fixture> getFixtures(){
		return fixture;
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
	public int size(){
		return fixture.size();
	}
	 
	@Override
	public String toString(){
		return "{name: " + name + ", description: " + description + ", fixtureNO: " + size() + "}";
	}
}
