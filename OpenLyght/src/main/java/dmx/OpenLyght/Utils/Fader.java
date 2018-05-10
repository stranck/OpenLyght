package dmx.OpenLyght.Utils;

import java.util.ArrayList;

import dmx.OpenLyght.Channel;

public class Fader {

	private ArrayList<ChannelInterval> ch = new ArrayList<ChannelInterval>();
	
	public Fader(Channel c, int min, int max){
		ch.add(new ChannelInterval(c, min, max));
	}
	
	public Fader(ArrayList<ChannelInterval> c){
		ch = c;
	}

	public void setValue(short value){
		for(ChannelInterval ci : ch){
			Channel c = ci.getChannel();
			c.setOriginalValue(ci.getChannelValue(value));
			c.reportReload();
		}
	}
}
