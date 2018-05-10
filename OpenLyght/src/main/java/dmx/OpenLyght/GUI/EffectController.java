package dmx.OpenLyght.GUI;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JComboBox;
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
import dmx.OpenLyght.Utils.EffectsEngine;

@SuppressWarnings("serial")
public class EffectController extends JPanel {
	private JComboBox<EffectCombo> effectComboBox;
	private JComboBox<GroupCombo> lightsGroupComboBox;
	private JComboBox<PhaseCombo> phaseComboBox;
	private JComboBox<CharacterZ> directionComboBox;
	private JComboBox<Numbers> groupsComboBox;
	private JComboBox<Numbers> blocksComboBox;
	private JComboBox<Numbers> wingsComboBox;
	private JSpinner phaseStartSpinner;
	private JSpinner phaseEndSpinner;
	private JSpinner groupsSpinner;
	private JSpinner blocksSpinner;
	private JSpinner wingsSpinner;
	private JTextField directionTextField;
	private JButton resetButton;
	
	private MainWindow mw;
	private EffectsEngine effect;
	private Thread thread;
	private boolean allowChangeListener = true;
	
	private PhaseCombo customPhaseCombo = new PhaseCombo("Custom");
	private CharacterZ customDirection = new CharacterZ("Custom");
	private Numbers customNumbers = new Numbers("Custom");
	
	private int effectIndex = 0;
	private int lightsGroupIndex = 0;
	private int phaseIndex = 0;
	private int directionIndex = 0;
	private int groupsIndex = 0;
	private int blocksIndex = 0;
	private int wingsIndex = 0;

	public EffectController(MainWindow mw, JSONObject data) throws Exception {
		this.mw = mw;
		
		setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JPanel lightsGroup = new JPanel();
		add(lightsGroup);
		lightsGroup.setLayout(new BorderLayout(0, 0));
		
		JLabel lightsGroupText = new JLabel("Light group:");
		lightsGroup.add(lightsGroupText, BorderLayout.NORTH);
		
		lightsGroupComboBox = new JComboBox<GroupCombo>();
		lightsGroup.add(lightsGroupComboBox, BorderLayout.CENTER);
		loadGroupList(lightsGroupComboBox, data.getJSONArray("lightsGroups"));
		lightsGroupComboBox.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				lightsGroupChange();
			}
		});
		
		JPanel effectType = new JPanel();
		add(effectType);
		effectType.setLayout(new BorderLayout(0, 0));
		
		JLabel effectTypeText = new JLabel("Effect:");
		effectType.add(effectTypeText, BorderLayout.NORTH);
		
		effectComboBox = new JComboBox<EffectCombo>();
		effectType.add(effectComboBox, BorderLayout.CENTER);
		loadEffectsList(effectComboBox, data.getJSONArray("effects"));
		effectComboBox.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent arg0) {
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
					phaseComboBox.setSelectedItem(customPhaseCombo);
					effect.setPhaseStart((int) phaseStartSpinner.getValue());
				}
			}
		});
		
		phaseEndSpinner = new JSpinner();
		phaseSubPanel.add(phaseEndSpinner);
		phaseEndSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(allowChangeListener){
					phaseComboBox.setSelectedItem(customPhaseCombo);
					effect.setPhaseEnd((int) phaseEndSpinner.getValue());
				}
			}
		});
		
		JLabel phaseText = new JLabel("Phase:");
		phaseRange.add(phaseText, BorderLayout.NORTH);
		
		phaseComboBox = new JComboBox<PhaseCombo>();
		phaseRange.add(phaseComboBox, BorderLayout.CENTER);
		loadPhaseList(phaseComboBox, data.getJSONArray("phases"));
		phaseComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				phaseChange();
			}
		});
		
		JPanel direction = new JPanel();
		add(direction);
		direction.setLayout(new BorderLayout(0, 0));
		
		JLabel directionText = new JLabel("Direction:");
		direction.add(directionText, BorderLayout.NORTH);
		
		directionComboBox = new JComboBox<CharacterZ>();
		direction.add(directionComboBox, BorderLayout.CENTER);
		loadDirectionsList(directionComboBox, data.getJSONArray("directions"));
		directionComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
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
						directionComboBox.setSelectedItem(customDirection);
						effect.setDirection(c);
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
					groupsComboBox.setSelectedItem(customNumbers);
					effect.setGroups((int) groupsSpinner.getValue());
				}
			}
		});
		
		groupsComboBox = new JComboBox<Numbers>();
		groups.add(groupsComboBox, BorderLayout.CENTER);
		loadNumbersList(groupsComboBox, data.getJSONArray("groups"));
		groupsComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				groupsIndex = groupsComboBox.getSelectedIndex();
				effect.setGroups(numbersChange(groupsComboBox, groupsSpinner));
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
					blocksComboBox.setSelectedItem(customNumbers);
					effect.setBlocks((int) blocksSpinner.getValue());
				}
			}
		});
		
		blocksComboBox = new JComboBox<Numbers>();
		blocks.add(blocksComboBox, BorderLayout.CENTER);
		loadNumbersList(blocksComboBox, data.getJSONArray("blocks"));
		blocksComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				blocksIndex = blocksComboBox.getSelectedIndex();
				effect.setBlocks(numbersChange(blocksComboBox, blocksSpinner));
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
					wingsComboBox.setSelectedItem(customNumbers);
					effect.setWings((int) wingsSpinner.getValue());
				}
			}
		});
		
		wingsComboBox = new JComboBox<Numbers>();
		wings.add(wingsComboBox, BorderLayout.CENTER);
		loadNumbersList(wingsComboBox, data.getJSONArray("wings"));
		wingsComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				wingsIndex = wingsComboBox.getSelectedIndex();
				effect.setWings(numbersChange(wingsComboBox, wingsSpinner));
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
				reset();
			}
		});
		
		JLabel resetText = new JLabel("<- Raw values");
		reset.add(resetText, BorderLayout.SOUTH);
		
		loadKeys(data.getJSONObject("keys"));
		effect = new EffectsEngine(
				data.getJSONObject("effectData"),
				App.utils.getChannel(data.getString("speedChannelName")),
				App.utils.getChannel(data.getString("amountChannelName")));
		reset();
		thread = new Thread(effect);
		thread.start();
	}
	
	
	private void loadKeys(JSONObject keys) throws Exception{
		
		mw.addListener(new Action() {
			@Override
			public void actionPerformed() {
				if(++effectIndex > effectComboBox.getItemCount() - 1) effectIndex = 0;
				effectComboBox.setSelectedIndex(effectIndex);
			}
		}, keys.getString("effectKey"));
		
		mw.addListener(new Action() {
			@Override
			public void actionPerformed() {
				if(++lightsGroupIndex > lightsGroupComboBox.getItemCount() - 1) lightsGroupIndex = 0;
				lightsGroupComboBox.setSelectedIndex(lightsGroupIndex);
			}
		}, keys.getString("lightsGroupKey"));
		
		mw.addListener(new Action() {
			@Override
			public void actionPerformed() {
				if(++phaseIndex > phaseComboBox.getItemCount() - 2) phaseIndex = 0;
				phaseComboBox.setSelectedIndex(phaseIndex);
			}
		}, keys.getString("phaseKey"));
		
		mw.addListener(new Action() {
			@Override
			public void actionPerformed() {
				if(++directionIndex > directionComboBox.getItemCount() - 2) directionIndex = 0;
				directionComboBox.setSelectedIndex(directionIndex);
			}
		}, keys.getString("directionKey"));
		
		mw.addListener(new Action() {
			@Override
			public void actionPerformed() {
				if(++groupsIndex > groupsComboBox.getItemCount() - 2) groupsIndex = 0;
				groupsComboBox.setSelectedIndex(groupsIndex);
			}
		}, keys.getString("groupsKey"));

		mw.addListener(new Action() {
			@Override
			public void actionPerformed() {
				if(++blocksIndex > blocksComboBox.getItemCount() - 2) blocksIndex = 0;
				blocksComboBox.setSelectedIndex(blocksIndex);
			}
		}, keys.getString("blocksKey"));
		
		mw.addListener(new Action() {
			@Override
			public void actionPerformed() {
				if(++wingsIndex > wingsComboBox.getItemCount() - 2) wingsIndex = 0;
				wingsComboBox.setSelectedIndex(wingsIndex);
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
		effectIndex = 0;
		effectComboBox.setSelectedIndex(0);
		lightsGroupIndex = 0;
		lightsGroupComboBox.setSelectedIndex(0);
		phaseIndex = 0;
		phaseComboBox.setSelectedIndex(0);
		directionIndex = 0;
		directionComboBox.setSelectedIndex(0);
		groupsIndex = 0;
		groupsComboBox.setSelectedIndex(0);
		blocksIndex = 0;
		blocksComboBox.setSelectedIndex(0);
		wingsIndex = 0;
		wingsComboBox.setSelectedIndex(0);	
		effect.reset();
	}
	
	private int numbersChange(JComboBox<Numbers> comboBox, JSpinner spinner){
		allowChangeListener = false;
		Numbers n = (Numbers) comboBox.getSelectedItem();
		int i = 0;
		if(n != customNumbers){
			i = n.getNumber();
			spinner.setValue(i);
		}
		allowChangeListener = true;
		return i;
	}
	
	private void directionChange(){
		allowChangeListener = false;
		directionIndex = directionComboBox.getSelectedIndex();
		CharacterZ cz = (CharacterZ) directionComboBox.getSelectedItem();
		if(cz != customDirection){
			effect.setDirection(cz.getChar());
			directionTextField.setText(cz.getChar() + "");
		}
		allowChangeListener = true;
	}
	
	private void phaseChange(){
		allowChangeListener = false;
		phaseIndex = phaseComboBox.getSelectedIndex();
		PhaseCombo pc = (PhaseCombo) phaseComboBox.getSelectedItem();
		if(pc != customPhaseCombo){
			phaseStartSpinner.setValue(pc.getStart());
			effect.setPhaseStart(pc.getStart());
			phaseEndSpinner.setValue(pc.getEnd());
			effect.setPhaseEnd(pc.getEnd());
		}
		allowChangeListener = true;
	}
	
	private void effectChange(){
		allowChangeListener = false;
		effectIndex = effectComboBox.getSelectedIndex();
		EffectCombo ec = (EffectCombo) effectComboBox.getSelectedItem();
		effect.setEffect(ec.getEffect());
		allowChangeListener = true;
	}
	
	private void lightsGroupChange(){
		allowChangeListener = false;
		lightsGroupIndex = lightsGroupComboBox.getSelectedIndex();
		GroupCombo gc = (GroupCombo) lightsGroupComboBox.getSelectedItem();
		System.out.println(gc.getGroup().toString());
		effect.setGroup(gc.getGroup());
		allowChangeListener = true;
	}
	
	private void loadEffectsList(JComboBox<EffectCombo> effectComboBox, JSONArray effects){
		for(int i = 0; i < effects.length(); i++){
			try{
				JSONObject effect = effects.getJSONObject(i);
				effectComboBox.addItem(new EffectCombo(
						App.utils.getEffectByName(effect.getString("effectName")),
						effect.getString("name")
						));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void loadGroupList(JComboBox<GroupCombo> groupsComboBox, JSONArray groups){
		for(int i = 0; i < groups.length(); i++){
			try{
				JSONObject group = groups.getJSONObject(i);
				lightsGroupComboBox.addItem(new GroupCombo(
						App.utils.getGroup(group.getString("groupName")),
						group.getString("name")
						));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void loadPhaseList(JComboBox<PhaseCombo> phaseComboBox, JSONArray phases){
		for(int i = 0; i < phases.length(); i++){
			JSONObject phase = phases.getJSONObject(i);
			phaseComboBox.addItem(new PhaseCombo(phase.getInt("start"), phase.getInt("end")));
		}
		phaseComboBox.addItem(customPhaseCombo);
	}
	
	private void loadDirectionsList(JComboBox<CharacterZ> directionComboBox, JSONArray directions){
		for(int i = 0; i < directions.length(); i++)
			directionComboBox.addItem(new CharacterZ(directions.getString(i).charAt(0)));
		directionComboBox.addItem(customDirection);
	}
	
	private void loadNumbersList(JComboBox<Numbers> numbersComboBox, JSONArray array){
		for(int i = 0; i < array.length(); i++)
			numbersComboBox.addItem(new Numbers(array.getInt(i)));
		numbersComboBox.addItem(customNumbers);
	}
}
