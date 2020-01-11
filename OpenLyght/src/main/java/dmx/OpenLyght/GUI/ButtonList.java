package dmx.OpenLyght.GUI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;

import dmx.OpenLyght.Utils.Action;

@SuppressWarnings("serial")
public class ButtonList extends JButton {
	
	public static final int MODE_GO = 0;
	public static final int MODE_GOBACK = 1;
	public static final int MODE_TOP = 2;
	public static final int MODE_LOAD = 3;
	public static final int MODE_GOTO = 4;
	public static final int DEFAULT_MODE = MODE_GO;
	
	public static final Color DEFAULT_COLOR = new Color(0, 0, 0);
	public static final Color LOADED_COLOR = new Color(192, 0, 0);
	
	private ArrayList<Object> labels = new ArrayList<Object>();
	private ArrayList<Integer> jump = new ArrayList<Integer>();
	private ArrayList<Action> actions = new ArrayList<Action>();
	private static int mode = DEFAULT_MODE;
	private static boolean tempMode = false;
	private boolean resetting = false;
	private int loaded = -1;
	private int index = 0;
	
	public ButtonList(){
		addActions();
	}
	public ButtonList(ArrayList<Object> labels){
		if(labels != null){
			this.labels = labels;
			for(@SuppressWarnings("unused") Object o : labels)
				jump.add(-1);
		}
		addActions();
	}
	private void addActions(){
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(resetting){
					resetting = false;
					top();
				} else {
					if(loaded < 0){
						switch(mode){
							case MODE_GO: {
								go();
								break;
							}
							case MODE_GOBACK: {
								goBack();
								break;
							}
							case MODE_TOP: {
								top();
								break;
							}
							case MODE_LOAD: {
								break;
							}
							case MODE_GOTO: {
								break;
							}
						}
						if(tempMode){
							mode = DEFAULT_MODE;
							tempMode = false;
						}
					} else {
						setIndex(loaded);
						setForeground(DEFAULT_COLOR);
						loaded = -1;
					}
				}
				execActions();
			}
		});
		//addMouseListener(this);
	}
	
	private void execActions(){
		for(Action a : actions)
			a.actionPerformed();
	}
	
	public Object getSelectedItem(){
		return labels.get(index);
	}
	public int getIndex(){
		return index;
	}
	public int length(){
		return labels.size();
	}
	public void addAction(Action action){
		actions.add(action);
	}
	public void addItem(Object item){
		labels.add(item);
		jump.add(-1);
	}
	public void setJump(int position, int jumpTo){
		System.out.println("Setting cue loop: " + position + " " + jumpTo + " " + jump.size() + " " + labels.size());
		jump.set(position, jumpTo);
	}
	public void reset(){
		resetting = true;
		doClick();
	}
	
	public void setIndex(Object item){
		for(int i = 0; i < labels.size(); i++)
			if(labels.get(i).equals(item)){
				setIndex(i);
				break;
			}
	}
	public void setIndex(int i){
		index = i;
		setText(labels.get(i).toString());
	}
	
	public void go(){
		int jmp = jump.get(index);
		if(jmp < 0){
			if(++index > labels.size() - 1) index = 0;
		} else index = jmp;				
		setIndex(index);
	}
	public void goBack(){
		if(--index < 0) index = labels.size() - 1;
		setIndex(index);
	}
	public void top(){
		index = 0;
		setIndex(index);
	}
	public void load(int i){
		loaded = i;
		setForeground(LOADED_COLOR);
		setText(labels.get(index) + "\n(" + labels.get(i) + ")");
	}
	
	public static void setMode(int mode, boolean temp){
		ButtonList.mode = mode;
		tempMode = temp;
	}
	
	public static void addMouseListener(JButton btn){
		btn.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				System.out.println("RELEASED:\t" + btn.getText());
			}
			@Override
			public void mousePressed(MouseEvent e) {
				System.out.println("PRESSED:\t" + btn.getText());
			}
			@Override
			public void mouseExited(MouseEvent e) {
				System.out.println("EXITED: \t" + btn.getText());
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				System.out.println("ENTERED:\t" + btn.getText());
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("CLICKED:\t" + btn.getText());
			}
		});
	}
}
