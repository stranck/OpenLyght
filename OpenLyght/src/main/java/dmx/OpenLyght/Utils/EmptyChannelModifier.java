package dmx.OpenLyght.Utils;

import dmx.OpenLyght.Channel;
import dmx.OpenLyght.ChannelModifiers;

public class EmptyChannelModifier implements ChannelModifiers {

	@Override
	public short getChannelValue(short originalValue, int index, Channel ch) {
		return originalValue;
	}

}
