package dmx.OpenLyght.Utils.Effects;

import java.util.ArrayList;

import dmx.OpenLyght.BasicChannel;
import dmx.OpenLyght.Channel;
import dmx.OpenLyght.Utils.Effect;

public class Sin180 implements Effect {

	@Override
	public double getValue(int phase, BasicChannel bc) {
		return Math.sin(Math.toRadians(phase)) * -1;
	}

	@Override
	public String getName() {
		return "Sin180";
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
