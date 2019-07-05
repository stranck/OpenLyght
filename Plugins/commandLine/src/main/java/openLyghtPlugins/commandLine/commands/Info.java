package openLyghtPlugins.commandLine.commands;

import java.util.ArrayList;

import openLyghtPlugins.commandLine.Command;
import openLyghtPlugins.commandLine.Programmer;
import openLyghtPlugins.commandLine.Selection;

public class Info extends Command {

	@Override
	protected String execCommand(String fullCommand) {
		String s = "Selection list:\n";
		ArrayList<Selection> sel = new ArrayList<Selection>();
		if(super.args.length > 0){
			for(String name : super.args){
				Selection selection = Programmer.getSelection(name, false);
				if(selection != null)
					sel.add(selection);
			}
		} else sel.addAll(Programmer.active);
		for(Selection selection : sel)
			s += "\n" + selection.toString();
		return s;
	}

}
