package dmx.OpenLyght.Utils;

import java.util.ArrayList;

import org.json.JSONArray;

import dmx.OpenLyght.*;

public class Step {
	//private ArrayList<BasicChannel> bc = new ArrayList<BasicChannel>();
	private BasicChannel[] bc;
	
	public Step(JSONArray data, ArrayList<Channel> c){
		bc = new BasicChannel[c.size()];
		System.out.println("Adding basic channels to: " + hashCode());
		for(int i = 0; i < data.length(); i++){
			bc[i] = new BasicChannel(data.getJSONObject(i).getInt("value"), data.getJSONObject(i).getBoolean("smooth"), c.get(i));
		}
	}
	
	public Step(ArrayList<Channel> c){
		bc = new BasicChannel[c.size()];
		System.out.println("Adding basic channels to: " + hashCode());
		for(int i = 0; i < bc.length; i++)
			bc[i] = new BasicChannel(0, false, c.get(i));
	}
	
	public BasicChannel getBasicChannel(int i){
		return bc[i];
	}
	
	public BasicChannel getBasicChannel(Channel ch){
		return App.getBasicChannel(bc, ch);
	}
}
