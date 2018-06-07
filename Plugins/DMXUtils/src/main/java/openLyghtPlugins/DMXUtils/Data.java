package openLyghtPlugins.DMXUtils;

public class Data {
	
	public short channel, value;
	
	public Data(short channel, short value){
		this.channel = channel;
		this.value = value;
	}
	
	@Override
	public String toString(){
		return "Channel: " + channel + "\tValue: " + value;
	}
}
