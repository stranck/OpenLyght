package openLyghtPlugins.ButtonsUtils;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.JSONArray;
import org.json.JSONObject;

import dmx.OpenLyght.GUI.Action;

public class Sequence {
	
	private ArrayList<ArrayList<Command>> commands = new ArrayList<ArrayList<Command>>();
	private ArrayList<String> labels = new ArrayList<String>();
	private JComboBox<String> comboBox;
	private String name;
	private int index;
	
	public Sequence(JSONObject sequence) throws Exception{
		name = sequence.getString("name");
		
		JSONArray sequences = sequence.getJSONArray("commands");
		for(int i = 0; i < sequences.length(); i++){
			ArrayList<Command> cmd = new ArrayList<Command>();
			JSONArray cmds = sequences.getJSONArray(i);
			for(int n = 0; n < cmds.length(); n++)
				cmd.add(new Command(cmds.getString(n), Main.getScene(sequence.getString("scene"))));
			System.out.println(this + " CMD[" + i + "] SIZE = " + cmd.size() + " " + cmds.length());
			commands.add(cmd);
		}
		
		JSONArray lbs = sequence.getJSONArray("labels");
		for(int i = 0; i < lbs.length(); i++)
			labels.add(lbs.getString(i));
		
		gui();
		
		Main.openLyght.mainWindow.addListener(new Action() {
			@Override
			public void actionPerformed() {
				index++;
				if(index >= commands.size()) index = 0;
				//commands.get(index).execute(false);
				comboBox.setSelectedIndex(index);
			}
		}, sequence.getString("goKey"));
		
		Main.openLyght.mainWindow.addListener(new Action() {
			@Override
			public void actionPerformed() {
				System.out.println("resetting");
				index = 0;
				//commands.get(0).execute(false);
				comboBox.setSelectedIndex(0);
			}
		}, sequence.getString("resetKey"));

		execute(commands.get(index));
	}
	
	public void gui(){
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JLabel(name + ":"), BorderLayout.NORTH);
		
		comboBox = new JComboBox<String>();
		panel.add(comboBox, BorderLayout.CENTER);
		for(String s : labels)
			comboBox.addItem(s);
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				index = comboBox.getSelectedIndex();
				//System.out.println("Pressed: " + this);
				execute(commands.get(index));
			}
		});
		
		Main.sequencePanel.addSequence(panel);
	}
	
	private void execute(ArrayList<Command> cmds){
		//System.out.println(cmds.size() + " " + commands.size());
		for(Command c : cmds) c.execute(false);
	}
}
