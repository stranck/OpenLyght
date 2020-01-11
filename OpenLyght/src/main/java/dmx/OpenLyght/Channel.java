package dmx.OpenLyght;

import java.util.ArrayList;

public class Channel {
	
	private int modifierIndex;
	private boolean reloadReported = false, invertValue = false;
	private short originalValue = 0, previousValue, value = 0, dmxValue;
	private ArrayList<ChannelModifiers> modifiers = new ArrayList<ChannelModifiers>();
	private String description = "";
	private Fixture origianlFx;
	
	public Channel(String name){
		description = name;
	}
	
	public synchronized boolean reloadValue(){
		if(reloadReported){
			previousValue = value;
			value = originalValue;
			modifierIndex = 0;
			//System.out.println("Start reload: " + description + "(" + hashCode() + ")" + "@" + value + " " + origianlFx/*+ cm.hashCode()*/);
			for(ChannelModifiers cm : modifiers){
				//if(!description.isEmpty()) System.out.println(value);
				//System.out.println("Reloading: " + description + "(" + hashCode() + ")" + "@" + value + " " + cm + " " + origianlFx/*+ cm.hashCode()*/);
				if(cm != null) value = cm.getChannelValue(value, modifierIndex++, this);
			}
			//if(!description.isEmpty()) System.out.println("reloaded: " + description + "(" + hashCode() + ")" + "@" + value);
			if(invertValue) value = (short) (255 - value);
			reloadReported = false;
			//System.out.println("reloaded: " + description + "(" + hashCode() + ")" + "@" + value + ";" + dmxValue + " " + origianlFx);
			if(value != previousValue){
				dmxValue = value;
				if(dmxValue < 0) dmxValue = 0;
				if(dmxValue > 255) dmxValue = 255;
				return true;
			}
		}
		return false;
	}
	
	public synchronized void removeChannelModifier(ChannelModifiers cm){
		for(int i = 0; i < modifiers.size(); i++)
			if(modifiers.get(i) == cm) modifiers.set(i, null);
		reloadReported = true;
	}
	public synchronized int addChannelModifier(ChannelModifiers cm){
		int index = -1;
		for(int i = 0; i < modifiers.size() && index < 0; i++)
			if(modifiers.get(i) == null) {
				modifiers.set(i, cm);
				index = i;
				//System.out.println(hashCode() + " Replaced channel modifier " + cm + " at: " + i);
			}
		if(index < 0){
			modifiers.add(cm);
			index = modifiers.size() - 1;
			//System.out.println(hashCode() + " Added channel modifier " + cm + " at: " + (modifiers.size() - 1));
		}
		reloadReported = true;
		return index;
	}
	public synchronized void addChannelModifier(ChannelModifiers cm, int index){
		if(index < 0) addChannelModifier(cm);
		else if(index < modifiers.size()){
			
			if(modifiers.get(index) == null) modifiers.set(index, cm);
				else modifiers.add(index, cm);
			//System.out.println(hashCode() + " Added channel modifier " + cm + " at: " + index);
			
		} else {
			for(int i = modifiers.size(); i <= index - 1; i++)
				modifiers.add(null);
			modifiers.add(cm);
			//System.out.println(hashCode() + " Added channel modifier " + cm + " at: " + (modifiers.size() - 1));
		}
	}
	public synchronized int getModifiersSize(){
		return modifiers.size();
	}
	
	public void setInvert(boolean invert){
		invertValue = invert;
	}
	public void setDescription(String description){
		this.description = description;
	}
	public void setOriginalFixture(Fixture f){
		origianlFx = f;
	}
	public void setOriginalValue(short n){
		originalValue = n;
		reloadReported = true;
	}
	
	public void reportReload(){
		reloadReported = true;
	}
	
	public short getDMXValue(){
		return dmxValue;
	}
	
	public short getValue(){
		return value;
	}
	public String getDescription(){
		return description;
	}
	public String getChannelFinalName(){
		String sp[] = description.split(":");
		return sp[sp.length - 1];
	}
	public Fixture getOriginalFixture(){
		return origianlFx;
	}
	
	@Override
	public String toString(){
		return "Channel: " + getDescription() + " @ " + hashCode(); 
	}
}
