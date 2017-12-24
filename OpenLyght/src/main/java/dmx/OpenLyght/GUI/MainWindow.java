package dmx.OpenLyght.GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import dmx.OpenLyght.App;

import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ActionMap;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JComponent;

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements WindowListener {

	private ArrayList<Listener> listeners = new ArrayList<Listener>();
	private JScrollPane scrollPane;
	private JPanel contentPane;
	private JPanel panel = new JPanel();
	private ActionMap action;
	private InputMap input;
	
	public MainWindow() throws Exception {
		super("Open Lyght");
		
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		//contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		Dimension d = new Dimension(100, 100);
		contentPane.setLayout(new BorderLayout(0, 0));
		panel.setMinimumSize(d);
		panel.setPreferredSize(d);
		
		scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		contentPane.add(scrollPane);
		
		setVisible(true);
		action = contentPane.getActionMap();
		input = contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		loadElements();
	}
	
	private void loadElements() throws Exception {
			JSONObject data = new JSONObject(new String(Files.readAllBytes(Paths.get(App.utils.deafaultPath + "guiElements.json"))));
			JSONArray effects = data.getJSONArray("effects");
			for(int i = 0; i < effects.length(); i++)
				panel.add(
						new EffectController(this, new JSONObject(new String(
							Files.readAllBytes(Paths.get(App.utils.deafaultPath + effects.getString(i)))))));
			
			JSONArray groups = data.getJSONArray("groups");
			for(int i = 0; i < groups.length(); i++)
				panel.add(
						new GroupSelector(this, new JSONObject(new String(
							Files.readAllBytes(Paths.get(App.utils.deafaultPath + groups.getString(i)))))));
	}
	
	public void addPanel(JPanel panel){
		this.panel.add(panel);
		scrollPane.validate();
	}
	
	public void addListener(Action listener, String keyName) throws Exception{
		Listener l = null;
		int key = App.getKeyEvent(keyName);
		for(Listener ls : listeners)
			if(ls.getKeyCode() == key){
				l = ls;
				break;
			}
		if(l == null){
			l = new Listener(key);
			listeners.add(l);
		    input.put(KeyStroke.getKeyStroke(key, 0), keyName);
		    action.put(keyName, l);
		}
		l.addAction(listener);
	}
	
	public void addListener(KeyListener listener){
		setFocusable(true);
		addKeyListener(listener);
	}

	@Override
	public void windowClosing(WindowEvent e) {
		if(JOptionPane.showConfirmDialog(null, "Are you really sure to close Open Lyght?","Open Lyght",JOptionPane.YES_NO_OPTION)
			== JOptionPane.YES_OPTION) System.exit(0);
	}
	
	@Override
	public void windowActivated(WindowEvent e) {}
	@Override
	public void windowClosed(WindowEvent e) {}
	@Override
	public void windowDeactivated(WindowEvent e) {}
	@Override
	public void windowDeiconified(WindowEvent e) {}
	@Override
	public void windowIconified(WindowEvent e) {}
	@Override
	public void windowOpened(WindowEvent e) {}
}
