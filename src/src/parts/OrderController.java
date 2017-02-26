package parts;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

/**
 * Controller for orders. Does necessary validation before changing model.
 * @author George
 */
public class OrderController {
	private JComponent parent;
	
	public OrderController(JComponent parenr) {
		this.parent = parent;
	}
	
	/**
	 * Tries to add the given data into database. Performs validation on input.
	 * @param quantity quantity of parts to be ordered.
	 * @param supplierName name of supplier to order from
	 * @param partID id of part that is being ordered.
	 * @param dateOrdered date the order was made.
	 * @param dateExpected date the order is expected.
	 * @return returns true if successfully inserted. False if there was
	 * a validation error or DB exception.
	 */
	public boolean add(String quantity, String supplierName, int partID, String dateOrdered, String dateExpected) {
		if (!validate(quantity)) return false;
		int quantityInt = Integer.parseInt(quantity);
		int supplierID = SupplierModel.getInstance().getSupplierID(supplierName);
		OrderModel.getInstance().add(new Order(quantityInt, supplierID, partID, dateOrdered, dateExpected));
		return true;
	}
	
	/**
	 * Tries to edit an order in the database. Performs validation on input.
	 * @param orderID the id of the order to be edited
	 * @param quantity quantity of parts to be ordered.
	 * @param supplierName name of supplier to order from
	 * @param partID id of part that is being ordered.
	 * @param dateOrdered date the order was made.
	 * @param dateExpected date the order is expected.
	 * @return returns true if successfully updated. False if there was
	 * a validation error or DB exception.
	 */
	public boolean edit(int orderID, String quantity, String supplierName, int partID, String dateOrdered, String dateExpected) {
		if (!validate(quantity)) return false;
		int quantityInt = Integer.parseInt(quantity);
		int supplierID = SupplierModel.getInstance().getSupplierID(supplierName);
		OrderModel.getInstance().update(new Order(orderID, quantityInt, supplierID, partID, dateOrdered, dateExpected));
		return true;
	}
	/**
	 * Validate given input.
	*/
	private boolean validate(String quantity) {
		if (quantity.equals("")) {
			JOptionPane.showMessageDialog(parent, "Please enter a quantity");
			return false;
		}
		int quantityInt;
		try {
			quantityInt = Integer.parseInt(quantity);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(parent, "Please enter a valid number quantity");
			return false;
		}
		
		if (quantityInt < 0) {
			JOptionPane.showMessageDialog(parent, "Quantity must be larger than 0");
			return false;
		}
		
		return true;
	}
}
