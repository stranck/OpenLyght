package dmx.OpenLyght.Utils;

import java.util.ArrayList;

import dmx.OpenLyght.BasicChannel;
import dmx.OpenLyght.Channel;

public interface Effect {
	
	public double getValue(int phase, BasicChannel bc);
	
	public String getName();
	
	public void removeGroup(ArrayList<Channel> activeGroup);
	public void setGroup(ArrayList<Channel> activeGroup);
	
	public void setOriginalValue(short value, Channel ch);
	public void setAmount(short amount);
}
