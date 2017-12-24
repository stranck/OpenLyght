package dmx.OpenLyght.Utils;

import java.util.ArrayList;

import dmx.OpenLyght.Channel;

public class Fader {

	private ArrayList<ChannelInterval> ch = new ArrayList<ChannelInterval>();
	//private int priority;
	
	public Fader(Channel c, int min, int max){
		ch.add(new ChannelInterval(c, min, max));
		/*this.priority = priority;
		if(priority != 0) c.addChannelModifier(this);*/
	}
	
	public Fader(ArrayList<ChannelInterval> c){
		ch = c;
		/*this.priority = priority;
		for(ChannelInterval ci : c)
			if(priority != 0) ci.getChannel().addChannelModifier(this);*/
	}

	public void setValue(short value){
		for(ChannelInterval ci : ch){
			Channel c = ci.getChannel();
			//System.out.println("Setting value: " + value + " " + c.hashCode() + " " + hashCode());
			c.setOriginalValue(ci.getChannelValue(value));
			c.reportReload();
		}
	}

	/*@Override
	public short getChannelValue(short originalValue, int index, Channel ch) {
		if(priority != 0)
		return originalValue;
	}
	
	private ChannelInterval getChannelInterval(Channel ch){
		ChannelInterval ci = null;
		for(ChannelInterval c : this.ch)
			if(c.getChannel() == ch) {
				ci = c;
				break;
			}
		return ci;
	}*/
}
