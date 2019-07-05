package openLyghtPlugins.DMXUtils;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class PagePanel extends JPanel {

	private static final Color DISABLED = new Color(0, 0, 64);
	private static final Color ENABLED = new Color(0, 192, 0);
	
	public static ArrayList<String> faders = new ArrayList<String>();
	private static ArrayList<JButton> buttons = new ArrayList<JButton>();
	public static JComboBox<String> faderComboBox;
	private static JPanel btnPanel = new JPanel();
	private JPanel faderPanel;
	
	public PagePanel() {
		setMaximumSize(new Dimension(Integer.MAX_VALUE, Main.openLyght.mainWindow.getVerticalSlotSize()));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JPanel buttonPanel = new JPanel();
		add(buttonPanel);
		buttonPanel.setLayout(new BorderLayout(0, 0));
		
		JLabel buttonText = new JLabel("Button page:");
		buttonPanel.add(buttonText, BorderLayout.NORTH);
		
		buttonPanel.add(btnPanel, BorderLayout.CENTER);
		btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
		
		faderPanel = new JPanel();
		btnPanel.add(faderPanel);
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
		
		Main.openLyght.mainWindow.addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent e) {}
			@Override
			public void componentMoved(ComponentEvent e) {}
			@Override
			public void componentHidden(ComponentEvent e) {}
			@Override
			public void componentResized(ComponentEvent e) {
				setMaximumSize(new Dimension(Integer.MAX_VALUE, Main.openLyght.mainWindow.getVerticalSlotSize()));
				double baseSize = e.getComponent().getSize().getWidth() / (buttons.size() + 1);
				for(JButton b : buttons)
					resizeComponent(b, baseSize);
			}
		});
		
		setButtonIndex(0);
		
		/*faderComboBox.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				System.out.println("RELEASED:\t" + faderComboBox.getSelectedItem());
			}
			@Override
			public void mousePressed(MouseEvent e) {
				System.out.println("PRESSED:\t" + faderComboBox.getSelectedItem());
			}
			@Override
			public void mouseExited(MouseEvent e) {
				System.out.println("EXITED: \t" + faderComboBox.getSelectedItem());
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				System.out.println("ENTERED:\t" + faderComboBox.getSelectedItem());
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("CLICKED:\t" + faderComboBox.getSelectedItem());
			}
		});*/
	}

	private void resizeComponent(JButton b, double slotSize){
		Dimension d = new Dimension((int) slotSize, Main.openLyght.mainWindow.getVerticalSlotSize());
		b.setPreferredSize(d);
		b.setMaximumSize(d);
		b.setMinimumSize(d);
	}
	
	public static void setButtonIndex(int index){
		Main.buttonPage = index;
		for(int i = 0; i < buttons.size(); i++){
			if(i == index)
				buttons.get(i).setForeground(ENABLED);
			else
				buttons.get(i).setForeground(DISABLED);
		}
	}
	
	public static void addButton(String text){
		int pos = text.lastIndexOf(".");
		if(pos > 0) text = text.substring(0, pos);
		JButton button = new JButton(text);
		button.setFont(new Font("Tahoma", Font.BOLD, 24));
		final int btnIndex = buttons.size();
		btnPanel.add(button);
		buttons.add(button);
		//ButtonList.addMouseListener(button); //ASDDDDDDDDDDDDDDDDDDDD
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setButtonIndex(btnIndex);
			}
		});
	}
}
