package dmx.OpenLyght.GUI.ComboxShit;

public class PhaseCombo {
	//I hate JComboBox I hate JComboBox I hate JComboBox I hate JComboBox I hate JComboBox I hate JComboBox
	private int start, end;
	private String displayName = null;
	
	public PhaseCombo(int start, int end){
		this.start = start;
		this.end = end;
	}
	
	public PhaseCombo(String displayName){
		this.displayName = displayName;
	}
	
	public int getStart(){
		return start;
	}
	
	public int getEnd(){
		return end;
	}
	
	@Override
	public String toString(){
		String s = null;
		if(displayName == null) s = start + " THRU " + end;
			else s = displayName;
		return s;
	}
}
