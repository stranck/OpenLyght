package dmx.OpenLyght.Utils;

import java.util.ArrayList;

import org.json.JSONObject;

import dmx.OpenLyght.App;
import dmx.OpenLyght.BasicChannel;
import dmx.OpenLyght.Channel;
import dmx.OpenLyght.ChannelModifiers;
import dmx.OpenLyght.Group;
 
public class EffectsEngine implements Runnable, ChannelModifiers {
	private Group activeGroup;
	private boolean relative, requestReload = true, reloadingGroup = false;
	private Effect effect;
	private Channel speedChannel, amount;
	private char direction; //>, <, s (>), S (<)
	private short diff, mid, min;
	private int startPhase, endPhase, currentPhase, width, groups, blocks, wings, minSpeed;
	private int[] basePhase;
	private BasicChannel[] channels = new BasicChannel[0];
	//private ArrayList<Integer> phase = new ArrayList<Integer>();
	//private ArrayList<BasicChannel> channels = new ArrayList<BasicChannel>();
	
	public EffectsEngine(JSONObject data, Channel speed, Channel amount){
		min = (short) data.getInt("min");
		int max = data.getInt("max");
		mid = (short) ((max + min) / 2);
		diff = (short) (max - mid);
		
		width = data.getInt("width");
		minSpeed = data.getInt("minSpeed");
		relative = data.getBoolean("relative");
		
		speedChannel = speed;
		this.amount = amount;
	}
	
	public void reset(){
		currentPhase = 0;
	}
	
	public void setGroup(Group g){
		activeGroup = g;
		reloadingGroup = true;
		requestReload = true;
	}
	public void setEffect(Effect e){
		effect = e;
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
		this.wings = wings;
		requestReload = true;
	}
	
	public void reloadEffect(){
		if(reloadingGroup){
			for(BasicChannel bc : channels)
				bc.getChannel().removeChannelModifier(this);
			
			ArrayList<Channel> ch = activeGroup.getChannels();
			channels = new BasicChannel[ch.size()];
			for(int i = 0; i < channels.length; i++){
				Channel c = ch.get(i);
				c.addChannelModifier(this);
				channels[i] = new BasicChannel(0, true, c);
			}
			
			reloadingGroup = false;
		}
		
		int phaseLength = channels.length;
		if(groups == 0){
			if(blocks > 1 && phaseLength >= blocks) phaseLength /= blocks;
			if(wings > 1 && phaseLength >= Math.abs(wings)) phaseLength /= Math.abs(wings);
		} else phaseLength = groups;
		basePhase = new int[channels.length];
		
		int phaseDifference = (startPhase - endPhase) / phaseLength;
		for(int i = 0, phase = 0; i < phaseLength && i < basePhase.length; i++){
			basePhase[i] = phase + startPhase;
			phase += phaseDifference;
		}
		
		if(groups > 0) basePhase = applyGroups(groups, basePhase);
		if(blocks > 1) basePhase = applyBlocks(blocks, basePhase);
		if(Math.abs(wings) > 1) basePhase = applyWings(wings, basePhase);
		
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
		while(!Thread.interrupted()) {
			try{
				if(requestReload) reloadEffect();
				for(int i = 0; i < basePhase.length; i++){
					BasicChannel bc = channels[i];
					updateValue(bc, resizePhase(currentPhase + basePhase[i]));
					bc.getChannel().reportReload();
				}
				//System.out.println(amount.getValue() + "\t" + speedChannel.getValue());
				updatePhase();
				//while(speedChannel.getValue() < 16) App.utils.wait(20);
				App.wait(256 + minSpeed - speedChannel.getValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public short getChannelValue(short originalValue, int index, Channel ch) {
		short effectValue = originalValue;
		try{
			effectValue = (short) (App.getBasicChannel(channels, ch).getValue() * amount.getValue() / 0xFF);
		}catch(Exception e) {}
		short value = effectValue;
		//System.out.println("Channel: " + value + " " + originalValue + " " + App.getBasicChannel(channels, ch).getValue() + " " + amount.getValue() + " " + ch.hashCode());
		if(relative) value += originalValue;
		return value;
	}
	
	private void updatePhase(){
		if(speedChannel.getValue() > 8) {
			switch(direction){
				case '>': {
					if(++currentPhase >= 360) currentPhase = 0;
					break;
				}
				case '<': {
					if(--currentPhase <= 0) currentPhase = 360;
					break;
				}
				case 's': {
					if(++currentPhase >= 360) direction = 'S';
					break;
				}
				case 'S': {
					if(--currentPhase <= 0) direction = 's';
					break;
				}
			}
		}
	}
	
	private void updateValue(BasicChannel bc, int phase){
		if(phase > width) bc.setValue(min); //newPhase : width = phase : 360
			else bc.setValue((short) (effect.getValue(width * phase / 360) * diff + mid));
	}
	
	private int[] applyGroups(int value, int[] phase){
		for(int baseIndex = 0; baseIndex < value; baseIndex++){
			for(int i = value + baseIndex; i < phase.length; i += value)
				phase[i] = phase[baseIndex];			
		}
		return phase;
	}
	
	private int[] applyBlocks(int value, int[] phase){
		int[] newPhase = new int[phase.length];
		for(int blockIndex = 0; blockIndex * value < phase.length; blockIndex++){
			for(int i = 0; i < value && i + value * blockIndex < phase.length; i++){
				newPhase[i] = phase[blockIndex];
			}
		}
		return newPhase;
	}
	
	private int[] applyWings(int value, int[] phase){
		int segmentLength = phase.length / Math.abs(value);
		int i = 0;
		while(segmentLength > 0 && i < phase.length){
			
			//System.out.println("A" + segmentLength + " " + i + phase.length);
			for(int position = 0; position < segmentLength && i + position < phase.length; position++){
				int destinationIndex = 2 * segmentLength + i - position;
				if(phase.length % 2 == 0) destinationIndex--;
				//System.out.println("B" + destinationIndex + " " + phase.length);
				
				if(destinationIndex < phase.length){
					int phaseModifier = 0;
					if(value < 0) phaseModifier = 180;
					//System.out.println("C" + destinationIndex + " " + i + " " + position);
					phase[destinationIndex] = phase[i + position] + phaseModifier;
				}
			}
			
			i += segmentLength;
		}
		return phase;
	}
	
	private int resizePhase(int phase){
		while(phase > 360) phase -= 360;
		while(phase < 0) phase += 360;
		return phase;
	}
}
