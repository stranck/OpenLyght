package dmx.OpenLyght.GUI.ComboxShit;

import dmx.OpenLyght.Group;
import dmx.OpenLyght.Utils.Effect;

public class EffectCombo {
	//Shame on who invented the JComboBox
	private Effect effect;
	private Group group;
	private String displayName;
	
	public EffectCombo(Effect effect, Group group, String displayName){
		this.effect = effect;
		this.group = group;
		this.displayName = displayName;
	}
	
	public Group getGroup(){
		return group;
	}
	
	public Effect getEffect(){
		return effect;
	}
	
	@Override
	public String toString(){
		return displayName;
	}
}
