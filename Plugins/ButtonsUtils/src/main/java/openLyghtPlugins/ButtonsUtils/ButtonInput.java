package openLyghtPlugins.ButtonsUtils;

import dmx.OpenLyght.Utils.Arduino;
import gnu.io.SerialPortEvent;

public class ButtonInput extends Arduino {

	public ButtonInput(String portName) {
		super(portName, 9600);
	}
	
	@Override
	public synchronized void serialEvent(SerialPortEvent spEvent) {
		if (spEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String s = null;
				while((s = input.readLine()) != null){
					char[] in = s.toCharArray();
					for(char c : in){
						c -= 63;
						newData(c / 2 - 1, c % 2 == 1);
					}						
				}
			} catch (Exception e) {}
		}
	}
	
	public void newData(int button, boolean status){
		System.out.println("Button: " + button + " Status: " + status);
	}

}
