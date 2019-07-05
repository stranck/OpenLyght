package openLyghtPlugins.commandLine.commands;

import openLyghtPlugins.commandLine.Command;
import openLyghtPlugins.commandLine.Programmer;

public class Change extends Command {

	private static final int FLAG = 0;
	private static final int VALUE = 1;
	
	@Override
	protected String execCommand(String fullCommand) {
		String s = "Can't update flag";
		boolean b = super.args[VALUE].equalsIgnoreCase("true");
		switch(super.args[FLAG]){
			case "prt" : {
				Programmer.updatePriority(b);
				s = "Updated priority";
				break;
			}
			case "abs" : {
				Programmer.updateAbsolute(b);
				s = "Updated absolute";
				break;
			}
		}
		return s + " for " + Programmer.active.size() + " selection(s)";
	}
}