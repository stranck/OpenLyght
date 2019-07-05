package openLyghtPlugins.commandLine.commands;

import openLyghtPlugins.commandLine.Command;
import openLyghtPlugins.commandLine.Programmer;

public class Edit extends Command {

	@Override
	protected String execCommand(String fullCommand) {
		Programmer.updateValue(super.args[0]);
		return "Value updated for " + Programmer.active.size() + " channel(s)";
	}
}