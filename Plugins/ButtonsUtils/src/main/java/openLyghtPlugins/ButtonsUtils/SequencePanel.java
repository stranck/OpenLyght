package openLyghtPlugins.ButtonsUtils;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SequencePanel extends JPanel {
	//private static int i = 0;
	
	public SequencePanel() {
		setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		/*Color[] c = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};
		setBackground(c[i++]);
		System.out.println("COLOR: " + i);*/
	}
}
