package dmx.OpenLyght.GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class AutoSuggestor extends JPanel{
	private final JTextField tf;
    private final JComboBox<String> combo = new JComboBox<String>();
    private boolean hide_flag = false;
    
    public AutoSuggestor(Vector<String> v) {
        super(new BorderLayout());
        combo.setEditable(true);
        tf = (JTextField) combo.getEditor().getEditorComponent();
        tf.addKeyListener(new KeyAdapter() {
        	public void keyTyped(KeyEvent e) {
        		EventQueue.invokeLater(new Runnable() {
        			public void run() {
        				String text = tf.getText();
                        if(text.length()==0) {
                        	combo.hidePopup();
                        	setModel(new DefaultComboBoxModel<String>(v), "");
                        } else {
                        	DefaultComboBoxModel<String> m = getSuggestedModel(v, text);
                        	if(m.getSize()==0 || hide_flag) {
                        		combo.hidePopup();
                        		hide_flag = false;
                        	} else {
                        		setModel(m, text);
                        		combo.showPopup();
                        	}
                        }
        			}
        		});
        	}
        	public void keyPressed(KeyEvent e) {
            	String text = tf.getText();
            	int code = e.getKeyCode();
            	if(code==KeyEvent.VK_ENTER) {
            		if(!v.contains(text)) {
            			v.addElement(text);
            			Collections.sort(v);
            			setModel(getSuggestedModel(v, text), text);
            		}
            		hide_flag = true; 
            	}else if(code==KeyEvent.VK_ESCAPE) {
            		hide_flag = true; 
            	}else if(code==KeyEvent.VK_RIGHT) {
            		for(int i=0;i<v.size();i++) {
            			String str = v.elementAt(i);
            			if(str.startsWith(text)) {
            				combo.setSelectedIndex(-1);
            				tf.setText(str);
            				return;
            			}
            		}
            	}
        	}
        });
        setModel(new DefaultComboBoxModel<String>(v), "");
        add(combo);
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
    }
    private void setModel(DefaultComboBoxModel<String> mdl, String str) {
       	combo.setModel(mdl);
       	combo.setSelectedIndex(-1);
       	tf.setText(str);
    }
    private static DefaultComboBoxModel<String> getSuggestedModel(java.util.List<String> list, String text) {
    	DefaultComboBoxModel<String> m = new DefaultComboBoxModel<String>();
    	for(String s: list) {
    		if(s.startsWith(text)) m.addElement(s);
    	}
    	return m;
    }
    
    public String getInput(){
    	return tf.getText();
    }
    
    public void setText(String text){
    	combo.getEditor().setItem(text);
    }
}