package openLyghtPlugins.DMXUtils;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import dmx.OpenLyght.Utils.ChannelInterval;
import dmx.OpenLyght.Utils.Fader;
import dmx.OpenLyght.Utils.Scene;

public class Input {
	
	private ArrayList<Fader> faders = new ArrayList<Fader>();
	private Scene blackOutScene;
	private int fadersNO;
	private static int blackoutFaderID = 0;
	private static boolean enableInputs = false;
	
	public Input(File f) throws Exception{
		JSONArray fadersData = new JSONArray(Main.openLyght.read(f.getAbsolutePath()));
		PagePanel.faders.add(f.getName());
		
		for(int i = 0; i < fadersData.length(); i++){
			
			ArrayList<ChannelInterval> ci = new ArrayList<ChannelInterval>();
			JSONArray fader = fadersData.getJSONArray(i);
			for(int n = 0; n < fader.length(); n++){
				JSONObject channel = fader.getJSONObject(n);
				ci.add(new ChannelInterval(Main.openLyght.getChannel(channel.getString("channelName")), channel.getInt("min"), channel.getInt("max")));
			}
			
			faders.add(new Fader(ci));
		}
		fadersNO = faders.size();
		
		if(fadersNO + 2 > blackoutFaderID)
			blackoutFaderID = fadersNO + 2;
		blackOutScene = new Scene(Main.defaultPath + "blackoutScene.json");
	}
	
	public void setFaderStatus(short value, short fader){
		//System.out.println(value + " " + fader + " " + enableInputs);
		try{
			if(fader == blackoutFaderID) {
				enableInputs = value > 127;
				blackOutScene.setStatus(!enableInputs);
			}
			else if(enableInputs){
				if(fader == 1) {
					Main.buttons.buttonPressed(value);
				} else {
					if(fader - 2 < fadersNO)
						faders.get(fader - 2).setValue(value);
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void reset(){
		for(Fader f : faders)
			f.setValue((short) 0);
	}
}
