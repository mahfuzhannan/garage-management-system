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
import parts.GMCheckbox;
import parts.GMDialog;

/**
 *
 * @author George
 */
public class ChangePrivsDialog extends GMDialog {
	int id;
	JCheckBox[] checkBoxes;
	ActionListener confirmListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			int[] privs = new int[6];
			for (int i = 0; i < privs.length; i ++) {
				privs[i] = checkBoxes[i].isSelected() ? 1 : 0;
			}
			UsersModel.getInstance().changePrivs(id, privs);
			dispose();
		}
	};
	
	public ChangePrivsDialog(int id) {
		this.id = id;
		setTitle("Change Privileges");
		
		checkBoxes = new JCheckBox[6];
		checkBoxes[0] = new GMCheckbox("Customers");
		checkBoxes[1] = new GMCheckbox("Vehicles");
		checkBoxes[2] = new GMCheckbox("DAR");
		checkBoxes[3] = new GMCheckbox("Parts");
		checkBoxes[4] = new GMCheckbox("SchedMaint");
		checkBoxes[5] = new GMCheckbox("Users");
		
		confirm.addActionListener(confirmListener);
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill= GridBagConstraints.HORIZONTAL;
		c.insets = new  Insets(5, 5, 5, 5);
		
		c.gridx = 0;
		c.gridy = 0;
		
		for (int i = 0; i < checkBoxes.length; i++) {
			panel.add(checkBoxes[i], c);
			c.gridy +=1;
		}
		
		pack();
		setVisible(true);
	}
}
