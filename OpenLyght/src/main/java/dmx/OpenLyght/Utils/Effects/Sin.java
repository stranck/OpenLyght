package dmx.OpenLyght.Utils.Effects;

import java.util.ArrayList;

import dmx.OpenLyght.BasicChannel;
import dmx.OpenLyght.Channel;
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
	public void setGroup(ArrayList<Channel> activeGroup) {}
	@Override
	public void removeGroup(ArrayList<Channel> activeGroup) {}
	@Override
	public void setOriginalValue(short value, Channel ch) {}
	@Override
	public void setAmount(short amount) {}
}
