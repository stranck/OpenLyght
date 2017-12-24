package dmx.OpenLyght.GUI.ComboxShit;

public class CharacterZ {
	//Hope the person who made the JComboBox is dead.
	private Character c;
	private String displayName = null;
	
	public CharacterZ(char c){
		this.c = c;
	}
	
	public CharacterZ(String displayName){
		this.displayName = displayName;
	}
	
	public char getChar(){
		return c;
	}
	
	@Override
	public String toString(){
		String s = null;
		if(displayName == null) {
			switch(c){
				case 'S': {
					displayName = "< Bounce";
					break;
				}
				case 's': {
					displayName = "> Bounce";
					break;
				}
				default: displayName = c + "";
			}
		} else s = displayName;
		return s;
	}
}
