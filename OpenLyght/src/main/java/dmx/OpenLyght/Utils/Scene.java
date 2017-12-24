package dmx.OpenLyght.Utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import dmx.OpenLyght.App;
import dmx.OpenLyght.Channel;
import dmx.OpenLyght.ChannelModifiers;

public class Scene implements ChannelModifiers {
	private ArrayList<Step> steps = new ArrayList<Step>();
	private ArrayList<Channel> channels = new ArrayList<Channel>();
	private ArrayList<Channel> groupChannels = new ArrayList<Channel>();
	private ArrayList<FadeEngine> fe = new ArrayList<FadeEngine>();
	private String groupName, name = this.toString();
	private Channel speedChannel;
	private Step currentStep;
	private boolean absolute, status;
	private int priority, maxEnable = Integer.MAX_VALUE, maxSpeed = 4000;
	
	public Scene(Path p) {
		try{
			JSONObject data = new JSONObject(new String(Files.readAllBytes(p)));
			JSONArray steps = data.getJSONArray("steps");
			JSONArray channels = data.getJSONArray("channels");
			priority = data.getInt("priority");
			speedChannel = App.utils.getChannel(data.getString("speedChannel"));
			speedChannel.addChannelModifier(this);
			groupName = data.getString("group");
			status = data.getBoolean("defaultStatus");
			absolute = data.getBoolean("absolute");
			if(data.has("maxSpeed")) maxSpeed = data.getInt("maxSpeed");
			if(data.has("name")) name = data.getString("name");
			if(data.has("maxEnable")) maxEnable = data.getInt("maxEnable");
			
			for(int i = 0; i < channels.length(); i++){
				Channel c = App.utils.getChannel(channels.getString(i));
				if(priority != 0) c.addChannelModifier(this, priority);
				this.channels.add(c);
			}
			
			for(int i = 0; i < steps.length(); i++)
				this.steps.add(new Step(steps.getJSONArray(i), this.channels));
			
			currentStep = new Step(this.channels);
			gotoStep(0);
			groupChannels = App.utils.getGroup(groupName).getChannels();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void gotoStep(int step){
		for(FadeEngine fe : this.fe) fe.getThread().interrupt();
		groupChannels = App.utils.getGroup(groupName).getChannels();
		Step nextStep = steps.get(step);
		fe.clear();

		//System.out.println("step: " + step + " channel size: " + channels.size());
		for(int i = 0; i < channels.size(); i++){
			try{
				if(groupChannels.contains(channels.get(i))) {
					if(nextStep.getBasicChannel(i).getSmooth()){
						fe.add(new FadeEngine(maxSpeed * speedChannel.getValue() / 255,
								currentStep.getBasicChannel(i),
								nextStep.getBasicChannel(i),
								channels.get(i),
								priority == 0));
					} else {
						if(priority == 0)
							channels.get(i).setOriginalValue(nextStep.getBasicChannel(i).getValue());
						else 
							currentStep.getBasicChannel(i).setValue(nextStep.getBasicChannel(i).getValue());
						channels.get(i).reportReload();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//System.out.println("exit. Fe size: " + fe.size() + ". Object: " + this);
	}
	
	public void setStatus(boolean status){
		this.status = status;
		for(Channel ch : channels)
			ch.reportReload();
	}
	
	public String getName(){
		return name;
	}
	
	@Override
	public synchronized short getChannelValue(short originalValue, int index, Channel ch) {
		short value = originalValue;
		//System.out.println("scene " + value + " " + status + " " + ch.hashCode() + " " + hashCode() + " " + originalValue + " " + index);
		if(ch != speedChannel) {
			//System.out.println("PREVAL " + value + status + groupChannels.contains(ch) + groupChannels.size() + " " + maxEnable);
			if(status && originalValue < maxEnable && groupChannels.contains(ch)){
				if(absolute) value = currentStep.getBasicChannel(ch).getValue();
				else value = (short) (originalValue * currentStep.getBasicChannel(ch).getValue() / 0xFF);
			}
		} else {
			try{
				//System.out.println("Editing speed. Fe size: " + fe.size() + ". Object: " + this);
				int speed = maxSpeed * value / 255;
				for(FadeEngine fe : this.fe) fe.setSpeed(speed);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		//System.out.println("val " + value + " " + status + " " + ch.hashCode() + " " + hashCode());
		return value;
	}
	
	/*private void replaceSteps(Step original, Step replace){
		for(int i = 0; i < channels.size(); i++)
			if(App.getGroup(groupName).getChannels().contains(channels.get(i)))
				original.getBasicChannel(i).setValue(replace.getBasicChannel(i).getValue());
	}
	
	public void runOLD() {
		//SISTEMARE VErIFICA VELOCITA' MENTRE GIA' IN ESECUZIONE
		int crossLoop = (15000 * speedChannel.getValue() / 255) / 20;
		short variationPerLoop[] = new short[channels.size()];
		Step nextStep = steps.get(nextStepIndex);
		
		for(int i = 0; i < variationPerLoop.length && !Thread.interrupted(); i++){
			if(App.getGroup(groupName).getChannels().contains(channels.get(i))) {
				if(nextStep.getBasicChannel(i).getSmooth()){
					variationPerLoop[i] = (short)((currentStep.getBasicChannel(i).getValue() - nextStep.getBasicChannel(i).getValue()) / crossLoop * -1);
				} else currentStep.getBasicChannel(i).setValue(nextStep.getBasicChannel(i).getValue());
			}
		}
		
		while(crossLoop-- > 0 && !Thread.interrupted())
			for(int i = 0; i < variationPerLoop.length && !Thread.interrupted(); i++){
				if(App.getGroup(groupName).getChannels().contains(channels.get(i))) {
					short value = currentStep.getBasicChannel(i).getValue();
					if(value != nextStep.getBasicChannel(i).getValue()){
						
						currentStep.getBasicChannel(i).setValue((short) (value + variationPerLoop[i]));
						if(priority) channels.get(i).setOriginalValue(currentStep.getBasicChannel(i).getValue());
						channels.get(i).reportReload();
					}
				}
			}
		if(!Thread.interrupted()) replaceSteps(currentStep, nextStep);
	}*/
}
