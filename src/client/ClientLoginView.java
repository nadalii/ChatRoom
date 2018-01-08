package client;

import javax.swing.*;

import java.awt.Color;
import java.awt.Font;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * The view of log in
 * @author luke shen
 *
 */
public class ClientLoginView {

	private JFrame frame;
	private JTextField txtNickName;
	private JTextField txtPortNum;
	
	private ClientController cc;
	private DataInputStream  din;
	private DataOutputStream dout;




	/**
	 * Create the application.
	 */
	public ClientLoginView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(300, 300, 350, 370);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Client Chatroom Login");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 18));
		lblNewLabel.setForeground(Color.black);
		lblNewLabel.setBounds(0, 6, 350, 49);
		frame.getContentPane().add(lblNewLabel);
		
		txtNickName = new JTextField();
		txtNickName.setBounds(152, 67, 134, 28);
		frame.getContentPane().add(txtNickName);
		txtNickName.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Nick Name: ");
		lblNewLabel_1.setBounds(53, 73, 87, 16);
		frame.getContentPane().add(lblNewLabel_1);

		
		txtPortNum = new JTextField();
		txtPortNum.setText("1111");
		txtPortNum.setBounds(152, 107, 134, 28);
		frame.getContentPane().add(txtPortNum);
		txtPortNum.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Port Number:");
		lblNewLabel_2.setBounds(53, 113, 87, 16);
		frame.getContentPane().add(lblNewLabel_2);
		
		// "log in" button
		JButton btnLogin = new JButton("Log in");
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(txtNickName.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Please input your nick name!");
				} else if(txtPortNum.getText().equals("")){
					JOptionPane.showMessageDialog(null, "Please input server's port number!");
				} else {
					try {
						cc = new ClientController("localhost", Integer.parseInt(txtPortNum.getText()));
						boolean connected = cc.connectServer(txtNickName.getText());	
						if (connected) {
							frame.dispose();
							ClientView cv = new ClientView(txtNickName.getText(), cc);
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						System.out.println("Client connection refused!");
						JOptionPane.showMessageDialog(null, "Log in failed! \nPlease make sure the server already start and you type in the correct port number!");
					}
					
				}
			}
		});
		btnLogin.setBounds(106, 206, 117, 29);
		frame.getContentPane().add(btnLogin);
	}

	public JFrame getFrame(){
		return  this.frame;
	}
}
