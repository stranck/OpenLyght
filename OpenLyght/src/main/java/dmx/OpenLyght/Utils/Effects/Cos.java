package dmx.OpenLyght.Utils.Effects;

import dmx.OpenLyght.Utils.Effect;

public class Cos implements Effect {

	@Override
	public double getValue(int phase) {
		return Math.cos(Math.toRadians(phase));
	}

	@Override
	public String getName() {
		return "Cos";
	}

}
