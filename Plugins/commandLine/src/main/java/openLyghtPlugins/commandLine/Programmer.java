package openLyghtPlugins.commandLine;

import java.util.ArrayList;

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
	
	public static Selection getSelection(String name, boolean canGenerate){
		Selection s = null;
		name = Selection.convertName(name);
		for(Selection sel : active)
			if(sel.isThisSelection(name)){
				s = sel;
				break;
			}
		if(s == null && canGenerate){
			s = new Selection(name);
			active.add(s);
		}
		return s;
	}
}
