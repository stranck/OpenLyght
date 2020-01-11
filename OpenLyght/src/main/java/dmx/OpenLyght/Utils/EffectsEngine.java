package dmx.OpenLyght.Utils;

import java.util.ArrayList;

import org.json.JSONObject;

import dmx.OpenLyght.App;
import dmx.OpenLyght.Channel;
import dmx.OpenLyght.ChannelModifiers;
import dmx.OpenLyght.Group;
import dmx.OpenLyght.Utils.Effects.Off;
 
public class EffectsEngine implements Runnable, ChannelModifiers {
	private Wait wait;
	private EffectChannel[] channels = new EffectChannel[0];
	private Channel speedChannel, amount, incAmount = null;
	private ArrayList<Channel> activeGroup = new ArrayList<Channel>();
	private Effect effect = new Off();
	private boolean relative, requestReload = true, reloadingGroup = false, enableAmountIncreaser;
	private char direction; //>, <, s (>), S (<)
	private short diff, mid, min, outValue;
	private int startPhase, endPhase, currentPhase, width, groups, blocks, wings, minSpeed;
	private int[] basePhase;
	
	public EffectsEngine(JSONObject data, Channel speed, Channel amount){
		min = (short) data.getInt("min");
		int max = data.getInt("max");
		mid = (short) ((max + min) / 2);
		diff = (short) Math.abs(Math.abs(max) - Math.abs(mid));
		
		width = data.getInt("width");
		minSpeed = data.getInt("minSpeed");
		relative = data.getBoolean("relative");
		enableAmountIncreaser = data.getBoolean("amountIncreaser");
		
		if(data.has("incAmountChannel")) {
			incAmount = App.utils.getChannel(data.getString("incAmountChannel"));
			if(data.has("incAmountChannelValue")) 
				incAmount.setOriginalValue((short) data.getInt("incAmountChannelValue"));
			else
				incAmount.setOriginalValue((short) 1);
		}
		
		speedChannel = speed;
		this.amount = amount;
		wait = new Wait();
	}
	
	public void reset(){
		currentPhase = 0;
	}
	
	public void setGroup(Group g, ArrayList<String> name){
		System.out.println("SIZE " + g.size());
		effect.removeGroup(activeGroup);
		activeGroup = g.getChannelsByNames(name);
		g.usedGroup();
		effect.setGroup(activeGroup);
		reloadingGroup = true;
		requestReload = true;
	}
	public void setEffect(Effect e){
		effect.removeGroup(activeGroup);
		effect = e;
		e.setGroup(activeGroup);
	}
	public void setDirection(char direction){
		this.direction = direction;
	}
	public void setPhaseStart(int start){
		startPhase = start;
		requestReload = true;
	}
	public void setPhaseEnd(int end){
		endPhase = end;
		requestReload = true;
	}
	public void setGroups(int groups){
		this.groups = Math.abs(groups);
		requestReload = true;
	}
	public void setBlocks(int blocks){
		this.blocks = Math.abs(blocks);
		requestReload = true;
	}
	public void setWings(int wings){
		System.out.println(wings);
		this.wings = wings;
		requestReload = true;
	}
	
	public void reloadEffect(){
		if(reloadingGroup){
			System.out.println("RELOADING GROUP");
			for(EffectChannel bc : channels)
				bc.getChannel().removeChannelModifier(this);
			
			channels = new EffectChannel[activeGroup.size()];
			for(int i = 0; i < channels.length; i++){
				Channel c = activeGroup.get(i);
				c.addChannelModifier(this);
				channels[i] = new EffectChannel(0, true, c);
			}
			
			reloadingGroup = false;
		}
		
		int phaseLength = channels.length;
		if(groups == 0){
			if(blocks > 1 && phaseLength >= blocks) phaseLength /= blocks;
			if(Math.abs(wings) > 1 && phaseLength >= Math.abs(wings)) phaseLength /= Math.abs(wings);
		} else phaseLength = groups;
		basePhase = new int[channels.length];
		
		int phaseDifference = (startPhase - endPhase) / phaseLength;
		for(int i = 0, phase = 0; i < phaseLength && i < basePhase.length; i++){
			basePhase[i] = phase + startPhase;
			phase += phaseDifference;
		}
		
		//System.out.println(Arrays.toString(basePhase) + " " + phaseLength + " " + phaseDifference);
		if(groups > 0) basePhase = applyGroups(groups, basePhase);
		if(blocks > 1) basePhase = applyBlocks(blocks, basePhase);
		if(Math.abs(wings) > 1) basePhase = applyWings(wings, basePhase);
		//System.out.println(Arrays.toString(basePhase) + " " + phaseLength + " " + phaseDifference);
		/*for(int i = 0; i < basePhase.length; i++){
			//resizePhase(basePhase[i]);
			System.out.println(basePhase[i]);
		}
		System.out.println("pl: " + phaseLength);*/
		requestReload = false;
	}
	
	@Override
	public void run() {
		currentPhase = 0;
		EffectChannel bc;
		int i, n, phase;
		short speed;
		
		while(!Thread.interrupted()) {
			try{
				if(requestReload) reloadEffect();
				speed = speedChannel.getValue();
				
				for(i = 0; i < basePhase.length; i++){
					bc = channels[i];
					if(enableAmountIncreaser)
						bc.updateIncAmount(speed, amount.getValue());
					
					phase = (currentPhase + basePhase[i]) % 360;
					if(phase < 0) phase += 360;
					
					//System.out.println(effect.getValue(width * phase / 360, bc) + " " + diff + " " + mid + " " + phase);
					if(phase > width) bc.setValue(min); //newPhase : width = phase : 360
						else bc.setValue((short) (effect.getValue(width * phase / 360, bc) * diff + mid));
					bc.getChannel().reportReload();
				}
				//System.out.println(amount.getValue() + "\t" + speedChannel.getValue() + "\t" + diff + "\t" + mid);
				if(speed > 8) {
					if(incAmount == null) n = 1;
						else n = incAmount.getValue();
					switch(direction){
						case '>': {
							if((currentPhase += n) >= 360) currentPhase = 0;
							break;
						}
						case '<': {
							if((currentPhase -= n) <= 0) currentPhase = 360;
							break;
						}
						case 's': {
							if((currentPhase += n) >= 360) direction = 'S';
							break;
						}
						case 'S': {
							if((currentPhase -= n) <= 0) direction = 's';
							break;
						}
					}
				}

				wait.sleep(256 + minSpeed - speed);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public short getChannelValue(short originalValue, int index, Channel ch) {
		outValue = originalValue;
		effect.setOriginalValue(originalValue, ch);
		short amt = amount.getValue();
		effect.setAmount(amt);
		try{
			EffectChannel ec = (EffectChannel) App.getBasicChannel(channels, ch);
			outValue = (short) (ec.getValue() * (amt + ec.getInc()) / 0xFF);
		}catch(Exception e) {}
		//System.out.println("Channel: " + value + " " + originalValue + " " + App.getBasicChannel(channels, ch).getValue() + " " + amount.getValue() + " " + ch.hashCode());
		//System.out.println(outValue + " " + originalValue);
		if(relative) outValue += originalValue;
		return outValue;
	}
	
	private int[] applyGroups(int value, int[] phase){
		for(int baseIndex = 0; baseIndex < value; baseIndex++){
			for(int i = value + baseIndex; i < phase.length; i += value)
				phase[i] = phase[baseIndex];			
		}
		//System.out.println("GROUPS=" + value + Arrays.toString(phase));
		return phase;
	}
	
	private int[] applyBlocks(int value, int[] phase){
		int[] newPhase = new int[phase.length];
		for(int blockIndex = 0; blockIndex * value < phase.length; blockIndex++){
			for(int i = 0; i < value && i + value * blockIndex < phase.length; i++){
				newPhase[i] = phase[blockIndex];
			}
		}
		//System.out.println("BLOCKS=" + value + Arrays.toString(newPhase));
		return newPhase;
	}
	
	private int[] applyWings(int value, int[] phase){
		int segmentLength = phase.length / Math.abs(value);
		//while(segmentLength > 0 && i < phase.length){
		for(int i = 0; i < phase.length; i += segmentLength){
			
			//System.out.println("A" + segmentLength + " " + i + phase.length);
			for(int position = 0; position < segmentLength && i + position < phase.length; position++){
				int destinationIndex = 2 * segmentLength + i - position - 1 + phase.length % 2;
				//if(phase.length % 2 == 0) destinationIndex--;
				//System.out.println("B" + destinationIndex + " " + phase.length);
				
				if(destinationIndex < phase.length){
					phase[destinationIndex] = phase[i + position] + (value < 0 ? 180 : 0);
				}
			}
		}
		//System.out.println("WINGS=" + value + Arrays.toString(phase));
		return phase;
	}
}
