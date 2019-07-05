package openLyghtPlugins.ButtonsUtils;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.util.ArrayList;

import org.json.JSONObject;

import dmx.OpenLyght.Plugin;
import dmx.OpenLyght.Stuff;
import dmx.OpenLyght.Utils.Scene;

public class Main implements Plugin {

	private final String[] tags = { "buttons", "Input", "keyboard" };
	private final String name = "ButtonsUtils";
	public static MainPanel sequencePanel = new MainPanel();
	public static ArrayList<Scene> scenes = new ArrayList<Scene>();
	public static ArrayList<Sequence> sequences = new ArrayList<Sequence>();
	public static Button[] buttons = new Button[12];
	public static String defaultPath;
	public static Stuff openLyght;
	
	public Main(Stuff ol){
		try {
			System.out.println("LOADING: buttonsutils plugin");
			openLyght = ol;
			defaultPath = ol.deafaultPath + "plugins" + File.separator + name + File.separator;
			
			File[] dir = new File(defaultPath + "scenes" + File.separator).listFiles(File::isFile);
			for(File f : dir){
				System.out.println("Loading scene: " + f.getAbsolutePath());
				scenes.add(new Scene(f.getAbsolutePath()));
			}
			
			for(int i = 0; i < 12; i++)
				buttons[i] = new Button();
			for(int i = 0; i < 12; i++)
				buttons[i].load(new JSONObject(openLyght.read(defaultPath + "buttons" + File.separator + i + ".json")));
			
			JSONObject comPorts = new JSONObject(openLyght.read(defaultPath + "serialPorts.json"));
			new ButtonInput(comPorts.getString("buttons")){
				@Override
				public void newData(int button, boolean status){
					buttons[button].update(status);
				}
			};
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Scene getScene(String name){
		Scene s = null;
		for(Scene scene : scenes)
			if(scene.getName().equals(name)) {
				s = scene;
				break;
			}
		return s;
	}
	
	@Override
	public String getName() {
		return name;
	}
	@Override
	public String[] getTags() {
		return tags;
	}

	public void message(String message) {
		if(message.equalsIgnoreCase("openlyght started")){
			try{
				System.out.println("Buttons utils: loading window panels");
				sequencePanel.initDimension();
				openLyght.mainWindow.addPanel(sequencePanel, "sequencePanel");
				
				File[] dir = new File(defaultPath + "sequences" + File.separator).listFiles(File::isFile);
				for(File f : dir){
					sequences.add(new Sequence(new JSONObject(openLyght.read(f.getAbsolutePath()))));
				}
				
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
						sequencePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, v));
						for(SequencePanel sp : sequencePanel.pages){
							Component[] cmps = sp.getComponents();
							Dimension d = new Dimension((int) ((e.getComponent().getSize().getWidth() - MainPanel.BUTTON_WIDTH) / cmps.length), v);
							for(Component c : cmps){
								c.setMinimumSize(d);
								c.setMaximumSize(d);
								c.setPreferredSize(d);
							}
							//resizePanel(reset, slotSize, 3);
						}
					}
				});
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public MainPanel getSequencePanel(){
		return sequencePanel;
	}
}
