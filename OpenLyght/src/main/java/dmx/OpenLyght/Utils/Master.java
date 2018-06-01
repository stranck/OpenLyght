package dmx.OpenLyght.Utils;

import java.util.ArrayList;

import dmx.OpenLyght.*;

public class Master implements ChannelModifiers {
	
	private int diff = 0xFF, min = 0, enableLimit = Integer.MIN_VALUE;
	private int mode; //0 = absolute, 1 = relative, 2 = relative remove, 3 = relative add, 4 = add, 5 = remove, 6 = biggerThan, others = nothing
	private boolean invertValue;
	private ArrayList<Channel> channels;
	private Channel value;
	
	private short val, masterValue;
	
	public Master(Channel sourceValue, ArrayList<Channel> channels, int mode){
		this.channels = channels;
		this.mode = mode;
		value = sourceValue;
		System.out.println("Creating master. Source index: " + value.addChannelModifier(this) + " id: " + hashCode());
		for(Channel c : channels)
			c.addChannelModifier(this);
	}
	
	public Master(Channel sourceValue, ArrayList<Channel> channels, int mode, int sourceIndex, int modifierIndex){
		this.channels = channels;
		this.mode = mode;
		value = sourceValue;
		System.out.println("Creating master. Source index: " + sourceIndex + " id: " + hashCode());
		value.addChannelModifier(this, sourceIndex);
		for(Channel c : channels)
			c.addChannelModifier(this, modifierIndex);
	}
	
	public void setInvertValue(boolean b){
		invertValue = b;
	}
	
	public void setEnableLimit(int limit){
		enableLimit = limit;
	}
	
	public void setLimits(int min, int max){
		this.min = min;
		diff = max - min;
	}
	
	@Override
	public short getChannelValue(short originalValue, int index, Channel ch) {
		//newValue : diff = originalValue : 0xFF   
		val = originalValue;
		masterValue = this.value.getValue();
		//System.out.println(originalValue + " " + masterValue);
		try{
			if(ch == this.value){
				for(Channel c : channels) {
					//System.out.println("MASTER " + hashCode() + ": Reported reload for " + c.hashCode());
					c.reportReload();
				}
				//System.out.println("MASTER " + hashCode() + ": All reload reported");
			} else if(masterValue >= enableLimit) {
				if(invertValue) val = (short) (0xFF - val);
				switch(mode){
					case 0: {
						val = (short) (masterValue * diff / 0xFF + min);
						break;
					}
					case 1: {
						val = (short) (originalValue * masterValue / 0xFF * diff / 0xFF + min);
						break;
					}
					case 2: {
						//System.out.println(value + " " + originalValue + " " + masterValue + " " + ch.hashCode());
						val += masterValue * diff / 0xFF + min;
						break;
					}
					case 3: {
						//System.out.println("MASTER TYPE: 3 " + ch.hashCode() + " " + hashCode());
						val -= masterValue * diff / 0xFF + min;
						break;
					}
					case 4: {
						val = (short) (val * diff / 0xFF + masterValue);
						break;
					}
					case 5: {
						val = (short) (val * diff / 0xFF - masterValue);
						break;
					}
					case 6: {
						int v = masterValue * diff / 0xFF + min;
						//System.out.println("MASTER TYPE: 6 " + v + " " + val + " " + ch.hashCode());
						if(v > val) val = (short) v;
						break;
					}
				}
				//System.out.println("MASTER: " + value + " " + originalValue + " " + masterValue + " " + mode + " " + ch.hashCode() + " " + hashCode());
			} //else System.out.println("MASTER under enable limit: " + value + " " + enableLimit + " " + hashCode() + " " + ch.hashCode());
		} catch (Exception e){
			e.printStackTrace();
		}
		//System.out.println("MASTER DONE " + mode + " " + hashCode() + " " + ch.hashCode());
		return val;
	}
}
