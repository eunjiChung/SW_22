package com.example.seclient; 
import java.io.BufferedReader; 
import java.io.BufferedWriter; 
import java.io.IOException;  
import java.io.InputStreamReader; 
import java.io.OutputStreamWriter; 
import java.io.PrintWriter; 
import java.net.Socket;   
import android.app.Activity; 
import android.os.Bundle; 
import android.os.Handler; 
import android.util.Log; 
import android.view.View; 
import android.view.View.OnClickListener; 
import android.widget.Button; 
import android.widget.EditText; 
import android.widget.TextView; 
import android.widget.Toast;   

public class Client extends Activity {       
	private String html = "";     
	private Handler mHandler;       
	private Socket socket;       
	private BufferedReader networkReader;     
	private BufferedWriter networkWriter;       
	private String ip = "1.242.144.197"; // IP     
	private int port = 22222; // PORT¹øÈ£
	EditText et;
	Button btn;
	TextView tv;
	
	@Override     
	protected void onStop() {         
		super.onStop();         
		try {             
			socket.close();         
		} catch (IOException e) {             
			e.printStackTrace();         
		}     
	}       
	
	public void onCreate(Bundle savedInstanceState) {         
		super.onCreate(savedInstanceState);         
		setContentView(R.layout.activity_main);         
		mHandler = new Handler();          
		new Thread() {
			public void run() {
				try {
					setSocket(ip, port);
				} catch (IOException e1) {             
					e1.printStackTrace();       
				}           
			}
		}.start();
		checkUpdate.start();          
		
		et = (EditText) findViewById(R.id.EditText01);         
		btn = (Button) findViewById(R.id.Button01);         
		tv = (TextView) findViewById(R.id.TextView01);
		btn.setOnClickListener(new OnClickListener() {               
			public void onClick(View v) {                
				if (et.getText().toString() != null || !et.getText().toString().equals("")) {                     
					PrintWriter out = new PrintWriter(networkWriter, true);                     
					String return_msg = et.getText().toString();                     
					out.println(return_msg);
					out.flush();
				}             
			}         
		});
	}       
	
	private Thread checkUpdate = new Thread() {           
		public void run() {             
			try {                 
				String line;                 
				Log.w("ChattingStart", "Start Thread");                
				while (true) {                     
					Log.w("Chatting is running", "chatting is running");
					line = networkReader.readLine();                     
					html = line;
					tv.setText(html);
					mHandler.post(showUpdate);                 
				}             
			} catch (Exception e) {               
				
			}         
		}    
	};       
	
	private Runnable showUpdate = new Runnable() {           
		public void run() {             
			Toast.makeText(Client.this, "Coming word: " + html, Toast.LENGTH_LONG).show();
		}       
	};       
	
	public void setSocket(String ip, int port) throws IOException {           
		try {             
			socket = new Socket(ip, port);             
			networkWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));             
			networkReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));         
		} catch (IOException e) {            
			System.out.println(e);             
			e.printStackTrace();        
		}       
	}   
}