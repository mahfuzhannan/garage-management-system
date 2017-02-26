package parts;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

/**
 * Controller for parts. Does necessary validation before changing part model.
 * @author George
 */
public class PartController {
	private JComponent parent;

	/**
	 * Constructor to make a new Controller. Must give a parent as validation 
	 * errors are displayed using a dialog box.
	 * @param parent The "parent" component of the object.
	 */
	public PartController(JComponent parent) {
		this.parent = parent;
	}

	/**
	 * Try to add a a new part into the database. Also performs validation on
	 * input.
	 * @param name name of the part to be inserted.
	 * @param desc a short description of the part.
	 * @param stock how much initial stock the part has.
	 * @param price the real world monetary price of the part.
	 * @return true if successful. False on validation or db error.
	 */
	public boolean newPart(String name, String desc, String stock, String price) {
		// Check for formatting errors.
		if (!validate(name, desc, stock, price)) return false;
		
		// Create the new part object
		Part part = new Part(name, desc, Integer.parseInt(stock),Float.parseFloat(price));
		
		// Send to model
		PartModel.getInstance().add(part);
		return true;
	}
	
	/**
	 * Try to update a a  part in the database. Also performs validation on
	 * input.
	 * @param id the id of the part being changed.
	 * @param name new name of the part
	 * @param desc a short description of the part.
	 * @param stock how much initial stock the part has.
	 * @param price the real world monetary price of the part.
	 * @return true if successful. False on validation or db error.
	 */
	public boolean editPart(int id, String name, String desc, String stock, String price) {
		// Check for formatting errors.
		if (!validate(name, desc, stock, price)) return false;
		// Create the new part object
		Part part = new Part(id, name, desc, Integer.parseInt(stock), Float.parseFloat(price));
		
		// Send to model
		PartModel.getInstance().update(part);
		return true;
	}
	
	/**
	 * Validates input to avoid user error. On error displays a  dialog telling
	 * the user what went wrong.
	 * @param name 
	 * @param desc
	 * @param stock
	 * @param price
	 * @return whether the validation passed or not.
	 */
	private boolean validate(String name, String desc, String stock, String price) {
		if (name.equals("")) {
			JOptionPane.showMessageDialog(parent, "Please enter a name");
			return false;
		}
		
		if (desc.equals("")) {
			JOptionPane.showMessageDialog(parent, "Please enter a description");
			return false;
		}
		
		if (price.equals("")) {
			JOptionPane.showMessageDialog(parent, "Please enter a price");
			return false;
		}
		int stocki = 0;
		try {
			Integer.parseInt(stock);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(parent, "Please enter valid stock number");
			return false;
		}
		
		float stockf = 0f;
		try {
			Float.parseFloat(price);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(parent, "Please enter valid price");
			return false;
		}
		return true;
	}
}
