package client;


import java.io.IOException;
import java.net.UnknownHostException;
import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

/**
 * The view of client
 * @author luke shen
 *
 */
public class ClientView implements Runnable{

	private JFrame frame;
	
	private String userNickName;
	private ClientController cc;
	private String hostName;
	private int portNum;
	private DefaultListModel<String> usrList = new DefaultListModel<String>();
	private DefaultComboBoxModel<String> usrCombo = new DefaultComboBoxModel<String>();

	private JButton btnSendMsg;
	private JButton btnLogin;
	private JButton btnLogout;
	private JButton btnClear;
	private JTextPane bulletinBoard;


	Thread t;

	/**
	 * Create the application.
	 */
	public ClientView(String userNickName, ClientController cc) {
		initialize(userNickName, cc);
		frame.setVisible(true);
		listenToServerThread();
	}
	
	public void listenToServerThread() {
		t = new Thread(this);
		// run the new client thread
		t.start();
	}

	/**
	 * Initialize the contents of the frame.
	 * @param userNickName 
	 */
	private void initialize(String userNickName, ClientController cc) {
		this.cc = cc;
		this.userNickName = userNickName;
		this.portNum = cc.portNum;
		this.hostName = cc.hostName;
		
		frame = new JFrame();
		frame.setTitle(userNickName + " - " + cc.socket.getRemoteSocketAddress());
		frame.setBounds(100, 100, 482, 494);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		bulletinBoard = new JTextPane();
		bulletinBoard.setEditable(false);
		bulletinBoard.setBounds(112, 30, 332, 178);
		frame.getContentPane().add(bulletinBoard);

		JTextArea txtMsg = new JTextArea();
		txtMsg.setBounds(6, 226, 262, 46);
		frame.getContentPane().add(txtMsg);
		
		// "Send to" button
		btnSendMsg = new JButton("Send");
		btnSendMsg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String receiverNickName = (String) usrCombo.getSelectedItem(); 
				if (!cc.socket.isClosed()) {
					try {
						cc.dout.writeUTF("message##" + userNickName + "##" + receiverNickName + "##" + txtMsg.getText());
						txtMsg.setText("");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
					
			}
		});
		btnSendMsg.setBounds(383, 386, 82, 29);
		frame.getContentPane().add(btnSendMsg);
		
		// "log out" button
		btnLogout = new JButton("Log out");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					cc.disconnectServer(userNickName);
					btnLogout.setEnabled(false);
					btnLogin.setEnabled(true);
					usrList.removeAllElements();
					usrCombo.removeAllElements();
					//System.exit(0);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnLogout.setBounds(364, 274, 98, 29);
		frame.getContentPane().add(btnLogout);

		// "clear" button
		btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				bulletinBoard.setText("");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnClear.setBounds(305, 386, 82, 29);
		frame.getContentPane().add(btnClear);



		
		// "Log in" button
		btnLogin = new JButton("Log in");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					cc.reconnectServer(hostName, portNum);
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				boolean connected = cc.connectServer(userNickName);	
				if (connected) {
					listenToServerThread();
					btnLogin.setEnabled(false);
					btnLogout.setEnabled(true);
					btnSendMsg.setEnabled(true);
				}
			}
		});
		btnLogin.setEnabled(false);
		btnLogin.setBounds(364, 309, 98, 23);
		frame.getContentPane().add(btnLogin);
		
		JLabel lblOnlineUsers = new JLabel("Online Users");
		lblOnlineUsers.setHorizontalAlignment(SwingConstants.CENTER);
		lblOnlineUsers.setBounds(364, 6, 101, 16);
		frame.getContentPane().add(lblOnlineUsers);
		
		JLabel lblBroadboard = new JLabel("CS513 ChatRoom");
		lblBroadboard.setHorizontalAlignment(SwingConstants.CENTER);
		lblBroadboard.setBounds(6, 6, 335, 16);
		frame.getContentPane().add(lblBroadboard);

		
		JComboBox<String> comboBox = new JComboBox<String>(usrCombo);
		comboBox.setMaximumRowCount(20);
		comboBox.setBounds(342, 351, 124, 32);
		frame.getContentPane().add(comboBox);
		
		JList<String> list = new JList<String>(usrList);
		list.setEnabled(false);
		list.setBounds(50, 30, 82, 184);
		frame.getContentPane().add(list);
		
		JScrollPane scrollPaneBulletin = new JScrollPane(bulletinBoard);
		scrollPaneBulletin.setBounds(16, 30, 321, 294);
		frame.getContentPane().add(scrollPaneBulletin);

		
		JScrollPane scrollPaneMsg = new JScrollPane(txtMsg);
		scrollPaneMsg.setBounds(16, 351, 271, 62);
		frame.getContentPane().add(scrollPaneMsg);
		
		JScrollPane scrollPaneUsr = new JScrollPane(list);
		scrollPaneUsr.setBounds(364, 30, 100, 232);
		frame.getContentPane().add(scrollPaneUsr);
		
		JLabel lblTo = new JLabel("To");
		lblTo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTo.setBounds(305, 351, 46, 29);
		frame.getContentPane().add(lblTo);

		
		// When closing the window of a client, it means this client logged out the server
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					if (!cc.socket.isClosed()) {
						cc.disconnectServer(userNickName);
					}
					System.exit(0);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}

	@Override
	public void run() {
		Style def = bulletinBoard.getStyledDocument().addStyle(null, null);
		StyleConstants.setFontFamily(def, "arial");
		StyleConstants.setFontSize(def, 12);
		Style normal = bulletinBoard.addStyle("normal", def);
		Style red = bulletinBoard.addStyle("red", normal);
		StyleConstants.setForeground(red, Color.RED);

		bulletinBoard.setParagraphAttributes(normal, true);
		// TODO Auto-generated method stub
		String info;
		try {
			usrCombo.addElement("All");
			while ((info = cc.din.readUTF()) != null) {
				String s[] = info.split("##");
				if (s[0].equals("create list")) {
					if(!s[1].equals(userNickName)) {
						usrCombo.addElement(s[1]);
					}
					usrList.addElement(s[1]);
				} 
				if(s[0].equals("add one")) {
					if(!s[1].equals(userNickName)) {
						usrCombo.addElement(s[1]);
					}
					usrList.addElement(s[1]);
					bulletinBoard.getDocument().insertString(bulletinBoard.getDocument().getLength(),"[SYSTEM]: " + s[1] + " log in!\n",normal);
				} 
				if(s[0].equals("remove one")) {
					if (s[1].equals(userNickName)) {
						btnSendMsg.setEnabled(false);
						bulletinBoard.getDocument().insertString(bulletinBoard.getDocument().getLength(),"[SYSTEM]: " + s[1] + " log out!\n",normal);

						break;
					} else {
						usrList.removeElement(s[1]);
						usrCombo.removeElement(s[1]);
						bulletinBoard.getDocument().insertString(bulletinBoard.getDocument().getLength(),"[SYSTEM]: " + s[1] + " log out!\n",normal);

					}
					
				}
				if (s[0].equals("public")) {
					bulletinBoard.getDocument().insertString(bulletinBoard.getDocument().getLength(),"[" + s[1] + "]: " + s[2] + "\n",normal);

				}
				if (s[0].equals("private")) {
					if (s[2].equals(userNickName)) {
						bulletinBoard.getDocument().insertString(bulletinBoard.getDocument().getLength(),"[" + s[1] + " to me]: " + s[3] + "\n",bulletinBoard.getStyle("red"));

					}
					if(s[1].equals(userNickName)) {
						bulletinBoard.getDocument().insertString(bulletinBoard.getDocument().getLength(),"[me to " + s[2] + "]: " + s[3] + "\n",bulletinBoard.getStyle("red"));

					}
				}
				if (s[0].equals("server stopped")) {
					bulletinBoard.getDocument().insertString(bulletinBoard.getDocument().getLength(),"[SYSTEM]: Stop Server!\n",normal);
					bulletinBoard.getDocument().insertString(bulletinBoard.getDocument().getLength(),"[SYSTEM]: You can either log out or wait until the server restart!\n",normal);

					btnSendMsg.setEnabled(false);
				}
				if (s[0].equals("server quit")) {
					bulletinBoard.getDocument().insertString(bulletinBoard.getDocument().getLength(),"[SYSTEM]: Server quit forever!\n",normal);

					bulletinBoard.getDocument().insertString(bulletinBoard.getDocument().getLength(),"[SYSTEM]: Please close the window and choose another server\n",normal);

					btnLogout.setEnabled(false);
					btnSendMsg.setEnabled(false);
					break;
				}
				if (s[0].equals("server restarted")) {
					bulletinBoard.getDocument().insertString(bulletinBoard.getDocument().getLength(),"[SYSTEM]: Server restart!\n",normal);

					btnSendMsg.setEnabled(true);
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cc.socket != null) {
				try {
					cc.socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}


	public DefaultComboBoxModel getUsrCombo() {
		return usrCombo;
	}

	public void setUsrCombo(DefaultComboBoxModel usrCombo) {
		this.usrCombo = usrCombo;
	}
}
