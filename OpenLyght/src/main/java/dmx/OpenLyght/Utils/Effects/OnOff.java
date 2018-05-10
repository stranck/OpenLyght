package dmx.OpenLyght.Utils.Effects;

import dmx.OpenLyght.BasicChannel;
import dmx.OpenLyght.Channel;
import dmx.OpenLyght.Group;
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
	public void setGroup(Group g) {}
	@Override
	public void removeGroup(Group g) {}
	@Override
	public void setOriginalValue(short value, Channel ch) {}
}
