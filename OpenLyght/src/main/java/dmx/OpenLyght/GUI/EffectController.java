package dmx.OpenLyght.GUI;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.json.JSONArray;
import org.json.JSONObject;

import dmx.OpenLyght.App;
import dmx.OpenLyght.GUI.ComboxShit.*;
import dmx.OpenLyght.Utils.Action;
import dmx.OpenLyght.Utils.EffectsEngine;

@SuppressWarnings("serial")
public class EffectController extends JPanel {
	private ButtonList effectBtn;
	private ButtonList lightsGroupBtn;
	private ButtonList phaseBtn;
	private ButtonList directionBtn;
	private ButtonList groupsBtn;
	private ButtonList blocksBtn;
	private ButtonList wingsBtn;
	private JSpinner phaseStartSpinner;
	private JSpinner phaseEndSpinner;
	private JSpinner groupsSpinner;
	private JSpinner blocksSpinner;
	private JSpinner wingsSpinner;
	private JTextField directionTextField;
	private JButton resetButton;
	
	private MainWindow mw;
	private ArrayList<EffectsEngine> effect = new ArrayList<EffectsEngine>();
	private ArrayList<Thread> thread = new ArrayList<Thread>();
	private boolean allowChangeListener = true;
	private int efctN;
	
	private PhaseCombo customPhaseCombo = new PhaseCombo("Custom");
	private CharacterZ customDirection = new CharacterZ("Custom");
	private Numbers customNumbers = new Numbers("Custom");

	public EffectController(MainWindow mw, JSONObject data) throws Exception {
		this.mw = mw;
		
		System.out.println("Loading effect controller");
		setMaximumSize(new Dimension(Integer.MAX_VALUE, mw.getVerticalSlotSize()));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JPanel lightsGroup = new JPanel();
		add(lightsGroup);
		lightsGroup.setLayout(new BorderLayout(0, 0));
		
		JLabel lightsGroupText = new JLabel("Light group:");
		lightsGroup.add(lightsGroupText, BorderLayout.NORTH);
		
		
		
		JSONArray effectsData = data.getJSONArray("effectData");
		efctN = effectsData.length();
		
		lightsGroupBtn = new ButtonList();
		lightsGroup.add(lightsGroupBtn, BorderLayout.CENTER);
		JSONArray lightsGroupJumps = null;
		if(data.has("lightsGroupJumps"))
			data.getJSONArray("lightsGroupJumps");
		loadGroupList(lightsGroupBtn, data.getJSONArray("lightsGroups"), lightsGroupJumps);
		lightsGroupBtn.addAction(new Action() {	
			@Override
			public void actionPerformed() {
				lightsGroupChange();
			}
		});
		
		JPanel effectType = new JPanel();
		add(effectType);
		effectType.setLayout(new BorderLayout(0, 0));
		
		JLabel effectTypeText = new JLabel("Effect:");
		effectType.add(effectTypeText, BorderLayout.NORTH);
		
		effectBtn = new ButtonList();
		effectType.add(effectBtn, BorderLayout.CENTER);
		JSONArray effectTypeJumps = null;
		if(data.has("effectTypeJumps"))
			data.getJSONArray("effectTypeJumps");
		loadEffectsList(effectBtn, data.getJSONArray("effects"), effectTypeJumps);
		effectBtn.addAction(new Action() {	
			@Override
			public void actionPerformed() {
				effectChange();
			}
		});
		
		JPanel phaseRange = new JPanel();
		add(phaseRange);
		phaseRange.setLayout(new BorderLayout(0, 0));
		
		JPanel phaseSubPanel = new JPanel();
		phaseRange.add(phaseSubPanel, BorderLayout.SOUTH);
		phaseSubPanel.setLayout(new BoxLayout(phaseSubPanel, BoxLayout.X_AXIS));
		phaseSubPanel.setPreferredSize(new Dimension(40, 20));
		
		phaseStartSpinner = new JSpinner();
		phaseSubPanel.add(phaseStartSpinner);
		phaseStartSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(allowChangeListener){
					phaseBtn.setIndex(customPhaseCombo);
					for(int i = 0; i < efctN; i++)
						effect.get(i).setPhaseStart((int) phaseStartSpinner.getValue());
				}
			}
		});
		
		phaseEndSpinner = new JSpinner();
		phaseSubPanel.add(phaseEndSpinner);
		phaseEndSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(allowChangeListener){
					phaseBtn.setIndex(customPhaseCombo);
					for(int i = 0; i < efctN; i++)
						effect.get(i).setPhaseEnd((int) phaseEndSpinner.getValue());
				}
			}
		});
		
		JLabel phaseText = new JLabel("Phase:");
		phaseRange.add(phaseText, BorderLayout.NORTH);
		
		phaseBtn = new ButtonList();
		phaseRange.add(phaseBtn, BorderLayout.CENTER);
		JSONArray phaseRangeJumps = null;
		if(data.has("phaseRangeJumps"))
			data.getJSONArray("phaseRangeJumps");
		loadPhaseList(phaseBtn, data.getJSONArray("phases"), phaseRangeJumps);
		phaseBtn.addAction(new Action() {
			@Override
			public void actionPerformed() {
				phaseChange();
			}
		});
		
		JPanel direction = new JPanel();
		add(direction);
		direction.setLayout(new BorderLayout(0, 0));
		
		JLabel directionText = new JLabel("Direction:");
		direction.add(directionText, BorderLayout.NORTH);
		
		directionBtn = new ButtonList();
		direction.add(directionBtn, BorderLayout.CENTER);
		JSONArray directionJumps = null;
		if(data.has("directionJumps"))
			data.getJSONArray("directionJumps");
		loadDirectionsList(directionBtn, data.getJSONArray("directions"), directionJumps);
		directionBtn.addAction(new Action() {
			@Override
			public void actionPerformed() {
				directionChange();
			}
		});
		
		directionTextField = new JTextField();
		directionTextField.setHorizontalAlignment(SwingConstants.CENTER);
		direction.add(directionTextField, BorderLayout.SOUTH);
		directionTextField.setColumns(1);
		directionTextField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				if(allowChangeListener){
					char c = directionTextField.getText().charAt(0);
					if(c == '<' || c == '>' || c == 'S' || c == 's'){
						directionBtn.setIndex(customDirection);
						for(int i = 0; i < efctN; i++)
							effect.get(i).setDirection(c);
					}
				}
			}
			@Override
			public void removeUpdate(DocumentEvent e) {}
			@Override
			public void changedUpdate(DocumentEvent e) {}
		});
		
		JPanel groups = new JPanel();
		add(groups);
		groups.setLayout(new BorderLayout(0, 0));
		
		JLabel groupText = new JLabel("Groups:");
		groups.add(groupText, BorderLayout.NORTH);
		
		groupsSpinner = new JSpinner();
		groups.add(groupsSpinner, BorderLayout.SOUTH);
		groupsSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(allowChangeListener){
					groupsBtn.setIndex(customNumbers);
					for(int i = 0; i < efctN; i++)
						effect.get(i).setGroups((int) groupsSpinner.getValue());
				}
			}
		});
		
		groupsBtn = new ButtonList();
		groups.add(groupsBtn, BorderLayout.CENTER);
		JSONArray groupsJumps = null;
		if(data.has("groupsJumps"))
			data.getJSONArray("groupsJumps");
		loadNumbersList(groupsBtn, data.getJSONArray("groups"), groupsJumps);
		groupsBtn.addAction(new Action() {
			@Override
			public void actionPerformed() {
				for(int i = 0; i < efctN; i++)
					effect.get(i).setGroups(numbersChange(groupsBtn, groupsSpinner, i));
			}
		});
		
		JPanel blocks = new JPanel();
		add(blocks);
		blocks.setLayout(new BorderLayout(0, 0));
		
		JLabel blockText = new JLabel("Blocks:");
		blocks.add(blockText, BorderLayout.NORTH);
		
		blocksSpinner = new JSpinner();
		blocks.add(blocksSpinner, BorderLayout.SOUTH);
		blocksSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(allowChangeListener){
					blocksBtn.setIndex(customNumbers);
					for(int i = 0; i < efctN; i++)
						effect.get(i).setBlocks((int) blocksSpinner.getValue());
				}
			}
		});
		
		blocksBtn = new ButtonList();
		blocks.add(blocksBtn, BorderLayout.CENTER);
		JSONArray blocksJumps = null;
		if(data.has("blocksJumps"))
			data.getJSONArray("blocksJumps");
		loadNumbersList(blocksBtn, data.getJSONArray("blocks"), blocksJumps);
		blocksBtn.addAction(new Action() {
			@Override
			public void actionPerformed() {
				for(int i = 0; i < efctN; i++)
					effect.get(i).setBlocks(numbersChange(blocksBtn, blocksSpinner, i));
			}
		});
		
		JPanel wings = new JPanel();
		add(wings);
		wings.setLayout(new BorderLayout(0, 0));
		
		JLabel wingText = new JLabel("Wings:");
		wings.add(wingText, BorderLayout.NORTH);
		
		wingsSpinner = new JSpinner();
		wings.add(wingsSpinner, BorderLayout.SOUTH);
		wingsSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(allowChangeListener){
					wingsBtn.setIndex(customNumbers);
					for(int i = 0; i < efctN; i++)
						effect.get(i).setWings((int) wingsSpinner.getValue());
				}
			}
		});
		
		wingsBtn = new ButtonList();
		wings.add(wingsBtn, BorderLayout.CENTER);
		JSONArray wingsJumps = null;
		if(data.has("wingsJumps"))
			data.getJSONArray("wingsJumps");
		loadNumbersList(wingsBtn, data.getJSONArray("wings"), wingsJumps);
		wingsBtn.addAction(new Action() {
			@Override
			public void actionPerformed() {
				for(int i = 0; i < efctN; i++)
					effect.get(i).setWings(numbersChange(wingsBtn, wingsSpinner, i));
			}
		});
		
		JPanel reset = new JPanel();
		add(reset);
		reset.setLayout(new BorderLayout(0, 0));
		
		resetButton = new JButton("Reset");
		reset.add(resetButton);
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(){
					public void run(){
						reset();
					}
				}.start();
			}
		});
		//ButtonList.addMouseListener(resetButton); //ADDSDDDDDDDDDDDDDDDDDDDDDddddddddddd
		
		JLabel resetText = new JLabel("<- Raw values");
		reset.add(resetText, BorderLayout.SOUTH);
		
		loadKeys(data.getJSONObject("keys"));
		JSONArray channels = data.getJSONArray("channels");
		for(int i = 0; i < efctN; i++){
			JSONObject obj = channels.getJSONObject(i);
			effect.add(new EffectsEngine(effectsData.getJSONObject(i),
					App.utils.getChannel(obj.getString("speedChannelName")),
					App.utils.getChannel(obj.getString("amountChannelName"))));
		}
		reset();
		for(int i = 0; i < efctN; i++){
			Thread t = new Thread(effect.get(i));
			thread.add(t);
			t.start();
		}
		
		App.utils.mainWindow.addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent e) {}
			@Override
			public void componentMoved(ComponentEvent e) {}
			@Override
			public void componentHidden(ComponentEvent e) {}
			@Override
			public void componentResized(ComponentEvent e) {
				setMaximumSize(new Dimension(Integer.MAX_VALUE, mw.getVerticalSlotSize()));
				double slotSize = e.getComponent().getSize().getWidth() / 20;
				resizePanel(lightsGroup, slotSize, 3);
				resizePanel(effectType, slotSize, 3);
				resizePanel(phaseRange, slotSize, 3);
				resizePanel(direction, slotSize, 2);
				resizePanel(groups, slotSize, 2);
				resizePanel(blocks, slotSize, 2);
				resizePanel(wings, slotSize, 2);
				//resizePanel(reset, slotSize, 3);
			}
		});
	}
	
		
	private void resizePanel(JPanel panel, double slotSize, int slot){
		Dimension d = new Dimension((int) slotSize * slot, mw.getVerticalSlotSize());
		panel.setPreferredSize(d);
		panel.setMaximumSize(d);
		panel.setMinimumSize(d);
	}
	
	private void loadKeys(JSONObject keys) throws Exception{
		
		mw.addListener(new Action() {
			@Override
			public void actionPerformed() {
				effectBtn.doClick();
			}
		}, keys.getString("effectKey"));
		
		mw.addListener(new Action() {
			@Override
			public void actionPerformed() {
				lightsGroupBtn.doClick();
			}
		}, keys.getString("lightsGroupKey"));
		
		mw.addListener(new Action() {
			@Override
			public void actionPerformed() {
				phaseBtn.doClick();
			}
		}, keys.getString("phaseKey"));
		
		mw.addListener(new Action() {
			@Override
			public void actionPerformed() {
				directionBtn.doClick();
			}
		}, keys.getString("directionKey"));
		
		mw.addListener(new Action() {
			@Override
			public void actionPerformed() {
				groupsBtn.doClick();
			}
		}, keys.getString("groupsKey"));

		mw.addListener(new Action() {
			@Override
			public void actionPerformed() {
				blocksBtn.doClick();
			}
		}, keys.getString("blocksKey"));
		
		mw.addListener(new Action() {
			@Override
			public void actionPerformed() {
				wingsBtn.doClick();
			}
		}, keys.getString("wingsKey"));
		
		mw.addListener(new Action() {
			@Override
			public void actionPerformed() {
				resetButton.doClick();
			}
		}, keys.getString("resetKey"));
		
	}
	
	
	private void reset(){
		effectBtn.reset();
		lightsGroupBtn.reset();
		phaseBtn.reset();
		directionBtn.reset();
		groupsBtn.reset();
		blocksBtn.reset();
		wingsBtn.reset();
		for(int i = 0; i < efctN; i++)
			effect.get(i).reset();
	}
	
	private int numbersChange(ButtonList comboBox, JSpinner spinner, int index){
		allowChangeListener = false;
		Numbers n = (Numbers) comboBox.getSelectedItem();
		int i = 0;
		if(n != customNumbers){
			i = n.getNumber(index);
			spinner.setValue(i);
		}
		allowChangeListener = true;
		return i;
	}
	
	private void directionChange(){
		allowChangeListener = false;
		CharacterZ cz = (CharacterZ) directionBtn.getSelectedItem();
		for(int i = 0; i < efctN; i++)
			if(cz != customDirection){
				effect.get(i).setDirection(cz.getChar(i));
				directionTextField.setText(cz.getChar(i) + "");
			}
		allowChangeListener = true;
	}
	
	private void phaseChange(){
		allowChangeListener = false;
		PhaseCombo pc = (PhaseCombo) phaseBtn.getSelectedItem();
		for(int i = 0; i < efctN; i++)
			if(pc != customPhaseCombo){
				phaseStartSpinner.setValue(pc.getStart(i));
				effect.get(i).setPhaseStart(pc.getStart(i));
				phaseEndSpinner.setValue(pc.getEnd(i));
				effect.get(i).setPhaseEnd(pc.getEnd(i));
			}
		allowChangeListener = true;
	}
	
	private void effectChange(){
		allowChangeListener = false;
		EffectCombo ec = (EffectCombo) effectBtn.getSelectedItem();
		for(int i = 0; i < efctN; i++)
			effect.get(i).setEffect(ec.getEffect(i));
		allowChangeListener = true;
	}
	
	private void lightsGroupChange(){
		allowChangeListener = false;
		GroupCombo gc = (GroupCombo) lightsGroupBtn.getSelectedItem();
		//System.out.println(gc.getGroup().toString());
		for(int i = 0; i < efctN; i++)
			effect.get(i).setGroup(gc.getGroup(i), gc.getChannelNames(i));
		allowChangeListener = true;
	}
	
	private void loadEffectsList(ButtonList effectBtn, JSONArray effects, JSONArray jumps){
		for(int i = 0; i < effects.length(); i++){
			try{
				effectBtn.addItem(new EffectCombo(effects.getJSONArray(i), efctN));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(jumps != null)
			for(int i = 0; i < jumps.length(); i++){
				JSONObject jmp = jumps.getJSONObject(i);
				effectBtn.setJump(jmp.getInt("position"), jmp.getInt("loop"));
			}
	}
	
	private void loadGroupList(ButtonList groupsBtn, JSONArray groups, JSONArray jumps){
		for(int i = 0; i < groups.length(); i++){
			try{
				lightsGroupBtn.addItem(new GroupCombo(groups.getJSONArray(i), efctN));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(jumps != null)
			for(int i = 0; i < jumps.length(); i++){
				JSONObject jmp = jumps.getJSONObject(i);
				groupsBtn.setJump(jmp.getInt("position"), jmp.getInt("loop"));
			}
	}
	
	private void loadPhaseList(ButtonList phaseBtn, JSONArray phases, JSONArray jumps){
		for(int i = 0; i < phases.length(); i++){
			phaseBtn.addItem(new PhaseCombo(phases.getJSONArray(i), efctN));
		}
		phaseBtn.addItem(customPhaseCombo);
		phaseBtn.setJump(phaseBtn.length() - 2, 0);
		if(jumps != null)
			for(int i = 0; i < jumps.length(); i++){
				JSONObject jmp = jumps.getJSONObject(i);
				phaseBtn.setJump(jmp.getInt("position"), jmp.getInt("loop"));
			}
	}
	
	private void loadDirectionsList(ButtonList directionBtn, JSONArray directions, JSONArray jumps){
		for(int i = 0; i < directions.length(); i++)
			directionBtn.addItem(new CharacterZ(directions.getJSONArray(i), efctN));
		directionBtn.addItem(customDirection);
		directionBtn.setJump(directionBtn.length() - 2, 0);
		if(jumps != null)
			for(int i = 0; i < jumps.length(); i++){
				JSONObject jmp = jumps.getJSONObject(i);
				directionBtn.setJump(jmp.getInt("position"), jmp.getInt("loop"));
			}
	}
	
	private void loadNumbersList(ButtonList numbersBtn, JSONArray array, JSONArray jumps){
		for(int i = 0; i < array.length(); i++)
			numbersBtn.addItem(new Numbers(array.getJSONArray(i), efctN));
		numbersBtn.addItem(customNumbers);
		numbersBtn.setJump(numbersBtn.length() - 2, 0);
		if(jumps != null)
			for(int i = 0; i < jumps.length(); i++){
				JSONObject jmp = jumps.getJSONObject(i);
				numbersBtn.setJump(jmp.getInt("position"), jmp.getInt("loop"));
			}
	}
}
