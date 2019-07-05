package openLyghtPlugins.commandLine.commands;

import openLyghtPlugins.commandLine.Command;

public class Exit extends Command {

	@Override
	protected String execCommand(String fullCommand) {
		System.exit(0);
		return "Exit";
	}

}