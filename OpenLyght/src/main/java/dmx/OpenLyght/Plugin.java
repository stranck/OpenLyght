package dmx.OpenLyght;

public interface Plugin {
	
	public String getName();
	
	public String[] getTags();
	
	public void message(String message);
}
