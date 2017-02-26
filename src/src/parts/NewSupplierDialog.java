/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parts;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;

/**
 * DIalog box for creating or editing a supplier.
 * @author George
 */
public class NewSupplierDialog extends GMDialog {
	JTextField nameField, telField;
	String oldName;
	
	/**
	 * Constructor for use when creating an all new supplier.
	 */
	public NewSupplierDialog() {
		setTitle("Add Supplier");
		init();
		
		// Add action listener for adding a new supplier.
		confirm.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SupplierController controller = new SupplierController(panel);
				if (controller.add(nameField.getText(), telField.getText())) 
					dispose();
			}
		});
		setVisible(true);
	}
	
	/**
	 * Constructor for use when editing an old supplier.
	 * @param oldName Name of supplier you want to edit. Is UNIQUE in DB.
	 * @param tel Telephone of supplier you want to edit.
	 */
	public NewSupplierDialog(String oldName, String tel) {
		this.oldName = oldName;
		setTitle("Edit Supplier");
		init();
		
		//Populate fields with data.
		nameField.setText(oldName);
		telField.setText(tel);
		
		//Add action listener for editing.
		confirm.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SupplierController controller = new SupplierController(panel);
				if (controller.edit(oldName, nameField.getText(), telField.getText()))
					dispose();
			}
		});
		setVisible(true);
	}
	
	/**
	* Set up ui.
	*/
	private void init() {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		c.gridx = 0;
		c.gridy = 0;
		panel.add(new GMLabel("Name:"), c);
		
		c.gridx = 0;
		c.gridy = 1;
		panel.add(new GMLabel("Tel:"), c);
		
		c.weightx = 1;
		
		c.gridx = 1;
		c.gridy = 0;
		nameField = new JTextField();
		panel.add(nameField, c);
		
		c.gridx = 1;
		c.gridy = 1;
		telField = new JTextField();
		panel.add(telField, c);
		
		pack();
	}
	
}
