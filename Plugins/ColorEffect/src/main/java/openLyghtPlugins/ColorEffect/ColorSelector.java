package openLyghtPlugins.ColorEffect;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.JSONArray;
import org.json.JSONObject;

import dmx.OpenLyght.GUI.Action;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class ColorSelector extends JPanel {
	private ArrayList<EffectColor> colors = new ArrayList<EffectColor>();
	private JSONObject data; 
	private JButton btn;
	private int index = 0;
	private EffectColor selected;
	
	public ColorSelector(JSONObject data) throws Exception {
		setLayout(new BorderLayout());
		add(new JLabel("2nd color:"), BorderLayout.NORTH);
		this.data = data;
		
		btn = new JButton("");
		btn.setFont(new Font("Tahoma", Font.BOLD, 18));
		add(btn, BorderLayout.CENTER);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(++index >= colors.size()) index = 0;
				go();
			}
		});
		
		loadColors(data.getJSONArray("colors"));
		go();
	}

	public EffectColor getSelected(){
		return selected;
	}
	
	private void go(){
		EffectColor ec = colors.get(index);
		btn.setForeground(ec.getColorLabel());
		btn.setText(ec.toString());
		selected = ec;
	}
	private void loadColors(JSONArray colors){
		for(int i = 0; i < colors.length(); i++)
			this.colors.add(new EffectColor(colors.getJSONObject(i)));
	}
	
	public void loadKeyListeners() throws Exception{
		Main.openLyght.mainWindow.addListener(new Action(){
			public void actionPerformed() {
				btn.doClick();
			}
		}, data.getString("go+Key"));
		Main.openLyght.mainWindow.addListener(new Action(){
			public void actionPerformed() {
				if((index -= 2) < -1) index = colors.size() - 2;
				btn.doClick();
			}
		}, data.getString("go-Key"));
		Main.openLyght.mainWindow.addListener(new Action(){
			public void actionPerformed() {
				new Thread(){
					public void run(){
						index = -1;
						btn.doClick();
					}
				}.start();
			}
		}, data.getString("resetKey"));
		data = null;
	}
}
