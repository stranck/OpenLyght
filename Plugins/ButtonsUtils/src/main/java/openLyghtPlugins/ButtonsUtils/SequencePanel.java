package openLyghtPlugins.ButtonsUtils;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SequencePanel extends JPanel {
	
	public SequencePanel() {
		setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	}
	
	public void addSequence(JPanel panel){
		add(panel);
	}

}
