package dmx.OpenLyght.GUI.ComboxShit;

import dmx.OpenLyght.Utils.Effect;

public class EffectCombo {
	//Shame on who invented the JComboBox
	private Effect effect;
	private String displayName;
	
	public EffectCombo(Effect effect, String displayName){
		this.effect = effect;
		this.displayName = displayName;
	}
	
	public Effect getEffect(){
		return effect;
	}
	
	@Override
	public String toString(){
		return displayName;
	}
}
