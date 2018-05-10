package dmx.OpenLyght;

import java.awt.event.KeyEvent;

public class App {
	
	public static Stuff utils;
	
    public static void main(String[] args) throws Exception{
    	utils = new Stuff();
    }
    
	public static int getInt(String s){ //This function sucks
		try {
			return Integer.parseInt(s);
		} catch (Exception e){}
		return -1;
	}
	
	public static void wait(int ms){
		try{
		    Thread.sleep(ms);
		} catch(Exception ex){
		    Thread.currentThread().interrupt();
		}
	}
	
	public static boolean checkForPluginType(Plugin p, String type){
		String[] tags = p.getTags();
		boolean b = true;
		for(int i = 0; i < tags.length && b; i++)
			if(tags[i].equals(type)) b = false;
		return !b;
	}
	
    public static BasicChannel getBasicChannel(BasicChannel[] channels, Channel ch){;
		int i = 0;
		BasicChannel ret = null;
		while(i < channels.length && ret == null)
			if(channels[i].isThisChannel(ch))
				ret = channels[i];
			else i++;
		return ret;
	}
	
    public static int getKeyEvent(String st) throws Exception {
        return KeyEvent.class.getField("VK_" + st.toUpperCase()).getInt(null);
    }
}
