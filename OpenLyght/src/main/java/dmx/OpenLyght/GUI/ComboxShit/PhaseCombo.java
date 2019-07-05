package dmx.OpenLyght.GUI.ComboxShit;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class PhaseCombo {
	//I hate JComboBox I hate JComboBox I hate JComboBox I hate JComboBox I hate JComboBox I hate JComboBox
	private ArrayList<Integer> start = new ArrayList<Integer>(), end = new ArrayList<Integer>();
	private String displayName = "";
	
	public PhaseCombo(JSONArray arr, int n){
		for(int i = 0; i < n; i++){
			int a = i % arr.length();
			JSONObject phase = arr.getJSONObject(a);
			this.start.add(phase.getInt("start"));
			this.end.add(phase.getInt("end"));
			if(i < arr.length())
				displayName += getDisplayName(i) + " / ";
		}
		displayName = displayName.substring(0, displayName.length() - 3);
	}
	//phase.getInt("start"), phase.getInt("end")
	public PhaseCombo(String displayName){
		this.displayName = displayName;
	}
	
	public int getStart(int i){
		return start.get(i);
	}
	
	public int getEnd(int i){
		return end.get(i);
	}
	
	private String getDisplayName(int i){
		return start.get(0) + " THRU " + end.get(0);
	}
	
	@Override
	public String toString(){
		if(displayName.equals(""))
			return getDisplayName(0);
		return displayName;
	}
}
