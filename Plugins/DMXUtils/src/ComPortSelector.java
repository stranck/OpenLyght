package openLyghtPlugins.DMXUtils;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class ComPortSelector extends JFrame {

	private JPanel contentPane;

	public ComPortSelector() {
		super("DMXUtils - OpenLyght plugin");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 168);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		JPanel reciverPanel = new JPanel();
		contentPane.add(reciverPanel);
		reciverPanel.setLayout(new BorderLayout(0, 0));
		
		JLabel reciverText = new JLabel(" Reciver: ");
		reciverPanel.add(reciverText, BorderLayout.WEST);
		
		JComboBox<String> reciverComboBox = new JComboBox<String>();
		reciverPanel.add(reciverComboBox, BorderLayout.CENTER);
		
		JPanel senderPanel = new JPanel();
		contentPane.add(senderPanel);
		senderPanel.setLayout(new BorderLayout(0, 0));
		
		JLabel senderText = new JLabel(" Sender:  ");
		senderPanel.add(senderText, BorderLayout.WEST);
		
		JComboBox<String> senderComboBox = new JComboBox<String>();
		senderPanel.add(senderComboBox, BorderLayout.CENTER);
		
		JPanel okPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) okPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		contentPane.add(okPanel);
		
		JButton confirmButton = new JButton("Ok");
		okPanel.add(confirmButton);
		confirmButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		setVisible(true);
	}

}
