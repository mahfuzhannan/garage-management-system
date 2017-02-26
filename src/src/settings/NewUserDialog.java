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
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import parts.GMCheckbox;
import parts.GMDialog;
import parts.GMLabel;

/**
 *
 * @author George
 */


public class NewUserDialog extends GMDialog {
	
	JCheckBox[] checkBoxes;
	JTextField username;
	JPasswordField password1, password2;
	ActionListener confirmListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String user = username.getText();
			String pass1 = new String(password1.getPassword());
			String pass2 = new String(password2.getPassword());


			UsersController controller = new UsersController(panel);

			boolean[] privs = new boolean[6];
			for(int i = 0; i < 6; i++) {
				privs[i] = checkBoxes[i].isSelected();
			}
			if (controller.add(user, pass1, pass2, privs))dispose();
		}
	};
	
	public NewUserDialog() {
		setTitle("New User");		
		GridBagConstraints c = new GridBagConstraints();
		
		confirm.addActionListener(confirmListener);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new  Insets(5, 5, 5, 5);
		c.weightx = 0;
		panel.add(new GMLabel("Username:"), c);
		
		username = new JTextField();
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
		panel.add(username, c);
		
		c.weightx = 0;
		c.gridx = 0;
		c.gridy = 1;
		panel.add(new GMLabel("Password:"), c);
		
		password1 = new JPasswordField();
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 1;
		panel.add(password1, c);
		
		password2 = new JPasswordField();
		c.gridx = 1;
		c.gridy = 2;
		c.weightx = 1;
		panel.add(password2, c);
		
		
		checkBoxes = new JCheckBox[6];
		checkBoxes[0] = new GMCheckbox("Customers");
		checkBoxes[1] = new GMCheckbox("Vehicles");
		checkBoxes[2] = new GMCheckbox("DAR");
		checkBoxes[3] = new GMCheckbox("Parts");
		checkBoxes[4] = new GMCheckbox("SchedMaint");
		checkBoxes[5] = new GMCheckbox("Users");
		
		c.gridwidth = 2;
		c.gridx = 0;
		c.weightx = 0;
		for (JCheckBox checkBoxe : checkBoxes) {
			c.gridy += 1;
			panel.add(checkBoxe, c);
		}
		
		
		pack();
		setVisible(true);
		
	}
	
}
