package openLyghtPlugins.DMXUtils;

import java.io.File;
import java.util.ArrayList;

import dmx.OpenLyght.Utils.Scene;

public class Button {
	
	public static ArrayList<Scene> scenes = new ArrayList<Scene>();
	private static int globalSelctedScene;
	
	public Button() throws Exception {
		File[] dir = new File(Main.defaultPath + "scenes" + File.separator).listFiles(File::isFile);
		for(File f : dir){
			System.out.println("Loading scene " + f.getAbsolutePath());
			scenes.add(new Scene(f.getAbsolutePath()));
			PagePanel.addButton(f.getName());
		}
		/*currentScenes = new int[scenes.size()];
		for(int i = 0; i < currentScenes.length; i++)
			currentScenes[i] = 0;*/
	}
	
	public void buttonPressed(int value){
		int scene = (value - 31) / 28;
		//System.out.println("BUTTON PRESSED " + scene + " " + value + " " + Main.buttonPage + " " + currentScenes[Main.buttonPage] + " " + globalSelctedScene);
		if(value < 31)
			scene = globalSelctedScene;
		else
			globalSelctedScene = scene;
		//if(scene != currentScenes[Main.buttonPage]) {
		//	currentScenes[Main.buttonPage] = scene;
		scenes.get(Main.buttonPage).gotoStep(scene); 
		//}
	}
}
