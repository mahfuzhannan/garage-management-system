/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package settings;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPasswordField;
import parts.GMDialog;
import parts.GMLabel;

/**
 *
 * @author George
 */
public class ChangePasswordDialog extends GMDialog {
	
	JPasswordField password1, password2;
	int id;
	ActionListener confirmListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String pass1 = new String(password1.getPassword());
			String pass2 = new String(password2.getPassword());
			UsersController controller = new UsersController(panel);
			if (controller.changePass(id, pass1, pass2)) dispose();
		}
	};
	
	public ChangePasswordDialog(int id) {
		this.id = id;
		
		setTitle("Change Password");		
		GridBagConstraints c = new GridBagConstraints();
		
		confirm.addActionListener(confirmListener);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new  Insets(5, 5, 5, 5);
		
		c.gridx = 0;
		c.gridy = 0;
		panel.add(new GMLabel("Password:"), c);
		
		c.gridx = 0;
		c.gridy = 1;
		panel.add(new GMLabel("Repeat:"), c);
		
		c.weightx = 1;
		
		password1 = new JPasswordField();
		c.gridx = 1;
		c.gridy = 0;
		panel.add(password1, c);
		
		password2 = new JPasswordField();
		c.gridx = 1;
		c.gridy = 1;
		panel.add(password2, c);
		
		pack();
		setVisible(true);
	}
}
