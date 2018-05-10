package dmx.OpenLyght.GUI;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.json.JSONArray;
import org.json.JSONObject;

import dmx.OpenLyght.App;
import dmx.OpenLyght.Group;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class GroupSelector extends JPanel {
	private ArrayList<Group> groups = new ArrayList<Group>();
	private ArrayList<String> keys = new ArrayList<String>();
	private ArrayList<JButton> buttons = new ArrayList<JButton>();
	private final Color DISABLED = new Color(255, 0, 0);
	private final Color ENABLED = new Color(0, 192, 0);
	private boolean superGroupActive = false;
	private JButton superGroupButton, keybutton;
	private String groupName;
	
	public GroupSelector(MainWindow mw, JSONObject data) throws Exception {
		setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		groupName = data.getString("superGroupName");
		
		JPanel superGroupPanel = new JPanel();
		add(superGroupPanel);
		superGroupPanel.setLayout(new BorderLayout(0, 0));
		
		superGroupButton = new JButton(data.getString("description"));
		superGroupButton.setFont(new Font("Tahoma", Font.BOLD, 24));
		superGroupButton.setForeground(DISABLED);
		superGroupPanel.add(superGroupButton);
		superGroupButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				superGroupActive = !superGroupActive;
				setAllGroupsAndButtons(superGroupActive);
			}
		});
		
		JSONArray key = data.getJSONArray("keys");
		for(int i = 0; i < key.length(); i++) keys.add(key.getString(i));
		
		groups = App.utils.getSuperGroupObjects(groupName);
		for(Group g : groups) {
			JPanel groupPanel = new JPanel();
			add(groupPanel);
			groupPanel.setLayout(new BorderLayout(0, 0));
			
			JButton groupButton = new JButton(g.getDescription());
			groupButton.setFont(new Font("Tahoma", Font.BOLD, 24));
			groupButton.setForeground(DISABLED);
			groupPanel.add(groupButton);
			buttons.add(groupButton);
			
			groupButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
	        		boolean newStatus = !g.getStatus();
					
		        	if(superGroupActive) {
		        		superGroupActive = false;
		        		newStatus = true;
		        	}
				          
				    if(newStatus) {
				        	setAllGroupsAndButtons(false);
				        	groupButton.setForeground(ENABLED);
				        	g.setStatus(true);
				    }
				}
			});
		}
		
		for(int i = 0; i < keys.size(); i++){
			Action a;
			if(i == 0){
				a = new Action(){
					@Override
					public void actionPerformed() {
						superGroupButton.doClick();
					}
				};
			} else {
				keybutton = buttons.get(i - 1);
				a = new Action() {
					private final JButton button = keybutton;
					@Override
					public void actionPerformed() {
						button.doClick();
					}
				};
			}
			mw.addListener(a, keys.get(i));
		}
	}

	public void clear(){
		setAllGroupsAndButtons(false);
		superGroupActive = false;
	}
	
	private void setAllGroupsAndButtons(boolean status){
		App.utils.changeSuperGroupStatus(groupName, status);
		
		if(status) superGroupButton.setForeground(ENABLED);
			else superGroupButton.setForeground(DISABLED);
		
		for(int i = 0; i < groups.size(); i++){
			if(status) buttons.get(i).setForeground(ENABLED);
				else buttons.get(i).setForeground(DISABLED);
		}
	}
}
