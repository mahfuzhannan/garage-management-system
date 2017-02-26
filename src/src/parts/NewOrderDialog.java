package parts;

import assets.GMComboBox;
import com.toedter.calendar.JDateChooser;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 * Dialog box for creating a new parts order.
 * @author George
 */
public class NewOrderDialog extends GMDialog {
	JComboBox supplierSelect;
	JTextField quantityField;
	JDateChooser orderedDateChooser, expectedDateChooser;
	int partID, orderID;
	Order o;
	
	// What happens when the confirm button is clicked. DIS DOES:
	ActionListener add = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			OrderController controller = new OrderController(panel);
			String dateOrdered = new SimpleDateFormat("yyyy-MM-dd").format(orderedDateChooser.getDate());
			String dateExpected = new SimpleDateFormat("yyyy-MM-dd").format(expectedDateChooser.getDate());
			if (controller.add(quantityField.getText(),
					(String) supplierSelect.getSelectedItem(),
					partID, dateOrdered, dateExpected))
				dispose();
		}
	};
	
	// And what happens when the confirm button is clicked but you are editing 
	// an order:
	ActionListener edit = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// Retrieve data from form.
			OrderController controller = new OrderController(panel);
			String dateOrdered = new SimpleDateFormat("yyyy-MM-dd").format(orderedDateChooser.getDate());
			String dateExpected = new SimpleDateFormat("yyyy-MM-dd").format(expectedDateChooser.getDate());
			// Let the controller validate and deal with the model. Close if 
			// succesful.
			if(controller.edit(o.getId(), quantityField.getText(), (String) supplierSelect.getSelectedItem(), o.getPartID(), dateOrdered, dateExpected))
				dispose();
		}
	};
	
	/**
	 * Constructor for when creating a new order.
	 * @param partID partid of part to be ordered.
	 */
	public NewOrderDialog(int partID) {
		this.partID = partID;
		setTitle("New Order");
		init();
		confirm.addActionListener(add);
		setVisible(true);
	}
	
	/**
	 * Constructor for when editing an existing order.
	 * @param order the order to be edited.
	 */
	public NewOrderDialog(Order order) {
		o = order;
		setTitle("Edit Order");
		init();
		quantityField.setText(""+ order.getQuantity());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			expectedDateChooser.setDate(sdf.parse(order.getDateExpected()));
			orderedDateChooser.setDate(sdf.parse(order.getDateOrdered()));
		} catch (Exception e) {}
		confirm.addActionListener(edit);
		setVisible(true);
	}
	
	private void init() {
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(3, 3, 3, 3);
		
		c.gridx = 0;
		c.gridy = 0;
		panel.add(new GMLabel("Supplier:"), c);
		
		c.gridx = 0;
		c.gridy = 1;
		panel.add(new GMLabel("Quantity:"), c);
		
		c.gridx = 0;
		c.gridy = 2;
		panel.add(new GMLabel("Order Date:"), c);
		
		c.gridx = 0;
		c.gridy = 3;
		panel.add(new GMLabel("Expected Date:"), c);
		
		c.weightx = 1;
		
		c.gridx = 1;
		c.gridy = 0;
		supplierSelect = new GMComboBox(SupplierModel.getInstance().getSupplierList());
		panel.add(supplierSelect);
		
		c.gridx = 1;
		c.gridy = 1;
		quantityField = new JTextField();
		panel.add(quantityField, c);
		
		c.gridx = 1;
		c.gridy = 2;
		orderedDateChooser = new JDateChooser(new java.util.Date());
		orderedDateChooser.setDateFormatString("yyyy-MM-dd");
		panel.add(orderedDateChooser, c);
		
		c.gridx = 1;
		c.gridy = 3;
		expectedDateChooser = new JDateChooser(new java.util.Date());
		expectedDateChooser.setDateFormatString("yyyy-MM-dd");
		panel.add(expectedDateChooser, c);
		
		pack();
		
	}
}
