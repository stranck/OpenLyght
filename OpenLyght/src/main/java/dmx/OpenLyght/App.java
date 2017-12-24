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
	
    public static BasicChannel getBasicChannel(BasicChannel[] channels, Channel ch){
		boolean founded = true;
		int i = 0;
		BasicChannel ret = null;
		while(i < channels.length && founded)
			if(channels[i].isThisChannel(ch)) {
				founded = false;
				ret = channels[i];
			} else i++;
		return ret;
	}
	
    public static int getKeyEvent(String st) throws Exception {
        return KeyEvent.class.getField("VK_" + st.toUpperCase()).getInt(null);
    }

	
    /*public void testMasterFader(){
    	Scanner sc = new Scanner(System.in);
    	Fader f = new Fader(getChannel("Fader0"), 64, 192);
    	ArrayList<Channel> ch = new ArrayList<Channel>();
    	for(int i = 0; i < 4; i++){
    		channels[i].setOriginalValue((short) (63 * i));
    		ch.add(channels[i]);
    	}
    	Master m = new Master(getChannel("Fader0"), ch, 0);
    	m.setLimits(sc.nextInt(), sc.nextInt());
    	for(int i = 0; i < 4; i++){
    		System.out.println(channels[i].getValue());
    	}
    	f.setValue(sc.nextShort());
    	getChannel("Fader0").reloadValue();
    	for(int i = 0; i < 4; i++){
    		channels[i].reloadValue();
    		System.out.println(channels[i].getValue());
    	}
    	sc.close();
    }*/
}
