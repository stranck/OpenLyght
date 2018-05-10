package openLyghtPlugins.ColorEffect;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.json.JSONObject;

import dmx.OpenLyght.BasicChannel;
import dmx.OpenLyght.Channel;
import dmx.OpenLyght.Group;
import dmx.OpenLyght.Plugin;
import dmx.OpenLyght.Stuff;
import dmx.OpenLyght.Utils.Effect;


public class Main implements Plugin, Effect {

	private final String[] tags = { "effect", "colors" };
	private final String name = "ColorEffect";
	private double value;
	private ArrayList<BasicChannel> channels = new ArrayList<BasicChannel>();
	public static ColorSelector colorSelector;
	public static String defaultPath;
	public static Stuff openLyght;
	
	public Main(Stuff ol){
		try {
			System.out.println("LOADING: ColorEffect plugin");
			openLyght = ol;
			defaultPath = ol.deafaultPath + "plugins" + File.separator + name + File.separator;
			
			JSONObject data = new JSONObject(new String(Files.readAllBytes(Paths.get(defaultPath + "config.json"))));
			colorSelector = new ColorSelector(data);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public double getValue(int phase, BasicChannel bc) {
		if(phase < 180){
			value = (double)(colorSelector.getValue() - getBasicChannel(bc.getChannel()).getValue()) / 0xFF;
			//System.out.println(phase + " " + colorSelector.getValue() + " " + bc.getChannel().getDMXValue() + " "
			//	+ value + " " + (255 / 255) + "\t\t\t" + bc.hashCode());
		} else
			value = 0;
		return value;
	}
	
	public String getName() {
		return name;
	}
	public String[] getTags() {
		return tags;
	}

	public void message(String message) {
		if(message.equalsIgnoreCase("openlyght started")){
			try{
				System.out.println("ColorEffect: loading JPanel");
				Main.openLyght.mainWindow.getJPanel("sequencePanel").add(colorSelector);
				colorSelector.loadKeyListeners();
			} catch (Exception e) {
				e.printStackTrace();
			}
				
		}
	}
	
	private BasicChannel getBasicChannel(Channel ch){
		BasicChannel ret = null;
		for(BasicChannel bc : channels)
			if(bc.isThisChannel(ch)) {
				ret = bc;
				break;
			}
		return ret;
	}

	public void removeGroup(Group g) {
		if(g != null){
			ArrayList<Channel> ch = g.getChannels();
			for(Channel c : ch)
				for(int i = 0; i < channels.size(); i++)
					if(channels.get(i).isThisChannel(c))
						channels.remove(i);
		}
	}
	public void setGroup(Group g) {
		if(g != null){
			ArrayList<Channel> ch = g.getChannels();
			for(Channel c : ch)
				channels.add(new BasicChannel(c.getDMXValue(), false, c));
		}
	}
	public void setOriginalValue(short value, Channel ch) {
		if(value < 0) value = 0;
		if(value > 255) value = 255;
		BasicChannel bc = getBasicChannel(ch);
		if(bc != null) bc.setValue(value);
	}
}
