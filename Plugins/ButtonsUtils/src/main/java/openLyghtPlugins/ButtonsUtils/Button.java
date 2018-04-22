package openLyghtPlugins.ButtonsUtils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import dmx.OpenLyght.Utils.Scene;

public class Button {
	private boolean status = true, flash = true, fix = false;
	private ArrayList<Command> onPress = new ArrayList<Command>();
	private ArrayList<Command> onRelease = new ArrayList<Command>();
	private Scene scene;
	
	public void load(JSONObject button){
		scene = Main.getScene(button.getString("scene"));
		
		if(button.has("onPress")) {
			JSONArray actions = button.getJSONArray("onPress");
			for(int i = 0; i < actions.length(); i++)
				onPress.add(new Command(actions.getString(i), scene));
		}
		if(button.has("onRelease")) {
			JSONArray actions = button.getJSONArray("onRelease");
			for(int i = 0; i < actions.length(); i++)
				onRelease.add(new Command(actions.getString(i), scene));
		}
		if(button.has("flash")) {
			flash = button.getBoolean("flash");
			System.out.println("FLASHSTATUS: " + button.getBoolean("flash") + " " + flash + " " + hashCode());
		}
	}
	
	public void setCommand(Command cmd, boolean press, int index){
		System.out.println(onPress.get(index));
		if(press) onPress.set(index, cmd);
			else  onRelease.set(index, cmd);
		System.out.println(onPress.get(index));
	}
	
	public Scene getScene(){
		return scene;
	}
	
	public void setFix(boolean b){
		//System.out.println("Fixing: " + b + " "+ hashCode());
		fix = b;
	}
	
	public void update(boolean b){
		//System.out.println(b + " " + status + " " + flash + " " + fix + " " + hashCode());
		if(!flash) if(b){
			b = !status;
		} else b = status;
		if(b != status){
			if(b) {
				for(Command c : onPress) c.execute(fix);
			} else for(Command c : onRelease) c.execute(fix);
			status = b;
		}
	}
}
