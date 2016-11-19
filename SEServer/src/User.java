import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;

import net.sf.json.JSONObject;


public class User implements Runnable {
	public String User_pnum;;
	BufferedReader in;
	
	Socket socket;
	
	public LinkedList<JSONObject> Jobs = null;
	public HashMap<String, Socket> UsrSocketAddr = null;
	
	public JSONObject JSONMsg = null;
	
	public int ServerPort;
	
	public User(Socket socket, HashMap<String, Socket> UsrSocketAddr, LinkedList<JSONObject> Jobs) {
		this.socket = socket;
		this.Jobs = Jobs;
		this.UsrSocketAddr = UsrSocketAddr;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void SetUserPnum(String User_pnum) {
		this.User_pnum = User_pnum;
	}
	
	public void run() {
		while (true) {
			try {
				JSONMsg = JSONObject.fromObject(in.readLine());
				SetUserPnum(JSONMsg.get("Sender_pnum").toString());
				if (UsrSocketAddr.get(this.User_pnum) != null) {
					UsrSocketAddr.put(this.User_pnum, socket);
				}
				Jobs.add(JSONMsg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
