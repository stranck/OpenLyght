package dmx.OpenLyght.GUI.ComboxShit;

import dmx.OpenLyght.Group;

public class GroupCombo {
	//"oh no, again that fucking Combobox" -me 3/5/2018 19:29
	private Group group;
	private String displayName;
	
	public GroupCombo(Group group, String displayName){
		this.group = group;
		this.displayName = displayName;
	}
	
	public Group getGroup(){
		return group;
	}
	
	@Override
	public String toString(){
		return displayName;
	}
}
