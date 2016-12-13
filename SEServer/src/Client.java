import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

import net.sf.json.*;


public class Client implements Runnable {
	public String User_pnum = null;
	BufferedReader in;
	
	Socket socket;
	
	public BlockingQueue<JSONObject> DBJobQueue = null;
	public HashMap<String, Socket> ActiveUser = null;
	
	public JSONObject JSONMsg = null;
	
	public int ServerPort;
	
	public Client(Socket socket, HashMap<String, Socket> UsrSocketAddr, BlockingQueue<JSONObject> DBJobQueue) {
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
				if (socket.isClosed()) {
					break;
				}
				JSONMsg = JSONObject.fromObject(in.readLine());
				if (!JSONMsg.isNullObject()) {
					if (this.User_pnum == null) {
						this.User_pnum = JSONMsg.get("Sender_pnum").toString();
						// Add the user phone number and socket
						if (!ActiveUser.containsKey(User_pnum)) {
							ActiveUser.put(this.User_pnum, socket);
						}
					}
					System.out.println(this.User_pnum + " : " + ActiveUser.get(this.User_pnum));
					System.out.println(Thread.currentThread().getName() + " : " + JSONMsg);
					DBJobQueue.add(JSONMsg);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.ActiveUser.remove(this.User_pnum);
		System.out.println(Thread.currentThread().getName() + " : Stop");
	}
}
