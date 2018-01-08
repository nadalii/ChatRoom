package server;

import server.ServerModel.ClientInfo;
import server.ServerModel.ClientMap;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Transmit messages to public or private
 * @author luke shen
 *
 */
public class MessageTransmit {
	private ClientInfo clientReceiver;
	private ClientInfo clientSender;
	private Socket socket;
	private Socket receiverSocket;
	private Socket senderSocket;
	private DataOutputStream dout;
	private DataOutputStream receiverDout;
	private DataOutputStream senderDout;
	
	/**
	 * Transmit message to public
	 * @param sender
	 * @param msg
	 */
	public void sendMsgPublic(String sender, String msg) {
		try {
			for (ClientInfo each : ClientMap.getInstance().values()) {
				socket = each.getSocket();
				dout = new DataOutputStream(socket.getOutputStream());
				dout.writeUTF("public##" + sender + "##" + msg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Transmit message to private client
	 * @param sender
	 * @param receiver
	 * @param msg
	 */
	public void sendMsgPrivate(String sender, String receiver, String msg) {
		try {
			clientReceiver = ClientMap.getInstance().get(receiver);
			receiverSocket = clientReceiver.getSocket();
			receiverDout = new DataOutputStream(receiverSocket.getOutputStream());
			receiverDout.writeUTF("private##" + sender + "##" + receiver + "##" + msg);
			
			clientSender = ClientMap.getInstance().get(sender);
			senderSocket = clientSender.getSocket();
			senderDout = new DataOutputStream(senderSocket.getOutputStream());
			senderDout.writeUTF("private##" + sender + "##" + receiver + "##" + msg);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
