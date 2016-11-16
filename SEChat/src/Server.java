import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;  

public class Server implements Runnable {       
	public static final int ServerPort = 22222;     
	public static final String ServerIP = "1.242.144.197";      
	
	@Override     
	public void run() {           
		try {             
			System.out.println("S: Server Start");             
			ServerSocket serverSocket = new ServerSocket(ServerPort);
			while (true) {                 
				Socket client = serverSocket.accept();                 
				System.out.print("S: Connected Client IP: " + client.getInetAddress() + "\n");                 
				try {                     
					BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));                     
					String str = in.readLine();                     
					System.out.println("S: Received: '" + str + "'");                                           
					PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);                     
					out.println("Server Received " + str);
					out.flush();
				} catch (Exception e) {                     
					System.out.println("S: Error");                     
					e.printStackTrace();                 
				} finally {                                          
					System.out.println("S: Done.");                 
				}             
			}         
		} catch (Exception e) {             
			System.out.println("S: Error");            
			e.printStackTrace();         
		}     
	}       
	
	public static void main(String[] args) {           
		Thread desktopServerThread = new Thread(new Server());         
		desktopServerThread.start();       
		}   
}