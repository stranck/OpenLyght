package openLyghtPlugins.ColorEffect;

import java.awt.Color;

import org.json.JSONObject;

public class EffectColor {
	private String label;
	private int value;
	private Color color;
	
	public EffectColor(JSONObject data){
		label = data.getString("label");
		value = (short) data.getInt("value");
		
		JSONObject rgb = data.getJSONObject("rgb");
		color = new Color(rgb.getInt("red"), rgb.getInt("green"), rgb.getInt("blue"));
	}
	
	@Override
	public String toString(){
		return label;
	}
	public int getValue(){
		return value;
	}
	public Color getColor(){
		return color;
	}
}
