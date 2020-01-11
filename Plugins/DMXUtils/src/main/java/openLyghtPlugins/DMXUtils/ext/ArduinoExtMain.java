package openLyghtPlugins.DMXUtils.ext;

import dmx.OpenLyght.Stuff;
import dmx.OpenLyght.Utils.Wait;
import openLyghtPlugins.DMXUtils.DMXInput;
import openLyghtPlugins.DMXUtils.Main;
import openLyghtPlugins.DMXUtils.Sender;

public class ArduinoExtMain {
	public static Stuff ol;
	public static Widow widow;
	public static DMXInput out;
	
	public static void main(String[] args) throws Exception{
		//testLenConv();
		boolean log = args.length > ArduinoExt.INDEX_LOG && args[ArduinoExt.INDEX_LOG].equalsIgnoreCase("true");
		if(!log) {
			widow = new Widow();
			//String path = new String(Base64.decodeBase64(args[ArduinoExt.INDEX_PATH].getBytes()));
			widow.setTitle("OpenLyght arduino connector (" + args[ArduinoExt.INDEX_COM] + ") "
					+ "- Listening on port " + args[ArduinoExt.INDEX_PORT]);
			//		+ " - Path: " + path);
		}
		System.out.println("Loading args");
		char response = ArduinoExt.RESPONSE_GETALL;
		System.out.println("Loading sender object");
		Sender s = new Sender(Integer.parseInt(args[ArduinoExt.INDEX_PORT]));
		//System.out.println("Init fixtures");
		//initFixture(path); 
		System.out.println("Loading dmx reset string");
		byte[] reset = loadResetString().getBytes();
		boolean newConnection = false;
		System.out.println("Syncing up with arduino");
		out = new DMXInput(args[ArduinoExt.INDEX_COM]);
		Wait.wait(Integer.parseInt(args[ArduinoExt.INDEX_SYNC]));
		System.out.println("Startup done. Starting receiving packets");
		while(true){
			try{
				switch(s.receive(response)){
					/*case MSG_DISCOVERY: {
						send(RESPONSE_DISCOVERY, packet, socket);
						break;
					}*/
					case ArduinoExt.MSG_NONE: {
						System.out.println("Incoming connection from a new client");
						newConnection = true;
						break;
					}
					case ArduinoExt.MSG_EXIT: {
						System.out.println("Exiting");
						System.exit(0);
					}
					case ArduinoExt.MSG_SEND: {
						//System.out.println("'" + new String(s.msg) + "'");
						if(newConnection){
							out.writeData(reset);
							newConnection = false;
							System.out.println("Resetted DMX out values");
						}
						out.writeData(s.msg);
						response = ArduinoExt.RESPONSE_OK;
						if(log) System.out.println("OUT: " + s.getString());
					}
				}
			} catch (Exception e){if(log) e.printStackTrace();}
		}
	}
	
	public static String loadResetString(){
		String reset = "";
		String zero = Main.codeOutput((short) 0);
		for(short i = 0; i < 512; i++)
			reset += Main.codeOutput(i) + zero;
		return reset;
	}
	
	public static void initFixture(String path) throws Exception{
		ol = new Stuff(path);
		ol.reloadVariables();
		ol.reloadFixtureTypes();
		ol.reloadFixtures();
		ol.reloadGroups();
	}
	
	@SuppressWarnings("unused")
	@Deprecated
	private static void testLenConv(){
		int n = 0x00aa;
		byte a = (byte) (n >> 8);
		byte b = (byte) (n & 0xff);
		int res = a << 8 & 0xff00 | b & 0xff;
		System.err.println(String.format(
				  "Orig:\t%d\t0x%08X\n"
				+ "A:\t%d\t0x%02X\n"
				+ "B:\t%d\t0x%02X\n"
				+ "Result:\t%d\t0x%08X\n",
				n, n, a, a, b, b, res, res));
		System.exit(0);
	}
}
