import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class Sendworker implements Runnable {
	public LinkedList<JSONObject> Jobs = null;
	public HashMap<String, Socket> UsrSocketAddr = null;
	
	public JSONObject JSONMsg = null;
	
	public Sendworker(LinkedList<JSONObject> Jobs, HashMap<String, Socket> UsrSocketAddr) {
		this.Jobs = Jobs;
		this.UsrSocketAddr = UsrSocketAddr;
	}
	
	
	public void run() {
		while(true) {
			if (!Jobs.isEmpty()){
				JSONMsg = Jobs.poll();
				String Sender_pnum = (String) JSONMsg.get("Sender_pnum");
				String Msg_type = (String) JSONMsg.get("Msg_type");
			
				Socket socket;
			
				switch(Msg_type) {
				case "Reg_User":
					socket = UsrSocketAddr.get(Sender_pnum);
					Send(socket, JSONMsg);
					break;
				case "Send_msg":
					JSONArray Recv_usr = JSONMsg.getJSONArray("Recv_usr");
					for (int i = 0; i < Recv_usr.size(); i++) {
						socket = UsrSocketAddr.get(Recv_usr.get(i));
						Send(socket, JSONMsg);
					}
					break;
				}
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ;
	}
}
