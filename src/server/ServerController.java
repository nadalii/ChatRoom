package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

import client.ClientController;
import server.ServerModel.ClientInfo;
import server.ServerModel.ClientMap;

/**
 * Server controller
 * @author luke shen
 *
 */
public class ServerController extends Thread{
	private static int portNumber;
	private static ServerSocket serverSocket;
	public static boolean flag;
	public static ArrayList<Socket> socketThreadList = new ArrayList<Socket>();
	
	public ServerController (int portNumber) {
		try {	
			setPortNumber(portNumber);
			ServerController.setServerSocket(new ServerSocket(portNumber));
			System.out.println("Start Server :" + getServerSocket().toString().substring(12));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Start or re-start server
	 */
	public static void runServer (){
		try{
			// Exclusive for re-start server, broadcast its restart to all former connected clients 
			DataOutputStream dout;
			for(ClientInfo each : ClientMap.getInstance().values()) {
				Socket socket = each.getSocket();
				try {
					dout = new DataOutputStream(socket.getOutputStream());
					dout.writeUTF("server restarted##" + each.getNickname());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			// Create server socket and listen to client log in requests
			flag = true;
			while(flag)
			{
				Socket clientSocket = getServerSocket().accept();
				socketThreadList.add(clientSocket);
                ServerMultiThreads usrThread = new ServerMultiThreads(clientSocket, socketThreadList);
                
                usrThread.start();
			}
			//getServerSocket().close();
			//ServerController.getServerSocket().setReuseAddress(true);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
	        if (getServerSocket() != null) {
	            try {
	            	// Broadcast server stop to all the clients
	            	DataOutputStream dout;
	            	for(Socket each : socketThreadList) {
	            		dout = new DataOutputStream(each.getOutputStream());
	            		dout.writeUTF("server stopped");
	        		}
	                getServerSocket().close();
	            } catch (IOException e) {
	                // log error just in case
	            }
	        }
	    }
		
	}
	
	/**
	 * Stop the server
	 */
	public void stopServer(){
		try {
			flag = false;
			// Just to escape from getServerSocket().accept(), so the while loop can run again, then go to break
			try {  
				ClientController in = new ClientController("localhost", portNumber);
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
			//ServerController.getServerSocket().close();
			System.out.println("Stop Server!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Close the server window
	 */
	public void quitServer(){
		DataOutputStream dout;
		stopServer();
		for(ClientInfo each : ClientMap.getInstance().values()) {
			Socket socket = each.getSocket();
			try {
				dout = new DataOutputStream(socket.getOutputStream());
				dout.writeUTF("server quit");
				System.out.println("The server is quit!");
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void run(){
		runServer();
	}

	public static ServerSocket getServerSocket() {
		return serverSocket;
	}

	public static void setServerSocket(ServerSocket serverSocket) {
		ServerController.serverSocket = serverSocket;
	}
	
	public static int getPortNumber() {
		return portNumber;
	}

	public static void setPortNumber(int portNumber) {
		ServerController.portNumber = portNumber;
	}

}
