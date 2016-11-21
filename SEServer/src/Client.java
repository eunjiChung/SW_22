import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;

import org.json.JSONObject;
import org.json.JSONTokener;


public class Client implements Runnable {
	public String User_pnum;
	BufferedReader in;
	
	Socket socket;
	
	public LinkedList<JSONObject> DBJobQueue = null;
	public HashMap<String, Socket> ActiveUser = null;
	
	public JSONObject JSONMsg = null;
	
	public int ServerPort;
	
	public Client(Socket socket, HashMap<String, Socket> UsrSocketAddr, LinkedList<JSONObject> DBJobQueue) {
		this.socket = socket;
		this.DBJobQueue = DBJobQueue;
		this.ActiveUser = UsrSocketAddr;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		while (true) {
			try{
				JSONMsg = new JSONObject(in.readLine());
				this.User_pnum = JSONMsg.get("Sender_pnum").toString();
				// Add the user phone number and socket
				if (ActiveUser.get(this.User_pnum) != null) {
					ActiveUser.put(this.User_pnum, socket);
				}
				DBJobQueue.add(JSONMsg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
