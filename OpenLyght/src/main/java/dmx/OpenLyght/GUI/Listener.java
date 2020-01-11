package dmx.OpenLyght.GUI;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;

import dmx.OpenLyght.Utils.Action;

@SuppressWarnings("serial")
public class Listener extends AbstractAction {
	
	private int keyCode;
	private ArrayList<Action> actions = new ArrayList<Action>();
	
	public Listener(int keyCode){
		this.keyCode = keyCode;
	}
	
	public void addAction(Action a){
		actions.add(a);
	}
	
	public int getKeyCode(){
		return keyCode;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for(Action a : actions)
			a.actionPerformed();
	}
}
