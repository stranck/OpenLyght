package openLyghtPlugins.commandLine;

import java.util.Scanner;

import dmx.OpenLyght.App;
import dmx.OpenLyght.Plugin;
import dmx.OpenLyght.Stuff;

public class Main implements Plugin, Runnable {

	private final String[] tags = { "cmd", "interpreter", "commands" };
	private final String name = "CMDinterpreter";
	public static Stuff openLyght;
	
	public Main(Stuff ol){
		openLyght = ol;
	}
	
	@Override
	public String getName() {
		return name;
	}
	@Override
	public String[] getTags() {
		return tags;
	}

	@Override
	public void message(String message) {
		if(message.equalsIgnoreCase("openlyght started")){
			System.out.println("Starting command line engine");
			//App.wait(1500);
			new Thread(this).start();
		}
	}

	@Override
	public void run() {
		String s;
		Command cmd;
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		App.wait(250);
		while(true){
			try{
				System.out.print(">");
				s = sc.nextLine();
				if(!s.equals("")){
					cmd = Command.getCommand(s);
					if(cmd != null){
						System.out.println(cmd.execCommand(s));
					} else System.out.println(s + ": Command not found");
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
}
