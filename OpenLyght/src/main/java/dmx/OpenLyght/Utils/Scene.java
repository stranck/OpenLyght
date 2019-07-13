package dmx.OpenLyght.Utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import dmx.OpenLyght.App;
import dmx.OpenLyght.BasicChannel;
import dmx.OpenLyght.Channel;
import dmx.OpenLyght.ChannelModifiers;
import dmx.OpenLyght.Fixture;
import dmx.OpenLyght.Group;

public class Scene implements ChannelModifiers {
	private ArrayList<Step> steps = new ArrayList<Step>();
	private ArrayList<Channel> channels;
	private ArrayList<Fixture> groupChannels = new ArrayList<Fixture>();
	private ArrayList<FadeEngine> fe = new ArrayList<FadeEngine>();
	private String groupName, name = this.toString();
	private Channel speedChannel;
	private Step currentStep;
	private boolean absolute, status;
	private int priority, speedChanelPriority, maxEnable = Integer.MAX_VALUE, maxSpeed = 4000;
	
	public Scene(String path) {
		try{
			JSONObject data = new JSONObject(App.utils.read(path));
			JSONArray steps = data.getJSONArray("steps");
			priority = data.getInt("priority");
			if(data.has("speedChanelPriority")) speedChanelPriority = data.getInt("speedChanelPriority");
			speedChannel = App.utils.getChannel(data.getString("speedChannel"));
			if(speedChanelPriority != 0) speedChannel.addChannelModifier(this, speedChanelPriority);
				else speedChannel.addChannelModifier(this);
			groupName = data.getString("group");
			status = data.getBoolean("defaultStatus");
			absolute = data.getBoolean("absolute");
			if(data.has("maxSpeed")) maxSpeed = data.getInt("maxSpeed");
			if(data.has("name")) name = data.getString("name");
			if(data.has("maxEnable")) maxEnable = data.getInt("maxEnable");
			
			Group g = App.utils.getGroup(groupName, true);
			groupChannels = App.utils.getGroup(groupName, true).getFixtures();
			ArrayList<String> chNames = new ArrayList<String>();
			for(int i = 0; i < steps.length(); i++){
				Step s = new Step(steps.getJSONArray(i), g);
				this.steps.add(s);
				chNames.addAll(s.getChannels());
			}
			channels = g.getChannelsByNames(App.removeDuplicates(chNames));
			for(Channel c : channels)
				if(priority != 0) c.addChannelModifier(this, priority);
			
			currentStep = new Step(this.channels);
			gotoStep(0);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void gotoStep(int step){
		//System.out.println(step + " " + previousStep);
		for(FadeEngine fe : this.fe) fe.getThread().interrupt();
		groupChannels = App.utils.getGroup(groupName, false).getFixtures();
		Step nextStep = steps.get(step);
		fe.clear();

		System.out.println("step: " + step + " channel size: " + channels.size());	
		for(Channel c : channels){
			try{
				if(groupChannels.contains(c.getOriginalFixture())) {
					BasicChannel nextBc = nextStep.getBasicChannel(c), curBc = currentStep.getBasicChannel(c);
					if(nextBc != null && curBc != null){
						boolean pr = priority == 0;
						//System.out.println("SC: " + nextBc.getValue() + " " + nextBc.getChannel().getOriginalFixture());
						if(nextBc.getSmooth()){
							fe.add(new FadeEngine(maxSpeed * speedChannel.getValue() / 255,
									curBc,
									nextBc,
									c,
									pr));
						} else {
							if(pr)
								c.setOriginalValue(nextBc.getValue());
							else 
								curBc.setValue(nextBc.getValue());
							c.reportReload();
						}
						//System.out.println("CURBC: " + curBc.getValue() + " " + curBc);

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
			//System.out.println("GETVALUE " + ch + " " + currentStep.getBasicChannel(ch) + "\n" + status + " " + originalValue + " " + maxEnable + " " + groupChannels.contains(ch));
			if(status && originalValue < maxEnable && groupChannels.contains(ch.getOriginalFixture()) && currentStep.contains(ch)){
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
	
	@Override
	public String toString(){
		return "Scene: " + name + " @ " + hashCode();
	}
}
