package openLyghtPlugins.ButtonsUtils;

import java.util.Arrays;

import dmx.OpenLyght.Utils.Scene;
import dmx.OpenLyght.Utils.Wait;

public class Command {

	//private String originalCommand;
	private Sequence sequence;
	private int originalStep;
	private Command cmd;
	private Scene scene;
	private Button[] buttons = null;
	private int command; //0 = goto; 1 = setStatus; 2 = button; 3 = fix; 4 = setCommand; 5 = gotoCue; 6 = wait;
	private int step;
	private boolean status;
	
	public Command(String command, Scene scene, Sequence sequence, int step){
		this.scene = scene;
		this.sequence = sequence;
		originalStep = step;
		//originalCommand = command;
		analyze(command.split("\\s+"));
	}
	public Command(String command, Scene scene){
		this.scene = scene;
		//originalCommand = command;
		analyze(command.split("\\s+"));
	}
	public Command(String[] sp, Scene scene, Sequence sequence, int step){
		//for(String s : sp) originalCommand += s + " ";
		
		this.sequence = sequence;
		originalStep = step;
		this.scene = scene;
		analyze(sp);
	}
	
	private void analyze(String[] sp){
		//System.out.println(Arrays.toString(Main.buttons));
		switch(sp[0]){
			case "goto" : {
				this.command = 0;
				step = Integer.parseInt(sp[1]);
				//System.out.println(Arrays.toString(sp));
				if(sp.length > 2)
					buttons = new Button[]{Main.buttons[Integer.parseInt(sp[2])]};
				break;
			}
			case "setStatus" : {
				this.command = 1;
				status = sp[1].equalsIgnoreCase("true");
				break;
			}
			case "button" : {
				this.command = 2;
				buttons = new Button[sp.length - 2];
				status = sp[1].equalsIgnoreCase("true");
				System.out.println(Arrays.toString(Main.buttons));
				for(int i = 2; i < sp.length; i++){
					buttons[i - 2] = Main.buttons[Integer.parseInt(sp[i])];
				}
				System.out.println(Arrays.toString(buttons));
				break;
			}
			case "fix" : {
				this.command = 3;
				buttons = new Button[sp.length - 2];
				status = sp[1].equalsIgnoreCase("true");
				for(int i = 2; i < sp.length; i++){
					buttons[i - 2] = Main.buttons[Integer.parseInt(sp[i])];
				}
				break;
			}
			case "setCommand" : {
				this.command = 4;
				buttons = new Button[]{Main.buttons[Integer.parseInt(sp[1])]};
				status = sp[2].equalsIgnoreCase("true");
				step = Integer.parseInt(sp[3]);
				String[] newCommand = new String[sp.length - 4];
				for(int i = 0; i < newCommand.length; i++)
					newCommand[i] = sp[i + 4];
				cmd = new Command(newCommand, buttons[0].getScene(), sequence, originalStep);
				break;
			}
			case "gotoCue" : {
				this.command = 5;
				if(!sequence.getName().equals(sp[1])){
					
					Sequence sq = null;
					for(Sequence s : Main.sequences)
						if(s.getName().equals(sp[1])){
							sq = s;
							break;
						}
					if(sq == null)
						throw new IllegalArgumentException(sp[1] + ": invalid sequence name");
					else 
						sequence = sq;
				}
				step = Integer.parseInt(sp[2]);
				break;
			}
			case "wait" : {
				this.command = 6;
				step = Integer.parseInt(sp[1]);
				
				String[] newCommand = new String[sp.length - 2];
				for(int i = 0; i < newCommand.length; i++)
					newCommand[i] = sp[i + 2];
				cmd = new Command(newCommand, scene, sequence, originalStep);
				
				break;
			}
			default : throw new IllegalArgumentException(sp[0] + ": invalid button command");
		}
	}
	
	public void execute(boolean fix){
		try{
			//System.out.println("Executing: " + originalCommand);
			switch(command){
				case 0:{
					//System.out.println("GOTO: " + step + " " + fix + " " + scene);
					if(!fix) {
						if(buttons == null) scene.gotoStep(step);
						else buttons[0].getScene().gotoStep(step);
					}
					break;
				}
				case 1:{
					scene.setStatus(status);
					break;
				}
				case 2:{
					for(int i = 0; i < buttons.length; i++)
						buttons[i].update(status);
					break;
				}
				case 3:{
					for(int i = 0; i < buttons.length; i++)
						buttons[i].setFix(status);
					break; 
				}
				case 4:{
					buttons[0].setCommand(cmd, status, step);
					break;
				}
				case 5:{
					sequence.getButton().setIndex(step);
					break;
				}
				case 6:{
					new Thread(){
						@Override
						public void run(){
							Wait.wait(step);
							if(sequence != null && sequence.getButton().getIndex() == originalStep)
								cmd.execute(false);
						}
					}.start();
					break;
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
