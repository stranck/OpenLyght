package dmx.OpenLyght.Utils.Effects;

import java.util.ArrayList;

import dmx.OpenLyght.App;
import dmx.OpenLyght.BasicChannel;
import dmx.OpenLyght.Channel;
import dmx.OpenLyght.Utils.Effect;

public class Random implements Effect {
	private static final java.util.Random RAND = new java.util.Random();
	private static final int PHASE_DIV = 30;
	private static final int RATE = 25;
	
	private BasicChannel[] bc;
	
	@Override
	public double getValue(int phase, BasicChannel bc) {
		if(bc != null){
			bc = App.getBasicChannel(this.bc, bc.getChannel());		
			phase /= PHASE_DIV;
			phase *= PHASE_DIV; //So there are 360/PHASE_DIV different phases instead of 360
			if(phase != bc.getValue()){
				bc.setValue((short) phase);
				bc.setSmooth(RAND.nextInt(100) < RATE);
			}
			return bc.getSmooth() ? 1 : -1;
		}
		return 0;
	}

	@Override
	public String getName() {
		return "Random";
	}
	
	@Override
	public void setGroup(ArrayList<Channel> activeGroup) {
		bc = new BasicChannel[activeGroup.size()];
		for(int i = 0; i < bc.length; i++)
			bc[i] = new BasicChannel(activeGroup.get(i));
	}
	@Override
	public void removeGroup(ArrayList<Channel> activeGroup) {
		bc = null;
	}
	@Override
	public void setOriginalValue(short value, Channel ch) {}
	@Override
	public void setAmount(short amount) {}
}
