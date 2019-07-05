package dmx.OpenLyght.GUI.ComboxShit;

import java.util.ArrayList;

import org.json.JSONArray;

public class Numbers {
	//Do I really need a Numbers class because a JComboBox can't fucking accept a simple fucking String as Item?!?
	private ArrayList<Integer> number = new ArrayList<Integer>();
	private String displayName = "";
	
	public Numbers(JSONArray arr, int n){
		for(int i = 0; i < n; i++){
			int a = i % arr.length();
			int nbr = arr.getInt(a);
			number.add(nbr);
			if(i < arr.length())
				displayName += nbr + " / ";
		}
		displayName = displayName.substring(0, displayName.length() - 3);
	}
	
	public Numbers(String displayName){
		this.displayName = displayName;
	}
	
	public int getNumber(int i){
		return number.get(i);
	}
	
	@Override
	public String toString(){
		if(displayName.equals("")) 
			return "" + number.get(0);
		return displayName;
	}
}
