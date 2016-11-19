import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;  
import java.util.HashMap;
import java.util.LinkedList;

import net.sf.json.JSONObject;

public class Server {       
	public static final int ServerPort = 22222;     
	public static final String ServerIP = "1.242.144.197";

	// Thread Share Queue
	public static LinkedList<JSONObject> DBJobQueue;
	public static LinkedList<JSONObject> SendJobQueue;
	public static HashMap<String, Socket> UsrSocketAddr;
	
	
	public static void main(String[] args) {
		ServerSocket serversocket = null;
		Socket socket = null;
		DBJobQueue = new LinkedList<JSONObject>();
		SendJobQueue = new LinkedList<JSONObject>();
		UsrSocketAddr = new HashMap<String, Socket>();
		
		Runnable dbworker = new DBworker(DBJobQueue, SendJobQueue);
		Thread DBworkerThread = new Thread(dbworker);
		DBworkerThread.start();
		
		Runnable sendworker = new Sendworker(SendJobQueue, UsrSocketAddr);
		Thread SendworkerThread = new Thread(sendworker);
		SendworkerThread.start();
		
		System.out.println("Server Starting...");
		
		try {
			serversocket = new ServerSocket(ServerPort);
			while (true) {
				socket = serversocket.accept();
				Runnable UserRun = new User(socket, UsrSocketAddr, DBJobQueue);
				Thread UserThread = new Thread(UserRun);
				
				UserThread.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (serversocket != null) {
				try {
					serversocket.close();
					System.out.println("Server Closed...");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}