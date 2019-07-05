package dmx.OpenLyght.GUI.ComboxShit;

import java.util.ArrayList;

import org.json.JSONArray;

public class CharacterZ {
	//Hope the person who made the JComboBox is dead.
	private ArrayList<Character> c = new ArrayList<Character>();
	private String displayName = "";
	
	public CharacterZ(JSONArray arr, int n){
		for(int i = 0; i < n; i++){
			int a = i % arr.length();
			char ch = arr.getString(a).charAt(0);
			c.add(ch);
			if(i < arr.length())
				displayName += decodeChar(ch) + " / ";
		}
		displayName = displayName.substring(0, displayName.length() - 3);
	}
	
	public CharacterZ(String displayName){
		this.displayName = displayName;
	}
	
	public char getChar(int i){
		return c.get(i);
	}
	
	private String decodeChar(char c){
		switch(c){
			case 'S': {
				return "< Bounce";
			}
			case 's': {
				return "> Bounce";
			}
			default: return c + "";
		}
	}
	
	@Override
	public String toString(){
		if(displayName.equals(""))
			return decodeChar(c.get(0));
		return displayName;
	}
}
