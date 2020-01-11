package openLyghtPlugins.DMXUtils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Sender {
	
	//public static final String RESPONSE_DISCOVERY = "I WANT TO KNOW, VORREI SAPERE";
	//public static final char MSG_DISCOVERY = '?';
	//public static final char MSG_CONFIRMED = 'y';
	public static final char MSG_RESEND = '!';
	public static final int INDEX_LEN1 = 0;
	public static final int INDEX_LEN2 = 1;
	public static final int INDEX_CHK = 2;
	public static final int INDEX_CMD = 3;
	public static final int INDEX_MSG = 4;
	public static final int BUFFER = 4096;
	//public static final int TIMEOUT = 10;
	//private static final byte[] MSG_CNF = { MSG_CONFIRMED };
	private static final byte[] MSG_ERR = { MSG_RESEND };
	private byte[] data = new byte[BUFFER];
	private byte[] cnfBuffer = new byte[1];
	private boolean server = false;
	private DatagramSocket socket;
	private InetAddress address;
	private int port;
	public DatagramPacket lastDP;
	public byte[] msg;

	public Sender(int port) throws Exception{
		socket = new DatagramSocket(port);
		server = true;
	}
	public Sender(int port, InetAddress ia, int timeout) throws Exception{
		this.port = port;
		address = ia;
		socket = new DatagramSocket();
		socket.setSoTimeout(timeout);
	}
	
	public char send(String message, int limit) throws Exception{
		return (char) send(message, address, port, limit);
	}
	public byte send(String message, InetAddress ia, int port, int limit) throws Exception {
		byte[] msg = message.getBytes();
		int length = msg.length;
		byte[] sendData = new byte[length + 3];
		sendData[INDEX_LEN1] = (byte) (length >> 8);
		sendData[INDEX_LEN2] = (byte) (length & 0xff);
		sendData[INDEX_CHK] = calcValidityAndCopy(msg, 0, length, sendData, INDEX_MSG);
		sendData[INDEX_CMD] = msg[0];
		if(!server){
			int i;
			cnfBuffer[0] = MSG_RESEND;
			for(i = 0; i < limit; i++){
				try{
					socket.send(new DatagramPacket(sendData, sendData.length, ia, port));
					DatagramPacket packet = new DatagramPacket(cnfBuffer, 1);
					socket.receive(packet);
					if(cnfBuffer[0] != MSG_RESEND) break;
				} catch(Exception e){/*e.printStackTrace();*/}
			}
			return cnfBuffer[0];
		}
		socket.send(new DatagramPacket(sendData, sendData.length, ia, port));
		return '\0';
	}
	public byte receive(char responseCode) throws Exception{
		byte ret = '\0';
		do {
			lastDP = new DatagramPacket(data, BUFFER);
			socket.receive(lastDP);
			int length = data[INDEX_LEN1] << 8 & 0xff00 | data[INDEX_LEN2] & 0xff;
			msg = new byte[length - 1];
			if(calcValidityAndCopy(data, INDEX_CMD, length + INDEX_CMD, msg, 0) == data[INDEX_CHK]){
				if(server)
					socket.send(new DatagramPacket(new byte[]{(byte) responseCode}, 1, lastDP.getAddress(), lastDP.getPort()));
				ret = data[INDEX_CMD];
			} else if(server){
				socket.send(new DatagramPacket(MSG_ERR, 1, lastDP.getAddress(), lastDP.getPort()));
			}
		} while(ret == '\0');
		return ret;
	}
		
	public static byte calcValidityAndCopy(byte[] src, int srcStart, int srcEnd, byte[] dst, int dstStart){
		byte b = src[srcStart];
		while(++srcStart < srcEnd){
			b ^= src[srcStart];
			dst[dstStart++] = src[srcStart];
		}
		return b;
	}
	
	public String getString(){
		return new String(msg);
	}
}
