package openLyghtPlugins.DMXUtils.ext;

import java.io.File;
import java.net.InetAddress;

import org.json.JSONObject;

import dmx.OpenLyght.App;
import openLyghtPlugins.DMXUtils.Main;
import openLyghtPlugins.DMXUtils.Sender;

public class ArduinoExt {
	public static final int INDEX_COM =		0;
	public static final int INDEX_PORT =	1;
	public static final int INDEX_SYNC =	2;
	//public static final int INDEX_PATH =	3;
	public static final int INDEX_LOG =		3;
	//public static final int PORT = 57172;
	public static final char MSG_SEND = '>';
	public static final char MSG_EXIT = ';';
	public static final char MSG_NONE = '?';
	public static final char RESPONSE_GETALL = 'a';
	public static final char RESPONSE_OK = 'o';
	
	public static short[] values = new short[512];
	public static int maxTries;
	public static Sender send;
	public static boolean initClient(JSONObject data) throws Exception{
		System.out.println("Init connection to DMX client");
		boolean alreadyStarted = true;
		for(int i = 0; i < values.length; i++)
			values[i] = -1;
		int port = data.getInt("port");
		send = new Sender(port, InetAddress.getLocalHost(), data.getInt("timeout"));
		maxTries = data.getInt("maxTries");
		System.out.println("Checking if sender process is already running");
		if(send.send(MSG_NONE + "i want to know, vorrei saper!", maxTries * 16) == Sender.MSG_RESEND){
			System.out.println("Starting sender process");
			String s = "java -jar \""
					+ new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getAbsolutePath()
					.replace("!", "").replace("file:", "")
				+ "\" " + data.getString("sender") + " " + port + " " + Main.syncTime;
			System.out.println(s);
			App.execute(s);
			alreadyStarted = false;
		}
		return alreadyStarted;
	}
	public static int send(){
		String data = "" + MSG_SEND;
		for(short i = 0; i < 512; i++)
			if(values[i] > -1){
				//System.out.print(i + "@" + values[i] + "\t");
				data += Main.codeOutput(i) + Main.codeOutput(values[i]);
			}
		//System.out.println("");
		//System.out.println("'" + data + "'");
		try{
			switch(send.send(data, maxTries)){
				case RESPONSE_GETALL: {
					System.out.println("Detected a different dmx out client. Resending all dmx values");
					for(int i = 0; i < 512; i++){
						short v = Main.channels[i].getDMXValue();
						if(v != 0)
							values[i] = v;
					}
					break;
				}
				case RESPONSE_OK: {
					for(int i = 0; i < 512; i++)
						values[i] = -1;
					break;
				}
				default: {
					return -1;
				}
			}
		} catch(Exception e){
			//e.printStackTrace();
			return -1;
		}
		return data.length();
	}
	public static void destroyClient(){
		try {
			if(send.send(MSG_EXIT + "so long gay bowser", maxTries * 16) == Sender.MSG_RESEND)
				System.out.println("Unable to kill the arduino connector");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
