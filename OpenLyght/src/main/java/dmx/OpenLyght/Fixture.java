package dmx.OpenLyght;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import dmx.OpenLyght.Utils.Master;

public class Fixture {
	public static final String DEFAULT_NAME = "channel wrapper;";
	public static final String DIMMER_CH_NAME = "general dimmer";
	@SuppressWarnings("unused")
	private static final int METHOD_NAME = 0;
	private static final int FP_NAME = 1;
	private static final int CHANNEL_NAME = 2;
	
	private ArrayList<Channel> channels = new ArrayList<Channel>();
	@SuppressWarnings("unused")
	private Master virtualDim;
	private Patch fixture;
	private String name;
	
	public Fixture(Channel c){
		channels.add(c);
		c.setOriginalFixture(this);
		name = DEFAULT_NAME + c.getDescription();
	}
	
	public Fixture(JSONObject data){
		name = data.getString("name");

		int i = data.getInt("patch");
		fixture = App.utils.getFixtureType(data.getString("fixtureType"));
		ArrayList<String> chs = fixture.getChannels();
		for(String s : chs){
			if(s != null){
				Channel c = App.utils.getChannel("" + i);
				c.setOriginalFixture(this);
				c.setDescription(fixture + ":" + name + ":" + s);
				channels.add(c);
			}
			i++;
		}
		String colorMode = fixture.getAttributeByName("Attributes Color mode");
		if(colorMode != null && colorMode.equals("rgb") && getChannelsByName(DIMMER_CH_NAME).size() == 0){
			System.out.println("Generating virtual dimmer channel and master");
			Channel c = App.utils.getChannel(fixture + ":" + name + ":" + DIMMER_CH_NAME);
			c.setOriginalFixture(this);
			channels.add(c);
			
			virtualDim = new Master(c, getChannelsByName("colors rgb*"), 1, -1, 10);
		}
	}
	
	public String getName(){
		return name;
	}
	public ArrayList<Channel> getChannelsByName(String name){
		ArrayList<Channel> chs = new ArrayList<Channel>();
		name = fixture + ":" + this.name + ":" + name;
		for(Channel c : channels){
			if(App.likeIgnoreCase(c.getDescription(), name))
				chs.add(c);
		}
		return chs;
	}
	public boolean has(Channel c){
		return channels.contains(c);
	}
	public String getAttributeByKey(String key){
		return fixture.getAttributeByName(key);
	}
	public String getPatchName(){
		return fixture.getName();
	}
	public Patch getPatch(){
		return fixture;
	}
	
	public static ArrayList<Channel> getChannelsByFullNames(JSONArray names, ArrayList<Fixture> fixtures){
		ArrayList<Channel> ch = new ArrayList<Channel>();
		for(int i = 0; i < names.length(); i++)
			ch.addAll(getChannelsByFullName(names.getString(i), fixtures));
		return App.removeDuplicates(ch);
	}
	public static ArrayList<Channel> getChannelsByFullName(String name, ArrayList<Fixture> fixtures){
		System.out.println("Getting channel: " + name);
		ArrayList<Channel> ch = new ArrayList<Channel>();
		char method = 0x00;
		String sp[] = null;
		if(name.charAt(0) == '$'){
			method = name.charAt(1);
			sp = name.split("\\.");
		}
		ArrayList<Fixture> fxs = new ArrayList<Fixture>(fixtures);
		for(Fixture f : fxs){
			switch(method){
				case 'p': { //$p.FIXTURE TYPE NAME.CHANNEL NAME
					if(App.likeIgnoreCase(f.getPatchName(), sp[FP_NAME]))
						ch.addAll(f.getChannelsByName(sp[CHANNEL_NAME]));
					break;
				}
				case 'f': { //f.FIXTURE NAME.CHANNEL NAME
					if(App.likeIgnoreCase(f.getName(), sp[FP_NAME]))
						ch.addAll(f.getChannelsByName(sp[CHANNEL_NAME]));
					break;
				}
				default: {
					ArrayList<Channel> c = f.getChannelsByName(name);
					//System.out.println("Adding channel" + c);
					ch.addAll(c);
				}
			}
		}
		if(ch.size() == 0 && method == 0x00)
			ch.add(App.utils.getChannel(name));
		//System.out.println(fixtures.size() + ") OBTAINING " + ch.size() + " CHANNELS " + name);
		return ch;
	}
	
	@Override
	public String toString(){
		return "Fx: " + name + " @ " + hashCode();
	}
	//public static boolean asd = false;
	@Override
	public boolean equals(Object f){
		//if(asd) System.out.println("eq: '" + ((Fixture)f).name + "'\t'" + name + "'\t" + (name.equals(((Fixture)f).name)));
		return f instanceof Fixture ? name.equalsIgnoreCase(((Fixture)f).name) : false;
	}
}
