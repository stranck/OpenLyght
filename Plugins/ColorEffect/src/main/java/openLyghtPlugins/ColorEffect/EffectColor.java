package openLyghtPlugins.ColorEffect;

import java.awt.Color;

import org.json.JSONObject;

public class EffectColor {
	private String label;
	private Color labelColor;
	private Color rgbColor;
	
	public EffectColor(JSONObject data){
		label = data.getString("label");
		
		JSONObject label = data.getJSONObject("labelColor");
		labelColor = new Color(label.getInt("red"), label.getInt("green"), label.getInt("blue"));
		//JSONObject rgb = data.getJSONObject("rgb");
		rgbColor = new Color(Integer.decode(data.getString("rgb")));
		//rgbColor = new Color(rgb.getInt("red"), rgb.getInt("green"), rgb.getInt("blue"));
	}
	
	@Override
	public String toString(){
		return label;
	}
	public Color getColorLabel(){
		return labelColor;
	}
	public Color getRgbColor(){
		return rgbColor;
	}
}
