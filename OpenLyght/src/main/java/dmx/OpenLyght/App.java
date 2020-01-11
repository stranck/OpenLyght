package dmx.OpenLyght;

import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class App {
	
	public static Stuff utils;

    public static void main(String[] args) throws Exception{
    	System.out.println(likeIgnoreCase("CiAo", "*.ia*"));
    	utils = new Stuff();
    }
	
    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list){
    	ArrayList<T> ret = new ArrayList<T>();
    	for(T element : list)
    		if(!ret.contains(element))
    			ret.add(element);
    	return ret;
    }
    
	public static int getInt(String s){ //This function sucks
		try {
			return Integer.parseInt(s);
		} catch (Exception e){}
		return -1;
	}
	
	public static boolean like(String original, String stringToMatch){
		return original.matches(stringToMatch.replace("*", ".*"));
	}
	public static boolean likeIgnoreCase(String original, String stringToMatch){
		return like(original.toUpperCase(), stringToMatch.toUpperCase());
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
    
    public static String time(String format) {
		SimpleDateFormat sdfDate = new SimpleDateFormat(format);
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;	
	}
    
    @Deprecated
	public static void wait(int ms){
		try{
		    Thread.sleep(ms);
		} catch(Exception ex){
		    Thread.currentThread().interrupt();
		}
	}
    
    public static boolean execute(String command, int timeout) throws Exception {
		boolean success = true;
		Process p = execute(command);
		if(!p.waitFor(timeout, TimeUnit.SECONDS)){
			System.out.println("Process timed out. Killing it gently");
			success = false;
			p.destroy();
			if(!p.waitFor(15, TimeUnit.SECONDS)){
				System.out.println("Nvm fuck it. SIGTERM IT NOW");
				p.destroyForcibly();
			}
		}
		return success;
	}
    public static Process execute(String command) throws Exception {
    	return Runtime.getRuntime().exec(command);
    }
}
