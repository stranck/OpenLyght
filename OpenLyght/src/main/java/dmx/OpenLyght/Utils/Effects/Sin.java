package dmx.OpenLyght.Utils.Effects;

import dmx.OpenLyght.Utils.Effect;

public class Sin implements Effect {

	@Override
	public double getValue(int phase) {
		return Math.sin(Math.toRadians(phase));
	}

	@Override
	public String getName() {
		return "Sin";
	}
}
