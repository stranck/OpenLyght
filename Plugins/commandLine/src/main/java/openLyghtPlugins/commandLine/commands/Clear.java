package openLyghtPlugins.commandLine.commands;

import openLyghtPlugins.commandLine.Command;
import openLyghtPlugins.commandLine.Programmer;
import openLyghtPlugins.commandLine.Selection;

public class Clear extends Command {

	@Override
	protected String execCommand(String fullCommand) {
		String s = "Can't clear";
		switch(args[0]){
			case "--all" : {
				clearAll();
				s = "Cleared all";
				break;
			}
			case "-a" : {
				clearAll();
				s = "Cleared all";
				break;
			}
			case "--selection" : {
				clearSelection();
				s = "Selection cleared";
				break;
			}
			case "--sel" : {
				clearSelection();
				s = "Selection cleared";
				break;
			}
			case "-s" : {
				clearSelection();
				s = "Selection cleared";
				break;
			}
			default : {
				Selection sel = Programmer.getSelection(args[0], false);
				if(sel != null){
					sel.deselect();
					s = "Deselected";
				}
			}
		}
		return s;
	}
	
	public void clearAll(){
		Programmer.clearAll();
	}
	public void clearSelection(){
		Programmer.clearSelection();
	}
}
