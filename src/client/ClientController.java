package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

/**
 * Client controller
 * @author luke shen
 *
 */
public class ClientController {
	DataOutputStream dout;
	DataInputStream din;
	Socket socket;
	String hostName;
	int portNum;
	
	/**
	 * Connect to server
	 * @param hostName
	 * @param portNum
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public ClientController(String hostName,int portNum) throws IOException, UnknownHostException{
		socket=new Socket(hostName,portNum);
		din = new DataInputStream(socket.getInputStream());
		dout = new DataOutputStream(socket.getOutputStream());
		this.hostName = hostName;
		this.portNum = portNum;
	}
	
	/**
	 * A client re-connect to server after logging out
	 * @param hostName
	 * @param portNum
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void reconnectServer(String hostName,int portNum) throws UnknownHostException, IOException {
		socket=new Socket(hostName,portNum);
		din = new DataInputStream(socket.getInputStream());
		dout = new DataOutputStream(socket.getOutputStream());
		this.hostName = hostName;
		this.portNum = portNum;
	}
	
	/**
	 * A client connect to server at first time
	 * @param usrNickName
	 * @return true if connect to server successfully; false otherwise
	 */
	public boolean connectServer(String usrNickName) {
		try {
			// Send login request to server through socket
			dout.writeUTF("client login##" + usrNickName);
			
			// Wait for response from server
			long startTime = System.currentTimeMillis(); //fetch starting time
			while (true) {
				String str = din.readUTF(); 
				if(str.equals("login confirm")) {
					System.out.println("Client [" + usrNickName+ "] logged in!");
					return true;
				} 
				if (str.equals("duplicate nickname")) {
					socket.close();
					JOptionPane.showMessageDialog(null, "Nick name duplicated! \nPlease Choose another one!");
					break;
				}
				
				// If haven't received response from server over 5 sec, retry log in
				if (System.currentTimeMillis() - startTime < 5000) {
					JOptionPane.showMessageDialog(null, "Login request timeout! \nPlease try again later!");
					break;
				}
			}
		} catch (Exception e1) {
			System.out.println("Client connection refused!");
			JOptionPane.showMessageDialog(null, "Log in failed! \nPlease make sure the server already start and you type in the correct port number!");
		}
		return false; 
	}
	
	/**
	 * Disconnect from server
	 * @param usrNickName
	 */
	public void disconnectServer(String usrNickName) {
		try {
			dout.writeUTF("client logout##"+ usrNickName);
			//socket.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
