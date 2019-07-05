package openLyghtPlugins.commandLine.commands;

import openLyghtPlugins.commandLine.Command;
import openLyghtPlugins.commandLine.Programmer;

public class Addsel extends Command {

	@Override
	protected String execCommand(String fullCommand) {
		Programmer.addSelection(super.args);
		return "Added " + super.args.length + " channel(s) to selection";
	}
}