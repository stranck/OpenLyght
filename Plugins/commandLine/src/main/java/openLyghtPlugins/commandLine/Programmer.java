package openLyghtPlugins.commandLine;

import java.util.ArrayList;

import dmx.OpenLyght.Channel;
import dmx.OpenLyght.Fixture;

public class Programmer {
	public static ArrayList<Selection> active = new ArrayList<Selection>();
	
	public static void clearSelection(){
		for(Selection s : active)
			s.deselect();
	}
	public static void clearAll(){
		for(Selection s : active)
			s.clear();
		active.clear();
	}
	
	public static void addSelection(String[] chs){
		for(String name : chs){
			Selection s = getSelection(name, true);
			s.select(name);
		}
	}
	
	public static void updateValue(String value){
		boolean abs = value.charAt(0) != '-' && value.charAt(0) != '+';
		short v = Short.parseShort(value);
		for(Selection s : active)
			if(s.isSelected())
				s.editValue(v, abs);
	}
	public static void updatePriority(boolean b){
		for(Selection s : active)
			if(s.isSelected())
				s.changePriority(b);
	}
	public static void updateAbsolute(boolean b){
		for(Selection s : active)
			if(s.isSelected())
				s.changeMode(b);
	}
	
	public static ArrayList<Selection> getSelection(String name, boolean canGenerate){
		ArrayList<Selection> s = new ArrayList<Selection>();
		name = Selection.convertName(name);
		for(Selection sel : active)
			if(sel.isThisSelection(name)){
				s.add(sel);
			}
		if(s.size() == 0 && canGenerate){
			ArrayList<Channel> chs = Fixture.getChannelsByFullName(name, Main.openLyght.fixtures);
			for(Channel c : chs){
				s = new Selection(c);
				active.add(s);
			}
		}
		return s;
	}
}
