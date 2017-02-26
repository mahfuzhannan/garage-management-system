package parts;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;

/**
 * The dialog for creating or editing a new part.
 * @author George
 */
public class NewPartDialog extends GMDialog {
	JTextField nameField, descField, stockField, priceField;
	int partID;
	
	// When adding a new part use this actionlistener for confirm
	ActionListener confirmListenerAdd = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			PartController controller = new PartController(panel);
			if(controller.newPart(nameField.getText(), descField.getText(), 
					stockField.getText(), priceField.getText()))
				dispose();
		}
	};
	
	// When editing use this actionlistener.
	ActionListener confirmListenerEdit = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			PartController controller = new PartController(panel);
			if(controller.editPart(partID, nameField.getText()
					, descField.getText(),stockField.getText(), priceField.getText())) 
				dispose();
		}
	};
	
	/**
	 * Constructor for creating a Dialog for entering a new part.
	 */
	public NewPartDialog() {
		setTitle("Add Part");
		init();
		confirm.addActionListener(confirmListenerAdd);
		setVisible(true);
	}
	
	/**
	 * Constructor to use when editing an existing part.
	 * @param part the part to be edited.
	 */
	public NewPartDialog(Part part) {
		setTitle("Edit Part");
		init();
		nameField.setText(part.getName());
		descField.setText(part.getDesc());
		stockField.setText("" + part.getStock());
		partID = part.getId();
		priceField.setText("" + part.getPrice());
		confirm.addActionListener(confirmListenerEdit);
		setVisible(true);
	} 
	
	/**
	 * Set up UI and such
	 */
	private void init() {		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(3, 3, 3, 3);
		
		c.gridx = 0;
		c.gridy = 0;
		panel.add(new GMLabel("Name:"), c);
		
		c.gridx = 0;
		c.gridy = 1;
		panel.add(new GMLabel("Description:"), c);
		
		c.gridx = 0;
		c.gridy = 2;
		panel.add(new GMLabel("Stock:"), c);
		
		c.gridx = 0;
		c.gridy = 3;
		panel.add(new GMLabel("Price:"), c);
		
		c.weightx = 1;
		
		c.gridx = 1;
		c.gridy = 0;
		nameField = new JTextField();
		panel.add(nameField, c);
		
		c.gridx = 1;
		c.gridy = 1;
		descField = new JTextField();
		panel.add(descField, c);
		
		c.gridx = 1;
		c.gridy = 2;
		stockField = new JTextField();
		panel.add(stockField, c);
		
		c.gridx = 1;
		c.gridy = 3;
		priceField = new JTextField();
		panel.add(priceField, c);
		
		pack();
	}
}
