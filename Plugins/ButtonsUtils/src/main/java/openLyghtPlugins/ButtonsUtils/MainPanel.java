package openLyghtPlugins.ButtonsUtils;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import javax.swing.JButton;
import java.awt.CardLayout;

@SuppressWarnings("serial")
public class MainPanel extends JPanel {

	public static final int BUTTON_WIDTH = 64;
	public ArrayList<SequencePanel> pages = new ArrayList<SequencePanel>();
	private CardLayout cardLayout = new CardLayout();
	private JButton changePageBtn;
	private JPanel pagePanel;
	private int currentPage = 0, pageCount = 0;
	
	public MainPanel() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		changePageBtn = new JButton();
		add(changePageBtn);
		changePageBtn.setMaximumSize(new Dimension(BUTTON_WIDTH, Integer.MAX_VALUE));
		changePageBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(++currentPage >= pages.size()) currentPage = 0;
				changePage(currentPage);
			}
		});
		
		pagePanel = new JPanel(cardLayout);
		add(pagePanel);
		//pagePanel.setMaximumSize(d);
		
		addPage(0);
		cardLayout.show(pagePanel, "0");
		updateButtonText();
		//changePage(0);
	}
	
	public void addSequence(JPanel panel, int page){
		if(page >= pages.size() || pages.get(page) == null)
			addPage(page);
		pages.get(page).add(panel);
	}
	
	private void changePage(int page){
		cardLayout.show(pagePanel, "" + page);
		currentPage = page;
		updateButtonText();
	}
	
	private void addPage(int page){
		SequencePanel sp = new SequencePanel();
		pages.add(page, sp);
		pagePanel.add(sp, "" + page);
		pageCount++;
		updateButtonText();
	}
	
	private void updateButtonText(){
		changePageBtn.setText((currentPage + 1) + " / " + pageCount);
	}
	
	public void initDimension(){
		setMaximumSize(new Dimension(Integer.MAX_VALUE, Main.openLyght.mainWindow.getVerticalSlotSize()));
	}
}