package hello22.hellochat;

import java.io.*;
import java.net.*;
import android.util.Log;
import android.os.Handler;
import android.os.Message;
import android.content.Context;

public class SocketListener extends Thread {
	private InputStream in;
	private OutputStream out;
	private BufferedReader br;
	private PrintWriter pw;
	
	Handler mHandler;
	Context context;
	
	public SocketListener(Context context, Handler handler) {
		this.mHandler = handler;
		this.context = context;
		
		try {
			in = SocketManager.getSocket().getInputStream();
			br = new BufferedReader(new InputStreamReader(in));
			out = SocketManager.getSocket().getOutputStream();
            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)), true);			
		} catch (IOException e) {
			Log.e("SocketListener", e.getMessage());
		}
	}
	
	@Override
	public void run() {
		super.run();
		while(true) {
			try {
				String receivedmsg;
				while((receivedmsg = br.readLine())!= null) {
					Log.e("SocketListener", receivedmsg);
					Message msg = Message.obtain(mHandler, 0, 0, 0, receivedmsg);
					mHandler.sendMessage(msg);          
				}
			} catch (IOException e) {
				Log.e("SocketListener",e.getMessage());
			}
		}
	}
	
	public void sendMsg(String Msg) {
		pw.println(Msg);
		pw.flush();
	}
}