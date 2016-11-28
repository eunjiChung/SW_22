package hello22.hellochat;

import java.io.*;
import java.net.*;
import android.os.Handler;
import android.os.Message;
import android.content.Context;

public class SocketManager {
	public final static String ip = "1.242.144.197";
	public final static int port = 22222;
	
	private static Socket socket = null;
	
	Handler mHandler;
	Context context;
	
	public static Socket getSocket() throws IOException {
		if(socket == null) {
			socket = new Socket(ip, port);
		}
		
		return socket;
	}

	public static void closeSocket() throws IOException {
			if (socket != null) {
				socket.close();
			}
	}
}