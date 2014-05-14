package org.imbox.server.functions.boardcast;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.BufferedOutputStream;
/*author: zhong-ting chen*/
public class SocketClient {
	public int port =8090;//using port
	public SocketClient( int size, String IP) {
		Socket client = new Socket();
		InetSocketAddress isa = new InetSocketAddress(IP, port);
		try {
			client.connect(isa, 10000);
			BufferedOutputStream out = new BufferedOutputStream(client
					.getOutputStream());
			// send string 
			out.write("Send From Server!!! ".getBytes());
			out.flush();
			out.close();
			out = null;
			client.close();
			client = null;
		} catch (java.io.IOException e) {
			System.out.println("Socket connection problem !");
			System.out.println("IOException :" + e.toString());
		}
	}	
}