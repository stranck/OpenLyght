package dmx.OpenLyght.GUI.ComboxShit;

public class Numbers {
	//Do I really need a Numbers class because a JComboBox can't fucking accept a simple fucking String as Item?!?
	private Integer n;
	private String displayName = null;
	
	public Numbers(int n){
		this.n = n;
	}
	
	public Numbers(String displayName){
		this.displayName = displayName;
	}
	
	public int getNumber(){
		return n;
	}
	
	@Override
	public String toString(){
		String s = null;
		if(displayName == null) s =  n.toString();
			else s = displayName;
		return s;
	}
}
