package dmx.OpenLyght.Utils;

import dmx.OpenLyght.Channel;

public class ChannelInterval {
	
	private Channel c;
	private int max, min;
	
	public ChannelInterval(Channel c, int min, int max){
		this.c = c;
		this.min = min;
		this.max = max;
	}
	
	public Channel getChannel(){
		return c;
	}
	
	public short getChannelValue(int value){
		short newValue = 0;
		if(value > max) 
			newValue = 255;
		else if(value > min) //(value - min) : (max - min) = newValue : 255
			newValue = (short) ((value - min) * 255 / (max - min));
		return newValue;
	}
}
