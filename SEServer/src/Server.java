import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;  
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import net.sf.json.*;

public class Server {       
	private static final int ServerPort = 22222;
	private static final String ServerIP = "1.242.144.197";

	// Thread Share Data
	private static BlockingQueue<JSONObject> DBJobQueue;
	private static BlockingQueue<JSONObject> SendJobQueue;
	private static HashMap<String, Socket> ActiveUser;
	
	
	public static void main(String[] args) {
		ServerSocket serversocket = null;
		Socket socket = null;
		DBJobQueue = new ArrayBlockingQueue<JSONObject>(100);
		SendJobQueue = new ArrayBlockingQueue<JSONObject>(100);
		ActiveUser = new HashMap<String, Socket>();
		
		// Create DBworker Thread
		Runnable dbworker = new DBworker(DBJobQueue, SendJobQueue);
		Thread DBworkerThread = new Thread(dbworker);
		DBworkerThread.start();
		
		// Create Sendworker Thread
		Runnable sendworker = new Sendworker(SendJobQueue, ActiveUser);
		Thread SendworkerThread = new Thread(sendworker);
		SendworkerThread.start();
		
		System.out.println("Server Starting...");
		
		try {
			serversocket = new ServerSocket(ServerPort);
			// Multi User Connect
			while (true) {
				socket = serversocket.accept();
				if (socket.isConnected()) {
					System.out.print("Connected IP: " + socket.getInetAddress() + "\n");
					Runnable UserRun = new Client(socket, ActiveUser, DBJobQueue);
					Thread UserThread = new Thread(UserRun);
				
					UserThread.start();
					System.out.println(UserThread.getName() + " : Start");
				}
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