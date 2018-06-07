package openLyghtPlugins.DMXUtils;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import dmx.OpenLyght.App;
import dmx.OpenLyght.Channel;
import dmx.OpenLyght.Plugin;
import dmx.OpenLyght.Stuff;
import dmx.OpenLyght.GUI.Action;
import dmx.OpenLyght.Utils.Master;

public class Main implements Plugin, Runnable {

	private final String[] tags = { "dmx", "IO" };
	private final String name = "DMXUtils";
	public static String defaultPath;
	public static Stuff openLyght;
	public static int faderPage = 0, buttonPage = 0, n;
	public static String[] buttonKeys, faderKeys, resetKeys;
	public static ArrayList<Input> faders = new ArrayList<Input>();
	public static ArrayList<Master> masters = new ArrayList<Master>();
	public static Button buttons;
	public static Channel[] channels;
	public static DMXInput in, out;
	
	public Main(Stuff ol){	
		try{
			System.out.println("LOADING: dmxutils plugin");
			openLyght = ol;
			defaultPath = ol.deafaultPath + "plugins" + File.separator + name + File.separator;
			channels = ol.channels;

			JSONObject comPorts = new JSONObject(openLyght.read(defaultPath + "serialPorts.json"));	
			
			out = new DMXInput(comPorts.getString("sender"));
			
			in = new DMXInput(comPorts.getString("reciver")){
				@Override
				public void newData(ArrayList<Data> data){
					for(Data d : data){
						faders.get(faderPage).setFaderStatus(d.value, d.channel);
						//System.out.println(d.toString());
					}
				}
			};
			loadMasters();
			buttons = new Button();
			
			File[] dir = new File(defaultPath + "faders" + File.separator).listFiles(File::isFile);
			for(File f : dir)
				faders.add(new Input(f));
			
			JSONObject keys = new JSONObject(openLyght.read(defaultPath + "keys.json"));
			buttonKeys = loadKeys(keys.getJSONArray("buttons"));
			faderKeys = loadKeys(keys.getJSONArray("faders"));
			resetKeys =loadKeys(keys.getJSONArray("reset"));
			
			new Thread(this).start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String[] loadKeys(JSONArray keys){
		String k[] = new String[keys.length()];
		for(int i = 0; i < k.length; i++)
			k[i] = keys.getString(i);
		return k;
	}
	
	private static void loadMasters() throws Exception {
		File[] dir = new File(defaultPath + "masters" + File.separator).listFiles(File::isFile);
		for(File f : dir){
			System.out.println("Loading master: " + f.getAbsolutePath());
			JSONObject master = new JSONObject(openLyght.read(f.getAbsolutePath()));
			JSONArray channels = master.getJSONArray("channels");
			ArrayList<Channel> ch = new ArrayList<Channel>();
			
			for(int i = 0; i < channels.length(); i++){
				ch.add(openLyght.getChannel(channels.getString(i)));
			}
			Master m = null;
			if(master.has("modifierSourceIndex") && master.has("modifierIndex")) 
				m = new Master(openLyght.getChannel(master.getString("source")),
						ch, master.getInt("mode"),
						master.getInt("modifierSourceIndex"),
						master.getInt("modifierIndex"));
			else 
				m = new Master(openLyght.getChannel(master.getString("source")), ch, master.getInt("mode"));
			if(master.has("limits")){
				JSONObject limits = master.getJSONObject("limits");
				m.setLimits(limits.getInt("min"), limits.getInt("max"));
			}
			if(master.has("enableLimit")) m.setEnableLimit(master.getInt("enableLimit"));
			if(master.has("invertValue")) m.setInvertValue(master.getBoolean("invertValue"));
			masters.add(m);
		}
	}
	
	public static short decodeInput(char[] data, int index){
		return (short) ((data[index] - 65) * 16 + data[index + 1] - 65);
	}
	public static String codeOutput(short value){
		String data = "";
		data += (char) (value / 16 + 65);
		data += (char) (value % 16 + 65);
		return data;
	}
	
	@Override
	public String getName() {
		return name;
	}
	@Override
	public String[] getTags() {
		return tags;
	}

	@Override
	public void run() {
		String data = "";
		boolean sendValues = false;
		int i;
		App.wait(2500);
		while(true){
			openLyght.reloadVirtualChannels();
			for(i = 0; i < 512; i++){
				
				if(channels[i] != null && channels[i].reloadValue()){
					data += codeOutput((short) (i)) + codeOutput(channels[i].getDMXValue());
					sendValues = true;
				}					
			}
			if(sendValues){
				//System.out.println("SENDING: " + data);
				out.writeData(data);
				data = "";
				sendValues = false;
			}
		}
	}

	@Override
	public void message(String message) {
		if(message.equalsIgnoreCase("openlyght started")){
			System.out.println("Loading dafers/buttons pages");
			openLyght.mainWindow.addPanel(new PagePanel(), "pagePanel");
			for(n = 0; n < buttonKeys.length; n++){
				try {
					openLyght.mainWindow.addListener(new Action() {
						private final int i = n;
						@Override
						public void actionPerformed() {
							System.out.println("Loading button page: " + i);
							buttonPage = i;
							PagePanel.buttonComboBox.setSelectedIndex(i);
						}
					}, buttonKeys[n]);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			for(n = 0; n < faderKeys.length; n++){
				try {
					openLyght.mainWindow.addListener(new Action() {
						private final int i = n;
						@Override
						public void actionPerformed() {
							System.out.println("Loading fader page: " + i);
							faderPage = i;
							PagePanel.faderComboBox.setSelectedIndex(i);
						}
					}, faderKeys[n]);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			for(n = 0; n < resetKeys.length; n++){
				try {
					openLyght.mainWindow.addListener(new Action() {
						private final int i = n;
						@Override
						public void actionPerformed() {
							System.out.println("Resetting fader: " + i);
							faders.get(i).reset();
						}
					}, resetKeys[n]);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

}
