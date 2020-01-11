package dmx.OpenLyght.Utils;

public class Wait {
	public long ms;
	public Wait(){
		ms = System.currentTimeMillis();
	}
	public void sleep(int ms){
		long rn = System.currentTimeMillis();
		int wait = (int) (ms - (rn - this.ms));
		if(wait > 0){
			wait(wait);
			rn += wait;
		}
		this.ms = rn;
	}
	
	public static void wait(int ms){
		try{
		    Thread.sleep(ms);
		} catch(Exception ex){
		    Thread.currentThread().interrupt();
		}
	}
}
