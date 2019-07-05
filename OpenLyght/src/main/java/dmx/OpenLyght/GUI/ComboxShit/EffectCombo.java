package dmx.OpenLyght.GUI.ComboxShit;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import dmx.OpenLyght.App;
import dmx.OpenLyght.Utils.Effect;

public class EffectCombo {
	//Shame on who invented the JComboBox
	private ArrayList<Effect> effect = new ArrayList<Effect>();
	private String displayName = "";
	
	public EffectCombo(JSONArray arr, int n){
		for(int i = 0; i < n; i++){
			int a = i % arr.length();
			JSONObject effect = arr.getJSONObject(a);
			this.effect.add(App.utils.getEffectByName(effect.getString("effectName")));
			if(i < arr.length())
				displayName += effect.getString("name") + " / ";
		}
		displayName = displayName.substring(0, displayName.length() - 3);
	}
	
	public Effect getEffect(int i){
		return effect.get(i);
	}
	
	@Override
	public String toString(){
		return displayName;
	}
}
