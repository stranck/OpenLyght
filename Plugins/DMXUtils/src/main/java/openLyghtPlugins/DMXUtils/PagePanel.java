package openLyghtPlugins.DMXUtils;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class PagePanel extends JPanel {

	public static ArrayList<String> buttons = new ArrayList<String>();
	public static ArrayList<String> faders = new ArrayList<String>();
	
	public static JComboBox<String> buttonComboBox;
	public static JComboBox<String> faderComboBox;
	
	public PagePanel() {
		setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JPanel faderPanel = new JPanel();
		add(faderPanel);
		faderPanel.setLayout(new BorderLayout(0, 0));
		
		JLabel faderText = new JLabel("Fader page:");
		faderPanel.add(faderText, BorderLayout.NORTH);
		
		faderComboBox = new JComboBox<String>();
		faderPanel.add(faderComboBox, BorderLayout.CENTER);
		faderComboBox.setFont(new Font("Tahoma", Font.BOLD, 24));
		for(String s : faders){
			int pos = s.lastIndexOf(".");
			if(pos > 0) s = s.substring(0, pos);
			faderComboBox.addItem(s);
		}
		faderComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.faderPage = faderComboBox.getSelectedIndex();
			}
		});
		
		JPanel buttonPanel = new JPanel();
		add(buttonPanel);
		buttonPanel.setLayout(new BorderLayout(0, 0));
		
		JLabel buttonText = new JLabel("Button page:");
		buttonPanel.add(buttonText, BorderLayout.NORTH);
		
		buttonComboBox = new JComboBox<String>();
		buttonPanel.add(buttonComboBox, BorderLayout.CENTER);
		buttonComboBox.setFont(new Font("Tahoma", Font.BOLD, 24));
		for(String s : buttons){
			int pos = s.lastIndexOf(".");
			if(pos > 0) s = s.substring(0, pos);
			buttonComboBox.addItem(s);
		}
		buttonComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.buttonPage = buttonComboBox.getSelectedIndex();
			}
		});
	}

}
