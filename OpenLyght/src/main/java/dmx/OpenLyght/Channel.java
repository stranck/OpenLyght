package dmx.OpenLyght;

import java.util.ArrayList;

import dmx.OpenLyght.Utils.EmptyChannelModifier;

public class Channel {
	
	private boolean reloadReported = false;
	private short originalValue = 0;
	private short value = 0;
	private ArrayList<ChannelModifiers> modifiers = new ArrayList<ChannelModifiers>();
	private EmptyChannelModifier emptyModifier = new EmptyChannelModifier();
	private String description = "";
	
	public Plugin test;
	
	public synchronized boolean reloadValue(){
		if(reloadReported){
			value = originalValue;
			int i = 0;
			for(ChannelModifiers cm : modifiers){
				//if(!description.isEmpty()) System.out.println(value);
				//System.out.println("Reloading: " + description + "(" + hashCode() + ")" + "@" + value + " " + cm + " " + cm.hashCode());
				value = cm.getChannelValue(value, i++, this);
			}
			//if(!description.isEmpty()) System.out.println("reloaded: " + description + "(" + hashCode() + ")" + "@" + value);
			//System.out.println("reloaded: " + description + "(" + hashCode() + ")" + "@" + value);
			reloadReported = false;
			return true;
		}
		return false;
	}
	
	public synchronized void removeChannelModifier(ChannelModifiers cm){
		for(int i = 0; i < modifiers.size(); i++)
			if(modifiers.get(i) == cm) modifiers.set(i, emptyModifier);
		reloadReported = true;
	}
	public synchronized int addChannelModifier(ChannelModifiers cm){
		int index = -1;
		for(int i = 0; i < modifiers.size() && index < 0; i++)
			if(modifiers.get(i) == emptyModifier) {
				modifiers.set(i, cm);
				index = i;
				System.out.println(hashCode() + " Replaced channel modifier " + cm + " at: " + i);
			}
		if(index < 0){
			modifiers.add(cm);
			index = modifiers.size() - 1;
			reloadReported = true;
			System.out.println(hashCode() + " Added channel modifier " + cm + " at: " + (modifiers.size() - 1));
		}
		reloadReported = true;
		return index;
	}
	public synchronized void addChannelModifier(ChannelModifiers cm, int index){
		if(index < 0) addChannelModifier(cm);
		else if(index < modifiers.size()){
			
			if(modifiers.get(index) == emptyModifier) modifiers.set(index, cm);
				else modifiers.add(index, cm);
			System.out.println(hashCode() + " Added channel modifier " + cm + " at: " + index);
			
		} else {
			for(int i = modifiers.size(); i <= index - 1; i++)
				modifiers.add(emptyModifier);
			modifiers.add(cm);
			System.out.println(hashCode() + " Added channel modifier " + cm + " at: " + (modifiers.size() - 1));
		}
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	public void setOriginalValue(short n){
		originalValue = n;
		reloadReported = true;
	}
	
	public void reportReload(){
		reloadReported = true;
	}
	
	public short getDMXValue(){
		short value = this.value;
		if(value < 0) value = 0;
		if(value > 255) value = 255;
		return value;
	}
	
	public short getValue(){
		return value;
	}
	public String getDescription(){
		return description;
	}
}
