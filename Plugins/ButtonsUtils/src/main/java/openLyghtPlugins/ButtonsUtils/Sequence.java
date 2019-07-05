package openLyghtPlugins.ButtonsUtils;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.JSONArray;
import org.json.JSONObject;

import dmx.OpenLyght.GUI.Action;
import dmx.OpenLyght.GUI.ButtonList;

public class Sequence {
	
	private ArrayList<ArrayList<Command>> commands = new ArrayList<ArrayList<Command>>();
	private ArrayList<String> labels = new ArrayList<String>();
	private ButtonList button;
	private JPanel panel;
	private String name;
	
	public Sequence(JSONObject sequence) throws Exception{
		name = sequence.getString("name");
		
		JSONArray sequences = sequence.getJSONArray("commands");
		for(int i = 0; i < sequences.length(); i++){
			ArrayList<Command> cmd = new ArrayList<Command>();
			JSONArray cmds = sequences.getJSONArray(i);
			for(int n = 0; n < cmds.length(); n++)
				cmd.add(new Command(cmds.getString(n), Main.getScene(sequence.getString("scene")), this, i));
			System.out.println(this + " CMD[" + i + "] SIZE = " + cmd.size() + " " + cmds.length());
			commands.add(cmd);
		}
		
		JSONArray lbs = sequence.getJSONArray("labels");
		for(int i = 0; i < lbs.length(); i++)
			labels.add(lbs.getString(i));
		
		gui(sequence.getInt("page"));
		
		Main.openLyght.mainWindow.addListener(new Action() {
			@Override
			public void actionPerformed() {
				button.doClick();
			}
		}, sequence.getString("goKey"));
		
		Main.openLyght.mainWindow.addListener(new Action() {
			@Override
			public void actionPerformed() {
				new Thread(){
					public void run(){
						System.out.println("resetting");
						button.reset();
					}
				}.start();
			}
		}, sequence.getString("resetKey"));
		
		button.reset();
		execute(commands.get(button.getIndex()));
	}
	
	public void gui(int page){
		panel = new JPanel(new BorderLayout());
		panel.add(new JLabel(name + ":"), BorderLayout.NORTH);
		
		button = new ButtonList();
		panel.add(button, BorderLayout.CENTER);
		for(String s : labels)
			button.addItem(s);
		button.addAction(new Action() {
			@Override
			public void actionPerformed() {
				execute(commands.get(button.getIndex()));
			}
		});
		
		Main.sequencePanel.addSequence(panel, page);
	}
	
	private void execute(ArrayList<Command> cmds){
		for(Command c : cmds) c.execute(false);
	}
	
	public ButtonList getButton(){
		return button;
	}
	public String getName(){
		return name;
	}
	public JPanel getPanel(){
		return panel;
	}
}
