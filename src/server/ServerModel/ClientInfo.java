package server.ServerModel;

import java.net.Socket;

/**
 * Client information: nick name & socket
 * @author luke shen
 *
 */
public class ClientInfo {
	private String nickname;
	private Socket socket;
	
	
	public ClientInfo (String nickname, Socket socket) {
		this.setNickname(nickname);
		this.setSocket(socket);
	}
	
	public String getNickname() {
		return nickname;
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}
