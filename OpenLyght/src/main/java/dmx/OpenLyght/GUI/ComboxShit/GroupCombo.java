package dmx.OpenLyght.GUI.ComboxShit;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import dmx.OpenLyght.App;
import dmx.OpenLyght.Group;

public class GroupCombo {
	//"oh no, again that fucking Combobox" -me 3/5/2018 19:29
	private ArrayList<Group> group = new ArrayList<Group>();
	private String displayName = "";
	
	public GroupCombo(JSONArray arr, int n){
		for(int i = 0; i < n; i++){
			int a = i % arr.length();
			JSONObject group = arr.getJSONObject(a);
			this.group.add(App.utils.getGroup(group.getString("groupName")));
			if(i < arr.length())
				displayName += group.getString("name") + " / ";
		}
		displayName = displayName.substring(0, displayName.length() - 3);
	}
	
	public Group getGroup(int i){
		return group.get(i);
	}
	
	@Override
	public String toString(){
		return displayName;
	}
}
