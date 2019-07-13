package dmx.OpenLyght.Utils;

import dmx.OpenLyght.BasicChannel;
import dmx.OpenLyght.Channel;
import dmx.OpenLyght.Fixture;
import dmx.OpenLyght.Patch;

public class EffectChannel extends BasicChannel {
	private short startIncSpeed = 256, incInterval, incAmountSpeed;
	private int inc;
	
	public EffectChannel(int value, boolean smooth, Channel ch) {
		super(value, smooth, ch);
		
		Fixture f = ch.getOriginalFixture();
		if(f != null){
			Patch p = f.getPatch();
			if(p != null){
				String amount = p.getAttributeByName("Effect AmountIncreaser amount " + ch.getDescription());
				String startSpeed = p.getAttributeByName("Effect AmountIncreaser startSpeed " + ch.getDescription());
				if(amount != null && startSpeed != null){
					incAmountSpeed = Short.parseShort(amount);
					startIncSpeed = Short.parseShort(startSpeed);
					incInterval = (short) (255 - startIncSpeed);
				}
			}
		}
	}

	public void updateIncAmount(short speed, short value){
		if(startIncSpeed < 256 && speed > startIncSpeed && value > 0)//(speed - start) : incInterval = inc : incAmount
			inc = (speed - startIncSpeed) * incAmountSpeed / incInterval * value / 0xFF;
		else inc = 0;
		//System.out.println(inc + " " + speed + " " + startIncSpeed);
	}
	
	public int getInc(){
		return inc;
	}
}
