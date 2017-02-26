/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auth;

import assets.GMButton;
import assets.Res;
import common.Database;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * 
 * @author George
 */
public class LoginBox extends JDialog {
	public LoginBox(JFrame parent) {
		
		super(parent, true);
		
		AuthModel model = AuthModel.getInstance();
		
		// Set up frame properties
		setTitle("Login");
		setSize(270, 150);
		setLocationRelativeTo(null);
		setLayout(null);
		setResizable(false);
                getContentPane().setBackground(Res.BKG_COLOR);
		
		
		// Add components
		JLabel userLabel = new JLabel("Username:");
                userLabel.setForeground(Res.FONT_COLOR);
		userLabel.setBounds(10, 10, 90, 20);
		this.add(userLabel);
		JLabel passLabel = new JLabel("Password:");
                passLabel.setForeground(Res.FONT_COLOR);
		passLabel.setBounds(10, 40, 90, 20);
		this.add(passLabel);
		
		
		JTextField userField = new JTextField(30);
		userField.setBounds(105, 10, 150, 20);
		this.add(userField);
		
		JPasswordField passField = new JPasswordField(30);
		passField.setBounds(105, 40, 150, 20);
		this.add(passField);
		
		GMButton login = new GMButton("Login");
		login.setBounds(85, 70, 100, 30);
		login.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String username = userField.getText();
                                Res.username = username;
				String password = new String(passField.getPassword());
				try {
					model.login(username, password);
                                        Database.loadTheme(username);
					closeLogin();
				} catch (LoginFailedException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}
			}
		});
		this.add(login);
		
		this.setVisible(true);
		
	}
	private void closeLogin() {
		this.dispose();
	}
}
