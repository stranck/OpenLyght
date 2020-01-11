package openLyghtPlugins.commandLine;

import dmx.OpenLyght.Channel;
import dmx.OpenLyght.ChannelModifiers;

public class Selection implements ChannelModifiers {
	private boolean selected, inverted, priority, absolute;
	private short updatedValue;
	private Channel channel;

	public Selection(Channel ch){
		channel = ch;
		updatedValue = channel.getValue();
	}
	
	private void updateChannel(){
		if(priority)
			channel.reportReload();
		else
			channel.setOriginalValue(modifyValue((short) 0));
	}
	public void editValue(short value, boolean abs){
		if(abs)
			updatedValue = value;
		else
			updatedValue += value;
		updateChannel();
	}
	
	public void clear(){
		channel.removeChannelModifier(this);
		channel = null;
	}
	public void deselect(){
		selected = false;
	}
	public void select(String channelName){
		selected = true;
		inverted = channelName.charAt(0) == '!';
		channelHook();
	}
	
	public void changePriority(boolean priority){
		this.priority = priority;
		channelHook();
	}
	public void changeMode(boolean mode){
		absolute = mode;
		updateChannel();
	}
	
	@Override
	public short getChannelValue(short originalValue, int index, Channel ch) {
		if(priority)
			originalValue = modifyValue(originalValue);
		return originalValue;
	}
	
	@Override
	public String toString(){
		return "NAME: " + channel.getDescription()
		   + "\tFLAGS:" + (inverted ? " inv" : "") + (priority ? " prt" : "") + (absolute ? " abs" : "")
		   + "\tVALUES: final=" + channel.getValue() + " edit=" + updatedValue;
	}
	
	private short modifyValue(short value){
		if(absolute && !priority){
			value = updatedValue;
			if(inverted) value = (short) (255 - value);
		} else {
			if(inverted)
				value -= updatedValue;
			else
				value += updatedValue;
		}
		return value;
	}
	private void channelHook(){
		channel.removeChannelModifier(this);
		if(priority)
			channel.addChannelModifier(this, channel.getModifiersSize());
		else
			channel.setOriginalValue(modifyValue((short) 0));
		channel.reportReload();
	}
	
	public boolean isSelected(){
		return selected;
	}
	public boolean isThisSelection(String name){
		return channel.getDescription().equals(name);
	}
	
	public static String convertName(String name){
		if(name.charAt(0) == '!' && name.length() > 1)
			return name.substring(1);
		return name;
	}
}
