package openLyghtPlugins.DMXUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

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
	public static int faderPage = 0, buttonPage = 0, n, syncTime;
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
			
			syncTime = comPorts.getInt("syncTime");
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
			
			System.out.print("dmxUtils is loading faders");
			File[] dir = new File(defaultPath + "faders" + File.separator).listFiles(File::isFile);
			for(File f : dir)
				faders.add(new Input(f));
			
			JSONObject keys = new JSONObject(openLyght.read(defaultPath + "keys.json"));
			buttonKeys = loadKeys(keys.getJSONArray("buttons"));
			faderKeys = loadKeys(keys.getJSONArray("faders"));
			resetKeys = loadKeys(keys.getJSONArray("reset"));
			
			new Thread(this).start();
			if(comPorts.has("inputThread") && comPorts.getBoolean("inputThread")) inputThread();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@SuppressWarnings("unused")
	private static void inputThread(){
		new Thread(){
			@Override
			public void run(){
				@SuppressWarnings("resource")
				Scanner sc = new Scanner(System.in);
				while(true){
					try{
						String[] sp = sc.nextLine().split("@"); //value@channel
						faders.get(faderPage).setFaderStatus(Short.parseShort(sp[0]), Short.parseShort(sp[1]));
					} catch (Exception e){
						e.printStackTrace();
					}
				}
			}
		}.start();
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
			masters.add(new Master(new JSONObject(openLyght.read(f.getAbsolutePath()))));
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
		int i, stats = 0;
		App.wait(syncTime);
		long delay = 0, bytesTrasmitted = 0, totalStart = System.currentTimeMillis(), start;
		while(true){
			start = System.currentTimeMillis();
			openLyght.reloadVirtualChannels();
			for(i = 0; i < 512; i++){
				
				if(channels[i] != null && channels[i].reloadValue()){
					data += codeOutput((short) (i)) + codeOutput(channels[i].getDMXValue());
					sendValues = true;
				}					
			}
			if(sendValues){
				//System.out.println("SENDING: " + data);
				//System.out.println("SENDING: " + Arrays.toString(data.getBytes()));
				out.writeData(data);
				bytesTrasmitted += data.getBytes().length;
				data = "";
				sendValues = false;
			}
			if(++stats > 8500){
				totalStart = System.currentTimeMillis() - totalStart;
				System.out.println("STATS:\t"
						+ "avarageSpeed = " + 
						String.format("%.3f", (double) bytesTrasmitted * 8 / (totalStart / 1000) / 1024)
						+ "kbps (Total: " + bytesTrasmitted + "B in " + totalStart + "ms)\t"
						+ "avarageTimeSlice = " + (delay / 8500) + "ms (Total: " + delay + "ms)");
				stats = 0;
				delay = 0;
				bytesTrasmitted = 0;
				totalStart = System.currentTimeMillis();
			}
			delay += System.currentTimeMillis() - start;
			//System.out.println("reload time: " + (System.currentTimeMillis() - start));
			App.wait(7);
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
							//buttonPage = i;
							PagePanel.setButtonIndex(i);
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
