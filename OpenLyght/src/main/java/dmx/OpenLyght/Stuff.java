package dmx.OpenLyght;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.UIManager;

import org.json.JSONArray;
import org.json.JSONObject;

import dmx.OpenLyght.GUI.MainWindow;
import dmx.OpenLyght.GUI.PathProjectSelector;
import dmx.OpenLyght.Utils.Effect;
import dmx.OpenLyght.Utils.Variable;

public class Stuff {
	
	private ArrayList<Channel> virtualChannel = new ArrayList<Channel>();
	public ArrayList<Variable> variables = new ArrayList<Variable>();
	public ArrayList<Plugin> plugins = new ArrayList<Plugin>();
	private ArrayList<Group> groups = new ArrayList<Group>();
	public Channel[] channels = new Channel[512];
	public MainWindow mainWindow;
	public String deafaultPath;
	public boolean freeze = false;
	
    public Stuff() throws Exception{
    	for(int i = 0; i < 512; i++){
    		channels[i] = new Channel();
    	}
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    	new PathProjectSelector();
    	
    	//System.out.println(new Integer(5).toString());
    	//testMasterFader();
    }
    
    public String read(String path) throws Exception{
    	String s = new String(Files.readAllBytes(Paths.get(path)));
    	for(Variable v : variables)
    		s = v.apply(s);
    	return s;
    }
    
    public void reloadVariables() throws Exception {
    	JSONArray data = new JSONArray(new String(Files.readAllBytes(Paths.get(deafaultPath + "variables.json"))));
    	System.out.println("Loading variables");
    	for(int i = 0; i < data.length(); i++)
    		variables.add(new Variable(data.getJSONObject(i)));
    }
    
    public void start() throws Exception{
    	reloadVariables();
    	reloadGroups();
    	reloadPlugins();
    	mainWindow = new MainWindow();
    	sendPluginMessage("openlyght started");
    }
    
    public void reloadPlugins() throws Exception {
    	File[] dir = new File("plugins" + File.separator).listFiles(File::isFile);
    	for(File f : dir)
    		plugins.add(getPlugin(f));
    }
    
    private Plugin getPlugin(File f) throws Exception {
    	URL[] urls = { new URL("jar:file:" + f.toString() +"!/") };
    	ClassLoader cl = URLClassLoader.newInstance(urls, getClass().getClassLoader());
    	
    	BufferedReader input = new BufferedReader(new InputStreamReader(cl.getResourceAsStream("/plugin.json")));
    	String s = "", line;
    	while((line = input.readLine()) != null) s += line;
    	
    	Class<?> clazz = Class.forName(new JSONObject(s).getString("pluginClass"), true, cl);
    	Constructor<?> ctor = clazz.getConstructor(Stuff.class);
    	return (Plugin) ctor.newInstance(this);
    }
    
    public void reloadGroups(){
    	groups.clear();
    	File[] dir = new File(deafaultPath + "groups" + File.separator).listFiles(File::isFile);
    	for(File f : dir){
    		try{
        		groups.add(new Group(new JSONObject(read(f.getAbsolutePath()))));
    		} catch (Exception e){
    			e.printStackTrace();
    		}
    	}
    }
    
    public void reloadVirtualChannels(){
    	for(Channel c : virtualChannel)
    		c.reloadValue();
    }
    
    public Channel getChannel(String name){
    	Channel c = null;
    	int channelID = App.getInt(name);
    	if(channelID < 0) {
        	for(Channel ch : virtualChannel)
        		if(ch.getDescription().equals(name)) {
        			c = ch;
        			break;
        		}
        	if(c == null) {
        		System.out.println("Generating new virtual channel: " + name);
        		c = new Channel();
        		c.setDescription(name);
        		virtualChannel.add(c);
        	}
    	} else c = channels[channelID];
    	return c;
    }
   
    public void addGroup(Group g){
    	groups.add(g);
    }
    
    public void makeActiveInSuperGroup(Group g){
    	for(Group group : groups)
    		if(group.getName().equals(g.getName()))
    			group.setStatus(group == g);
    }
    
    public void changeSuperGroupStatus(String name, boolean status){
    	for(Group group : groups)
    		if(group.getName().equals(name))
    			group.setStatus(status);
    }
    
    public Group getGroup(String name){
    	Group group = new Group(new ArrayList<Channel>(), name);
    	for(Group g : groups)
    		if(g.getName().equals(name) && g.getStatus())
    			group.merge(g);
    	return group;
    }
    
    public boolean isSuperGroupActive(String name){
    	boolean status = true;
    	for(int i = 0; i < groups.size() && status; i++)
    		if(groups.get(i).getName().equals(name))
    			status = status && groups.get(i).getStatus();
    	return status;
    }
    
    public ArrayList<Group> getSuperGroupObjects(String name){
    	ArrayList<Group> gr = new ArrayList<Group>();
    	for(Group g : groups)
    		if(g.getName().equals(name)) gr.add(g);
    	return gr;
    }
    
	public Effect getEffectByName(String name) {
		Effect e = null;
		for(Plugin p : plugins)
			if(App.checkForPluginType(p, "effect") && p.getName().equals(name)) {
				e = (Effect) p;
				break;
			}

		if(e == null) try {
			e = (Effect) Class.forName("dmx.OpenLyght.Utils.Effects." + name).newInstance();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return e;
	}
	
	public void sendPluginMessage(String message){
    	System.out.println("Broadcast message: " + message);
		for(Plugin p : plugins)
			p.message(message);
	}
}
