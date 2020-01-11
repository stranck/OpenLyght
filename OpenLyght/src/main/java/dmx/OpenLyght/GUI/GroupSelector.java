package dmx.OpenLyght.GUI;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.json.JSONArray;
import org.json.JSONObject;

import dmx.OpenLyght.App;
import dmx.OpenLyght.Group;
import dmx.OpenLyght.Utils.Action;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class GroupSelector extends JPanel {
	public static final long LIMIT_MULTISELECT_TIME = 30 * 1000;
	public static final Color DISABLED = new Color(255, 0, 0);
	public static final Color ENABLED = new Color(0, 192, 0);
	private ArrayList<Group> groups = new ArrayList<Group>();
	private ArrayList<String> keys = new ArrayList<String>();
	private ArrayList<JButton> buttons = new ArrayList<JButton>();
	private boolean superGroupActive = true;
	private long lastClicked;
	private JButton superGroupButton, keybutton;
	private String groupName;
	
	public GroupSelector(MainWindow mw, JSONObject data) throws Exception {
		setMaximumSize(new Dimension(Integer.MAX_VALUE, App.utils.mainWindow.getVerticalSlotSize()));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		groupName = data.getString("superGroupName");
		
		JPanel superGroupPanel = new JPanel();
		add(superGroupPanel);
		superGroupPanel.setLayout(new BorderLayout(0, 0));
		
		superGroupButton = new JButton(data.getString("description"));
		superGroupButton.setFont(new Font("Tahoma", Font.BOLD, 24));
		superGroupButton.setForeground(ENABLED);
		superGroupPanel.add(superGroupButton);
		superGroupButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!superGroupActive){
					//superGroupActive = !superGroupActive;
					//setAllGroupsAndButtons(superGroupActive);
					superGroupActive = true;
					setAllGroupsAndButtons(true);
				}
				lastClicked = 0;
			}
		});
		//ButtonList.addMouseListener(superGroupButton);
		
		JSONArray key = data.getJSONArray("keys");
		for(int i = 0; i < key.length(); i++) keys.add(key.getString(i));
		
		Action sub = new Action(){
			@Override
			public void actionPerformed() {
				lastClicked = 0;
			}
		};
		groups = App.utils.getSuperGroupObjects(groupName);
		for(Group g : groups) {
			JPanel groupPanel = new JPanel();
			add(groupPanel);
			groupPanel.setLayout(new BorderLayout(0, 0));
			
			JButton groupButton = new JButton(g.getDescription());
			groupButton.setFont(new Font("Tahoma", Font.BOLD, 24));
			groupButton.setForeground(ENABLED);
			//ButtonList.addMouseListener(groupButton);// ASDDDDDDDDDDDDDDDDDDDDDDDDd
			groupPanel.add(groupButton);
			buttons.add(groupButton);
			
			groupButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					long rn = System.currentTimeMillis();
	        		boolean newStatus = !g.getStatus();
					
		        	if(superGroupActive || rn - lastClicked > LIMIT_MULTISELECT_TIME) {
		        		superGroupActive = false;
		        		newStatus = true;
		        		setAllGroupsAndButtons(false);
		        	}

				    groupButton.setForeground(newStatus ? ENABLED : DISABLED);
			        g.setStatus(newStatus);
				    lastClicked = rn;
					/*boolean newStatus = !g.getStatus();
					
		        	if(superGroupActive) {
		        		superGroupActive = false;
		        		newStatus = true;
		        	}
				          
				    if(newStatus) {
				        	setAllGroupsAndButtons(false);
				        	groupButton.setForeground(ENABLED);
				        	g.setStatus(true);
				    }*/

				}
			});
			g.subscribeToUsedGroup(sub);
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
		
		/*mw.addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent e) {}
			@Override
			public void componentMoved(ComponentEvent e) {}
			@Override
			public void componentHidden(ComponentEvent e) {}
			@Override
			public void componentResized(ComponentEvent e) {
				setMaximumSize(new Dimension(Integer.MAX_VALUE, App.utils.mainWindow.getVerticalSlotSize()));
				for(JButton b : buttons){
				}
			}
		});*/
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
