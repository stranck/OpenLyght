package dmx.OpenLyght.Utils.Effects;

import dmx.OpenLyght.BasicChannel;
import dmx.OpenLyght.Channel;
import dmx.OpenLyght.Group;
import dmx.OpenLyght.Utils.Effect;

public class Sin implements Effect {

	@Override
	public double getValue(int phase, BasicChannel bc) {
		return Math.sin(Math.toRadians(phase));
	}

	@Override
	public String getName() {
		return "Sin";
	}
	
	@Override
	public void setGroup(Group g) {}
	@Override
	public void removeGroup(Group g) {}
	@Override
	public void setOriginalValue(short value, Channel ch) {}
}
