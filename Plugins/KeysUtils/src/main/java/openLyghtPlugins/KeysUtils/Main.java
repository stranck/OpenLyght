package openLyghtPlugins.KeysUtils;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.json.JSONArray;

import dmx.OpenLyght.Plugin;
import dmx.OpenLyght.Stuff;

public class Main implements Plugin {

	private final String[] tags = { "emulate", "Input", "keyboard" };
	private final String name = "KeysUtils";
	public static String defaultPath;
	public static Stuff openLyght;
	
	public ArrayList<Key> buttons = new ArrayList<Key>();
	public JPanel panel;
	
	public Main(Stuff ol){
		try {
			System.out.println("LOADING: keysutils plugin");
			openLyght = ol;
			defaultPath = ol.deafaultPath + "plugins" + File.separator + name + File.separator;
			
			panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
			//System.out.println(ol + " " + ol.mainWindow);
			//panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, ol.mainWindow.getVerticalSlotSize()));
			
			JSONArray keys = new JSONArray(ol.read(defaultPath + "keys.json"));
			for(int i = 0; i < keys.length(); i++)
				buttons.add(new Key(keys.getJSONObject(i), panel));
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	
	public String getName() {
		return name;
	}

	public String[] getTags() {
		return tags;
	}

	@Override
	public void message(String message) {
		if(message.equalsIgnoreCase("openlyght started")){
			try{
				System.out.println("Keys utils: loading window panels");
				panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, openLyght.mainWindow.getVerticalSlotSize()));
				Key.setMainWindow(openLyght.mainWindow);
				openLyght.mainWindow.addPanel(panel, "emuKeyPanel");
				
				openLyght.mainWindow.addComponentListener(new ComponentListener() {
					@Override
					public void componentShown(ComponentEvent e) {}
					@Override
					public void componentMoved(ComponentEvent e) {}
					@Override
					public void componentHidden(ComponentEvent e) {}
					@Override
					public void componentResized(ComponentEvent e) {
						int v = openLyght.mainWindow.getVerticalSlotSize();
						panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, v));
						Dimension d = new Dimension((int) (e.getComponent().getSize().getWidth() / buttons.size()), v);
						for(int i = 1; i < buttons.size(); i++){
							JPanel b = buttons.get(i).getPanel();
							b.setMinimumSize(d);
							b.setMaximumSize(d);
							b.setPreferredSize(d);
						}
					}
				});
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}

}
