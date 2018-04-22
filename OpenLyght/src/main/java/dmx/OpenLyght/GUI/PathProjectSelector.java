package dmx.OpenLyght.GUI;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.json.JSONArray;

import dmx.OpenLyght.App;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class PathProjectSelector extends JFrame {

	private JPanel contentPane;
	private AutoSuggestor as;
	private JSONArray paths;
	private Path configFile = Paths.get("recentProjectPaths.json");

	public PathProjectSelector() throws Exception {
		super("OpenLyght - Choose project path");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 135);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel textPanel = new JPanel();
		FlowLayout fl_textPanel = (FlowLayout) textPanel.getLayout();
		fl_textPanel.setAlignment(FlowLayout.LEFT);
		contentPane.add(textPanel, BorderLayout.NORTH);
		
		JLabel text = new JLabel("Project path:");
		textPanel.add(text);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		contentPane.add(panel, BorderLayout.SOUTH);
		
		JButton confirm = new JButton("OK");
		panel.add(confirm);
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String path = as.getInput();
					App.utils.deafaultPath = path;
					updateRecentPaths(path);
					App.utils.start();
					dispose();
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "An error occurred:\n" + e1, "OpenLyght - ERROR", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		JPanel pathPanel = new JPanel();
		contentPane.add(pathPanel, BorderLayout.CENTER);
		pathPanel.setLayout(new BorderLayout(0, 0));
		
		paths = new JSONArray(new String(Files.readAllBytes(configFile)));
		Vector<String> v = new Vector<String>();
		for(int i = paths.length() - 1; i >= 0; i--)
			v.addElement(paths.getString(i));
		as = new AutoSuggestor(v);
		if(paths.length() > 0) {
			Path p = Paths.get("recentPathIndex.ini");
			if(Files.exists(p))
				as.setText(new String(Files.readAllBytes(p)));
			else {
				String s = paths.getString(paths.length() - 1);
				Files.write(p, s.getBytes(), CREATE, TRUNCATE_EXISTING);
				as.setText(s);
			}
		}
		pathPanel.add(as, BorderLayout.CENTER);
		
		/*JPanel pathButtonPanel = new JPanel();
		as.add(pathButtonPanel, BorderLayout.EAST);
		
		JButton pathButton = new JButton("Choose");
		pathButtonPanel.add(pathButton);
		pathButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileDialog fd = new FileDialog((Frame) null, "OpenLyght - Choose project directory", FileDialog.SAVE);
				fd.setFile("guiElements.josn");
				fd.setVisible(true);
				if(fd.getFile() != null) as.setText(fd.getDirectory() + fd.getFile());
			}
		});*/
		
		setVisible(true);
	}

	private void updateRecentPaths(String s) throws Exception {
		if(notContains(s)){
			paths.put(s);
			if(paths.length() > 10) paths.remove(0);
		}
		Files.write(configFile, paths.toString().getBytes(), TRUNCATE_EXISTING);
		Files.write(Paths.get("recentPathIndex.ini"), s.getBytes(), CREATE, TRUNCATE_EXISTING);
	}
	
	private boolean notContains(String s){
		boolean b = true;
		for(int i = 0; i < paths.length() && b; i++){
			if(paths.getString(i).equals(s)) b = false;
			System.out.println(paths.getString(i) + " " + s);
		}
		return b;
	}
}
