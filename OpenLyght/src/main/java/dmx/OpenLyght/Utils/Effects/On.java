package dmx.OpenLyght.Utils.Effects;

import dmx.OpenLyght.Utils.Effect;

public class On implements Effect {

	@Override
	public double getValue(int phase) {
		return 1;
	}

	@Override
	public String getName() {
		return "On";
	}
}
