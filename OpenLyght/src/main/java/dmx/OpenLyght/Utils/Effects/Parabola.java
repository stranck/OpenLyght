package dmx.OpenLyght.Utils.Effects;

import dmx.OpenLyght.Utils.Effect;

public class Parabola implements Effect {

	@Override
	public double getValue(int phase) {
		//System.out.println(Math.sin(Math.toRadians(phase / 2)));
		return Math.sin(Math.toRadians(phase / 2));
	}

	@Override
	public String getName() {
		return "Parabola";
	}
}
