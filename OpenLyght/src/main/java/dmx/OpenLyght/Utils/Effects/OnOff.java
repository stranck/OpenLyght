package dmx.OpenLyght.Utils.Effects;

import java.util.ArrayList;

import dmx.OpenLyght.BasicChannel;
import dmx.OpenLyght.Channel;
import dmx.OpenLyght.Utils.Effect;

public class OnOff implements Effect {

	@Override
	public double getValue(int phase, BasicChannel bc) {
		double n = -1;
		if(phase > 180) n = 1;
		return n;
	}

	@Override
	public String getName() {
		return "OnOff";
	}

	@Override
	public void setGroup(ArrayList<Channel> activeGroup) {}
	@Override
	public void removeGroup(ArrayList<Channel> activeGroup) {}
	@Override
	public void setOriginalValue(short value, Channel ch) {}
	@Override
	public void setAmount(short amount) {}
}
