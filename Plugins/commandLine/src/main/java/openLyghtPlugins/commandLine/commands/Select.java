package openLyghtPlugins.commandLine.commands;

import openLyghtPlugins.commandLine.Command;
import openLyghtPlugins.commandLine.Programmer;

public class Select extends Command {

	@Override
	protected String execCommand(String fullCommand) {
		Programmer.clearSelection();
		Programmer.addSelection(super.args);
		return "Selected " + super.args.length + " channel(s)";
	}
}
