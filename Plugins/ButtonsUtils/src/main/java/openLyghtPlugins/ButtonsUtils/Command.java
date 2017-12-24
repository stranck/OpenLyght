package openLyghtPlugins.ButtonsUtils;

import dmx.OpenLyght.Utils.Scene;

public class Command {

	//private String originalCommand;
	private Scene scene;
	private Button[] buttons;
	private int command; //0 = goto; 1 = setStatus; 2 = button; 3 = fix
	private int step;
	private boolean status;
	
	public Command(String command, Scene scene){
		this.scene = scene;
		//originalCommand = command;
		String sp[] = command.split("\\s+");
		switch(sp[0]){
			case "goto" : {
				this.command = 0;
				step = Integer.parseInt(sp[1]);
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
				for(int i = 2; i < sp.length; i++){
					buttons[i - 2] = Main.buttons[Integer.parseInt(sp[i])];
				}
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
			default : throw new IllegalArgumentException(sp[0] + ": invalid button command");
		}
	}
	
	public void execute(boolean fix){
		try{
			//System.out.println("Executing: " + originalCommand);
			switch(command){
				case 0:{
					if(!fix) scene.gotoStep(step);
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
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
