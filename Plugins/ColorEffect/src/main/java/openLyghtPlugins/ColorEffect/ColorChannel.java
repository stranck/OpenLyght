package openLyghtPlugins.ColorEffect;

import java.awt.Color;

import dmx.OpenLyght.App;
import dmx.OpenLyght.BasicChannel;
import dmx.OpenLyght.Channel;
import dmx.OpenLyght.ChannelModifiers;
import dmx.OpenLyght.Fixture;

public class ColorChannel extends BasicChannel implements ChannelModifiers {

	public static final byte MODE_NONE = -1;
	public static final byte MODE_RGB = 0;
	public static final byte MODE_COLORWHEEL = 1;	
	public static final String COLOR_ATTRIB = "attributes color ";
	public static final String RGB_PREXIF = "colors rgb ";
	public static final String[] RGB_COLORS = {"red", "green", "blue"};
	public static final int RGB_RED = 0;
	public static final int RGB_GREEN = 1;
	public static final int RGB_BLUE = 2;
	
	private byte mode = MODE_NONE;
	private boolean isActive;
	private Channel[] rgb = new Channel[RGB_COLORS.length];
	private Fixture fixture;
	private ColorSelector cs;
	
	public ColorChannel(int value, boolean smooth, Channel ch, ColorSelector cs) {
		super(value, smooth, ch);
		this.cs = cs;
		fixture = ch.getOriginalFixture();
		String colorMode = fixture.getAttributeByKey(COLOR_ATTRIB + "mode");
		System.out.println(fixture.getName() + " cm: " + colorMode + " (" + COLOR_ATTRIB + "mode)");
		//if(colorMode != null){
			switch(colorMode){
				case "rgb":{
					mode = MODE_RGB;
					String name = ch.getDescription();
					for(int i = 0; i < RGB_COLORS.length; i++){
						String chName = RGB_PREXIF + RGB_COLORS[i];
						if(!App.likeIgnoreCase(chName, name)){
							//obtain rgb channeal
							Channel c = fixture.getChannelsByName(chName).get(0);
							rgb[i] = c;
							c.addChannelModifier(this);
							System.out.println("CC: added cm to " + c.getDescription() + " | " + c.getOriginalFixture());
						} else rgb[i] = ch;
					}
					break;
				}
				case "colorWheel":{
					mode = MODE_COLORWHEEL;
					break;
				}
			}
		//}
	}

	public void removeChannelModifiers(){
		for(Channel c : rgb)
			if(c != null)
				c.removeChannelModifier(this);
	}
	
	public int getFinalValue(Channel ch){
		int value = 0;
		EffectColor ec = cs.getSelected();
		switch(mode){
			case MODE_RGB: {
				Color rgb = ec.getRgbColor();
				if(ch == this.rgb[RGB_RED])
					value = rgb.getRed();
				else if(ch == this.rgb[RGB_GREEN])
					value = (short) rgb.getGreen();
				else if(ch == this.rgb[RGB_BLUE])
					value = (short) rgb.getBlue();
				break;
			}
			case MODE_COLORWHEEL: {
				String s = fixture.getAttributeByKey(COLOR_ATTRIB + ec.toString());
				try {
					value = Integer.parseInt(s);
				} catch (Exception e) {System.out.println("ERROR IN COLOR EFFECT: " + e);}
				break;
			}
		}
		return value;
	}
	
	public void setStatus(boolean status){
		isActive = status;
		if(mode == MODE_RGB)
			for(Channel c : rgb)
				c.reportReload();
	}
	
	@Override
	public short getChannelValue(short originalValue, int index, Channel ch) {
		//System.out.println(isActive + "-" + Main.amount + ") Requesting value for " + ch.getDescription() + " | " + ch.getOriginalFixture());
		if(isActive)
			originalValue += (short) ((getFinalValue(ch) - originalValue) * Main.amount / 0xFF);
		return originalValue;
	}

}
