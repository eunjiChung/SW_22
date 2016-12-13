import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;

import net.sf.json.*;


public class DBworker implements Runnable {
	// Database Connection and Statement
	private java.sql.Connection con;
	private java.sql.Statement st;
	private ResultSet rs;
	
	// Shared Queue
	public BlockingQueue<JSONObject> DBJobQueue = null;
	public JSONObject JSONMsg_in = null;
	public JSONObject JSONMsg_out = null;
	public BlockingQueue<JSONObject> SendJobQueue = null;
	
	// Constructor
	public DBworker(BlockingQueue<JSONObject> DBJobQueue, BlockingQueue<JSONObject> SendJobQueue) {
		try {
			// Connect Database
			con = DriverManager.getConnection("jdbc:mysql://localhost/seserverdb", "chatserver", "007459df");
			st = con.createStatement();
			rs = null;
			
			this.DBJobQueue = DBJobQueue;
			this.SendJobQueue = SendJobQueue;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName() + " : DBworker Start");
		while (true) {
			// Poll in the job queue
			try {
				JSONMsg_in = (JSONObject) DBJobQueue.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String Sender_pnum = (String) JSONMsg_in.get("Sender_pnum");
			System.out.println("Sender_pnum: " + Sender_pnum);
			String Msg_type = (String) JSONMsg_in.get("Msg_type");
			System.out.println("Msg_type: " + Msg_type);
				
			// Convert JSON to java type and Call method
			switch (Msg_type) {
			case "Reg_user":
				String User_name = (String) JSONMsg_in.get("User_name");
				JSONArray Friend_in_JSONArray = JSONMsg_in.getJSONArray("Friend_pnum_list");
				ArrayList<String> Friend_pnum_list = new ArrayList<String>();
				if (Friend_in_JSONArray != null) {
					for (int i = 0; i < Friend_in_JSONArray.size(); i++) {
						Friend_pnum_list.add(Friend_in_JSONArray.get(i).toString());
					}
				}
				RegistUser(Sender_pnum, User_name, Friend_pnum_list);
				break;
			case "Add_friend":
				break;
			case "Drop_user":
				break;
			case "Send_msg":
				// 1:1 Chat
				String Friend_pnum = (String) JSONMsg_in.get("Friend_pnum");
				String Text_msg = (String) JSONMsg_in.get("Text_msg");
				SendMessage(Sender_pnum, Friend_pnum, Text_msg);
				break;
			case "Inv_friend":
				break;
			case "Out_room":
				break;
			case "Giv_master":
				break;
			case "Fout_room":
				break;
			case "Set_anno":
				break;
			case "Del_anno":
				break;
			}
		}
}
	
	public void RegistUser(String Sender_pnum, String User_name, ArrayList<String> Friend_list_in) {
		try {
			List<String> Friend_list_out = new ArrayList<String>();
			rs = null;
			// Is it already exist user?
			rs = st.executeQuery("select Upnum from Userlist where Upnum = \'" + Sender_pnum + "\';");
			// If YES, not add user and send friend list
			// If NO, add user and send friend list
			if (rs.next()) {
				rs = st.executeQuery("select Flist from UserFriendlist where Upnum=\'" + Sender_pnum + "\';");
				while (rs.next()) {
					Friend_list_out.add(rs.getString("Flist"));
				}
			} else {
				st.executeUpdate("insert into Userlist (Upnum, Uname) values (\'"
									+ Sender_pnum + "\', \'" + User_name + "\');");
				ResultSet foundfriend_set = null;
				String foundfriend_str = null;
				for (int i = 0; i < Friend_list_in.size(); i++) {
					String str = Friend_list_in.get(i);
					System.out.println("Freind_list_in[" + i + "] : " + str);
					foundfriend_set = st.executeQuery("select Upnum from Userlist where Upnum=\'"
							 + str + "\';");
					if (foundfriend_set.next()) {
						foundfriend_str = foundfriend_set.getString("Upnum");
						Friend_list_out.add(foundfriend_str);
						st.executeUpdate("insert into UserFriendlist (Upnum, Flist) values (\'"
										+ Sender_pnum + "\', \'" + foundfriend_str + "\');");
					}
				}
			}
			
			// Create out message
			JSONArray Friend_out_JSONArray = new JSONArray();
			for (int i = 0; i < Friend_list_out.size(); i++) {
				Friend_out_JSONArray.add(Friend_list_out.get(i));
			}
			JSONMsg_out = new JSONObject();
			JSONMsg_out.put("Sender_pnum", Sender_pnum);
			JSONMsg_out.put("Msg_type", "Reg_user");
			JSONMsg_out.put("Friend_pnum", Friend_out_JSONArray);
			
			// Put the message in SendQueue
			SendJobQueue.add(JSONMsg_out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void SendMessage(String Sender_pnum, String Friend_pnum, String Text_msg) {
		try {
			// Is exist already room?
			rs = st.executeQuery("select uhr1.Rid from UserHasRoom uhr1, UserHasRoom uhr2 where uhr1.Upnum = \'" 
			+ Sender_pnum + "\' and uhr2.Upnum = \'" + Friend_pnum + "\' and uhr1.rid = uhr2.rid;");
			// If YES, add message in the room
			// If NO, create room and add message in the room
			int rid = rs.getInt("Rid");
			int mid;
			// Date setting
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.KOREA);
			Date nowTime = new Date();
			String nowTime_str = formatter.format(nowTime);
			
			int Room_member_num;
			String Room_member;
			
			if (rid != 0) {
				mid = st.executeQuery("select count(*) from Messagelist where rid = " + rid + " ;").getInt(0) + 1;
			} else {
				rid = st.executeQuery("select count(*) from Roomlist;").getInt(0) + 1;
				mid = 1;
				// Create Room
				st.executeUpdate("insert into Roomlist (Rid, Isgroup, Announce, Rmaster) "
						+ "values (" + rid + ", 0, \'\', \'\');");
				st.executeUpdate("insert into UserHasRoom (Upnum, Rid) values "
						+ "(\'" + Sender_pnum + ", " + rid + ";");
				st.executeUpdate("insert into UserHasRoom (Upnum, Rid) values "
						+ "(\'" + Friend_pnum + ", " + rid + ";");	
			}
			st.executeUpdate("insert into Messagelist (Msgid, Textmsg, Readcnt, Sendtime, Sendusr, Rid) "
					+ "values (" + mid + ", " + Text_msg + ", 2, \'" + nowTime_str + "\', \'" 
					+ Sender_pnum + "\', " + rid + ";");
			
			Room_member_num = st.executeQuery("select count(*) from UserHasRoom where rid = " + rid 
					+ " and Upnum <> \'" + Sender_pnum + "\';").getInt(0);
			
			JSONArray Recv_usr = new JSONArray();
			for (int i = 0; i < Room_member_num; i++) {
				Room_member = st.executeQuery("select Upnum from UserHasRoom where rid = " + rid 
						+ " and Upnum <> \'" + Sender_pnum + "\';").getString("Upnum");
				Recv_usr.add(Room_member);
			}
				JSONMsg_out.put("Msg_type", "Send_msg");
				JSONMsg_out.put("Msg_id", mid);
				JSONMsg_out.put("Text_msg", Text_msg);
				JSONMsg_out.put("Read_cnt", 2);
				JSONMsg_out.put("Send_time", nowTime_str);
				JSONMsg_out.put("Send_usr", Sender_pnum);
				JSONMsg_out.put("Recv_usr", Recv_usr);
				JSONMsg_out.put("Room_id", rid);
			SendJobQueue.add(JSONMsg_out);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
