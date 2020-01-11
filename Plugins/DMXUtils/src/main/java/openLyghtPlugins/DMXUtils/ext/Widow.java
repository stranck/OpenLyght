package openLyghtPlugins.DMXUtils.ext;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.border.EmptyBorder;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import dmx.OpenLyght.App;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class Widow extends JFrame implements WindowListener {
	private JScrollBar scrollBar;
	private PrintStream printer;
	private StyledDocument doc;
	private JPanel contentPane;
	private JTextPane log;
	private Style style;
	
	public Widow() throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		setResizable(false);
		setBounds(100, 100, 699, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		log = new JTextPane();
		log.setBackground(Color.BLACK);
		log.setEditable(false);
		JScrollPane logPane = new JScrollPane(log, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollBar = logPane.getVerticalScrollBar();
		doc = log.getStyledDocument();
		contentPane.add(logPane, BorderLayout.CENTER);
		style = log.addStyle("style", null);
		StyleConstants.setForeground(style, Color.WHITE);
		
		printer = new Printer(System.out, this);
		System.setOut(printer);
		setVisible(true);
	}

	public void log(String s) throws Exception {
		String text =  "[" + App.time("dd/MM/yy HH:mm:ss") + "] " + s;
		doc.insertString(doc.getLength(), text, style);
		scrollBar.setValue(scrollBar.getMaximum());
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		if(JOptionPane.showConfirmDialog(null, "Are you really sure to disconnect from Arduino?","Open Lyght", JOptionPane.YES_NO_OPTION)
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
