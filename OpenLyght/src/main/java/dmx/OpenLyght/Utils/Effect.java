package dmx.OpenLyght.Utils;

import dmx.OpenLyght.BasicChannel;
import dmx.OpenLyght.Channel;
import dmx.OpenLyght.Group;

public interface Effect {
	
	public double getValue(int phase, BasicChannel bc);
	
	public String getName();
	
	public void removeGroup(Group g);
	public void setGroup(Group g);
	
	public void setOriginalValue(short value, Channel ch);
}
