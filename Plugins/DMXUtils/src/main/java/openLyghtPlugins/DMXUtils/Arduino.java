package openLyghtPlugins.DMXUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener;

import java.util.ArrayList;
import java.util.Enumeration;


public class Arduino implements SerialPortEventListener {
	SerialPort serialPort;
	private BufferedReader input;
	private OutputStream output;
	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE = 76800;

	public Arduino(String portName) {
		CommPortIdentifier portId = null;
		Enumeration<?> portEnum = CommPortIdentifier.getPortIdentifiers();

		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			System.out.println(currPortId.getCurrentOwner());
			if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
			}
		}
		if (portId == null) return;

		try {
			serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);

			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String s = null;
				while((s = input.readLine()) != null){
					char[] in = s.toCharArray();
					//System.out.println(s);
					ArrayList<Data> data = new ArrayList<Data>();
					for(int i = 0; i < in.length; i += 4)
						data.add(new Data(Main.decodeInput(in, i + 0), Main.decodeInput(in, i + 2)));
					newData(data);
				}
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}
	}
	
	public void newData(ArrayList<Data> data){
		for(Data d : data)
			System.out.println("Address: " + d.channel + " Value: " + d.value);
	}

	public void writeData(String data) {
		try {
			output.write(data.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

