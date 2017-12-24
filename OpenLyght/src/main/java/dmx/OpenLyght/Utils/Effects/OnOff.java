package dmx.OpenLyght.Utils.Effects;

import dmx.OpenLyght.Utils.Effect;

public class OnOff implements Effect {

	@Override
	public double getValue(int phase) {
		double n = -1;
		if(phase > 270) n = 1;
		return n;
	}

	@Override
	public String getName() {
		return "OnOff";
	}

}
