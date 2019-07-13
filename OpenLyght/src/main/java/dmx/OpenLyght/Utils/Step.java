package dmx.OpenLyght.Utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import dmx.OpenLyght.*;

public class Step {
	private BasicChannel[] bc;
	private ArrayList<String> channels = new ArrayList<String>();
	
	public Step(JSONArray data, Group g){
		ArrayList<BasicChannel> bcs = new ArrayList<BasicChannel>();
		
		for(int i = 0; i < data.length(); i++){
			JSONObject j = data.getJSONObject(i);
			Object value = j.get("value");
			boolean smooth = j.getBoolean("smooth");
			
			JSONArray channelNames = j.getJSONArray("channels");
			for(int x = 0; x < channelNames.length(); x++)
				channels.add(channelNames.getString(x));
			
			ArrayList<Channel> chs = g.getChannelsByNames(channelNames);
			System.out.println("Adding basic channels to: " + hashCode());
			for(Channel c : chs){
				int v = 0;
				if(value instanceof Integer){
					v = (int) value;
				} else if(value instanceof String){
					String val = (String) value;
					if(val.charAt(0) == '$'){
						String sp[] = val.split("\\.");
						char method = val.charAt(1);
						switch(method){
							case 'a':{
								System.out.println("attr: " + sp[1] + " fName " + c.getOriginalFixture().getName());
								v = Integer.parseInt(c.getOriginalFixture().getAttributeByKey(sp[1]));
								break;
							}
						}
					} else v = Integer.parseInt(val);
				}
				bcs.add(new BasicChannel(v, smooth, c));
			}
		}
		bc = new BasicChannel[bcs.size()];
		bcs.toArray(bc);
	}
	
	public Step(ArrayList<Channel> c){
		bc = new BasicChannel[c.size()];
		System.out.println("Adding basic channels to: " + hashCode());
		for(int i = 0; i < bc.length; i++)
			bc[i] = new BasicChannel(0, false, c.get(i));
	}
	
	public boolean contains(Channel ch){
		for(BasicChannel bc : this.bc)
			if(bc.isThisChannel(ch))
				return true;
		return false;
	}
	
	public ArrayList<String> getChannels(){
		return channels;
	}
	public BasicChannel getBasicChannel(int i){
		return bc[i];
	}
	
	public BasicChannel getBasicChannel(Channel ch){
		return App.getBasicChannel(bc, ch);
	}
}
