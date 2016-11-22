import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Sendworker implements Runnable {
	public LinkedList<JSONObject> SendJobQueue = null;
	public HashMap<String, Socket> UsrSocketAddr = null;
	
	public JSONObject JSONMsg = null;
	
	public Sendworker(LinkedList<JSONObject> SendJobQueue, HashMap<String, Socket> UsrSocketAddr) {
		this.SendJobQueue = SendJobQueue;
		this.UsrSocketAddr = UsrSocketAddr;
	}
	
	
	public void run() {
		while(true) {
			try {
				if (!SendJobQueue.isEmpty()) {
					// Poll in the job queue
					JSONMsg = SendJobQueue.poll();
					String Sender_pnum = (String) JSONMsg.get("Sender_pnum");
					String Msg_type = (String) JSONMsg.get("Msg_type");

					Socket socket;

					switch (Msg_type) {
						case "Reg_User":
							// Re-send
							socket = UsrSocketAddr.get(Sender_pnum);
							Send(socket, JSONMsg);
							break;
						case "Send_msg":
							// Send to all room member
							JSONArray Recv_usr = JSONMsg.getJSONArray("Recv_usr");
							for (int i = 0; i < Recv_usr.length(); i++) {
								socket = UsrSocketAddr.get(Recv_usr.get(i));
								Send(socket, JSONMsg);
							}
							break;
					}
				}
			} catch (JSONException jsone) {
				jsone.printStackTrace();
			}
		}
	}
	
	public void Send(Socket socket, JSONObject JSONMsg) {
		if (socket == null) {
			return ;
		}
		PrintWriter out;
		try {
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			out.print(JSONMsg);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ;
	}
}
