package dmx.OpenLyght;

public class BasicChannel {
	private short value = 0;
	private boolean smooth;
	private Channel ch;
	
	public BasicChannel(int value, boolean smooth, Channel ch){
		//System.out.println("BC: " + value + " " + ch + " " + ch.getOriginalFixture() + " " + hashCode());
		this.value = (short) value;
		this.smooth = smooth;
		this.ch = ch;
	}
	
	public void setValue(short value){
		this.value = value;
	}
	public void addValue(short value){
		this.value += value;
	}
	
	public short getValue(){
		return value;
	}
	public boolean getSmooth(){
		return smooth;
	}
	public Channel getChannel(){
		return ch;
	}
	
	public boolean isThisChannel(Channel ch){
		return this.ch == ch;
	}
}
