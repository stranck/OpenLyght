package openLyghtPlugins.ColorEffect;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.json.JSONObject;

import dmx.OpenLyght.BasicChannel;
import dmx.OpenLyght.Channel;
import dmx.OpenLyght.Plugin;
import dmx.OpenLyght.Stuff;
import dmx.OpenLyght.Utils.Effect;


public class Main implements Plugin, Effect {

	private final String[] tags = { "effect", "colors" };
	private final String name = "ColorEffect";
	private double value;
	private ArrayList<ColorChannel> channels = new ArrayList<ColorChannel>();
	public static ColorSelector colorSelector;
	public static String defaultPath;
	public static Stuff openLyght;
	public static int amount;
	
	public Main(Stuff ol){
		try {
			System.out.println("LOADING: ColorEffect plugin");
			openLyght = ol;
			defaultPath = ol.deafaultPath + "plugins" + File.separator + name + File.separator;
			
			JSONObject data = new JSONObject(ol.read((defaultPath + "config.json")));
			colorSelector = new ColorSelector(data);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public double getValue(int phase, BasicChannel bc) {
		Channel ch = bc.getChannel();
		ColorChannel cc = getColorChannel(ch);
		boolean condition = phase < 180;
		cc.setStatus(condition);
		if(condition){
			value = (double)(cc.getFinalValue(ch) - cc.getValue()) / 0xFF;
			//System.out.println(phase + " " + colorSelector.getValue() + " " + bc.getChannel().getDMXValue() + " "
			//	+ value + " " + (255 / 255) + "\t\t\t" + bc.hashCode());
		} else {
			value = 0;
		}
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
				((JPanel)
						((JPanel)
								Main.openLyght.mainWindow.getJPanel("sequencePanel").getComponent(2))
						.getComponent(0))
				.add(colorSelector);
				//((MainPanel)Main.openLyght.mainWindow.getJPanel("sequencePanel")).addSequence(colorSelector, 0);
				colorSelector.loadKeyListeners();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private ColorChannel getColorChannel(Channel ch){
		ColorChannel ret = null;
		for(ColorChannel bc : channels)
			if(bc.isThisChannel(ch)) {
				ret = bc;
				break;
			}
		return ret;
	}

	public void removeGroup(ArrayList<Channel> ch) {
		if(ch != null){
			for(Channel c : ch)
				for(int i = 0; i < channels.size(); i++){
					ColorChannel cc = channels.get(i);
					if(cc.isThisChannel(c)){
						cc.removeChannelModifiers();
						channels.remove(i--);
					}
				}
		}
	}
	public void setGroup(ArrayList<Channel> ch) {
		if(ch != null){
			for(Channel c : ch){
				channels.add(new ColorChannel(c.getDMXValue(), false, c, colorSelector));
			}
		}
	}
	public void setOriginalValue(short value, Channel ch) {
		if(value < 0) value = 0;
		if(value > 255) value = 255;
		ColorChannel bc = getColorChannel(ch);
		if(bc != null) bc.setValue(value);
	}

	@Override
	public void setAmount(short amount) {
		Main.amount = amount;
	}
}
