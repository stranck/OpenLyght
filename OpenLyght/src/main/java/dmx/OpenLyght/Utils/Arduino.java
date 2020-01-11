package dmx.OpenLyght.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener;

import java.util.Enumeration;


public abstract class Arduino implements SerialPortEventListener {
	SerialPort serialPort;
	protected BufferedReader input;
	private OutputStream output;
	private static final int TIME_OUT = 2000;

	public Arduino(String portName, int dataRate) {
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

			serialPort.setSerialPortParams(dataRate,
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

	public abstract void serialEvent(SerialPortEvent spEvent);

	public void writeData(String data){
		writeData(data.getBytes());
	}
	public void writeData(byte[] data) {
		try {
			if(output != null)
				output.write(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

