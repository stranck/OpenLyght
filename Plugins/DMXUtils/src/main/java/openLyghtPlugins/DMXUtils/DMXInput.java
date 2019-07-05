package openLyghtPlugins.DMXUtils;

import java.util.ArrayList;

import dmx.OpenLyght.Utils.Arduino;
import gnu.io.SerialPortEvent;

public class DMXInput extends Arduino {

	public DMXInput(String portName) {
		super(portName, 76800);
	}

	@Override
	public synchronized void serialEvent(SerialPortEvent spEvent) {
		try{
			if (spEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
				String s = null;
				while((s = super.input.readLine()) != null){
					char[] in = s.toCharArray();
					ArrayList<Data> data = new ArrayList<Data>();
					for(int i = 0; i < in.length; i += 4)
						data.add(new Data(Main.decodeInput(in, i + 0), Main.decodeInput(in, i + 2)));
					newData(data);
				}
			}
		} catch (Exception e) {}
	}
	
	public void newData(ArrayList<Data> data){
		for(Data d : data)
			System.out.println("Address: " + d.channel + " Value: " + d.value);
	}
}
