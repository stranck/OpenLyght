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
	
	private ArrayList<Command> commands = new ArrayList<Command>();
	private ArrayList<String> labels = new ArrayList<String>();
	private JComboBox<String> comboBox;
	private String name;
	private int index;
	
	public Sequence(JSONObject sequence) throws Exception{
		name = sequence.getString("name");
		
		JSONArray cmds = sequence.getJSONArray("commands");
		for(int i = 0; i < cmds.length(); i++)
			commands.add(new Command(cmds.getString(i), Main.getScene(sequence.getString("scene"))));
		
		JSONArray lbs = sequence.getJSONArray("labels");
		for(int i = 0; i < lbs.length(); i++)
			labels.add(lbs.getString(i));
		
		gui();
		
		Main.openLyght.mainWindow.addListener(new Action() {
			@Override
			public void actionPerformed() {
				index++;
				if(index >= commands.size()) index = 0;
				commands.get(index).execute(false);
				comboBox.setSelectedIndex(index);
			}
		}, sequence.getString("goKey"));
		
		Main.openLyght.mainWindow.addListener(new Action() {
			@Override
			public void actionPerformed() {
				System.out.println("resetting");
				index = 0;
				commands.get(0).execute(false);
				comboBox.setSelectedIndex(0);
			}
		}, sequence.getString("resetKey"));

		commands.get(index).execute(false);
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
				commands.get(index).execute(false);
			}
		});
		
		Main.sequencePanel.addSequence(panel);
	}
}
