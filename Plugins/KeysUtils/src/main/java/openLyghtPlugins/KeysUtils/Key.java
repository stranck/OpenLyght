package openLyghtPlugins.KeysUtils;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.json.JSONArray;
import org.json.JSONObject;

import dmx.OpenLyght.App;
import dmx.OpenLyght.GUI.MainWindow;
import dmx.OpenLyght.Utils.Wait;

public class Key {
	private static final Font FONT = new Font("Tahoma", Font.BOLD, 18);
	
	private static MainWindow mw;
	private JPanel panel;
	private int waitTime;
	
	public Key(JSONObject data, JPanel fatherPane) throws Exception{
		String name = data.getString("name");
		System.out.println("Loading emuKey: " + name);
		
		final ArrayList<Integer> keys = new ArrayList<Integer>();
		JSONArray arr = data.getJSONArray("keys");
		for(int i = 0; i < arr.length(); i++)
			keys.add(App.getKeyEvent(arr.getString(i)));
		
		waitTime = 0;
		if(data.has("wait"))
			waitTime = data.getInt("wait");	
		
		JButton b = new JButton(name);
		panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 0));
		panel.add(b, BorderLayout.CENTER);
		fatherPane.add(panel);
		b.setFont(FONT);
		//ButtonList.addMouseListener(b);
		
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(){
					public void run(){
						for(int i : keys){
							mw.keyClicked(i);
							if(waitTime > 0)
								Wait.wait(waitTime);
						}
					}
				}.start();
			}
		});
	}
	
	public static void setMainWindow(MainWindow mainWindow){
		mw = mainWindow;
	}
	
	public JPanel getPanel(){
		return panel;
	}
}
