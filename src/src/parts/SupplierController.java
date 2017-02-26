package parts;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

/**
 * Controller for Suppliers. Does necessary validation before changing model.
 * @author George
 */
public class SupplierController {
	private JComponent parent;
	
	public SupplierController(JComponent parent) {
		this.parent = parent;
	}
	
	/**
	 * Tries to add the given data into database to create new supplier. 
	 * Performs validation on input.
	 * @param name name of supplier
	 * @param tel telephone of supplier
	 * @return true if successful false if there was a validation or db error.
	 */
	public boolean add(String name, String tel) {
		if (!validate(name, tel)) return false;
		try {
			SupplierModel.getInstance().add(name, tel);
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(parent, "Name must be unique");
			return false;
		}
	}
	
	/**
	 * Tries to edit a supplier already present in the database.
	 * @param id id of the supplier being updated.
	 * @param name new name of the suppplier
	 * @param tel new telephone number of the supplier.
	 * @return
	 */
	public boolean edit(String id, String name, String tel) {
		if (!validate(name, tel)) return false;
		try {
			SupplierModel.getInstance().update(id, name, tel);
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(parent, "Name must be unique");
			return false;
		}
	}
	
	private boolean validate(String name, String tel) {
		if (tel.equals("")) {
			JOptionPane.showMessageDialog(parent, "Please enter a phone number");
			return false;
		}
		
		if (name.equals("")) {
			JOptionPane.showMessageDialog(parent, "Please enter a name");
			return false;
		}
		
		if (tel.length() < 9||tel.length()>15||!tel.matches("[+]*[0-9]*")) {
			JOptionPane.showMessageDialog(parent, "Telephone number not a valid format");
			return false;
		}
		return true;
	}
}
