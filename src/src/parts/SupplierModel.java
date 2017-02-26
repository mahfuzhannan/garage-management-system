package parts;

import common.Database;
import common.GMTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import settings.NewUserFailedException;

/**
 * Supplier model. Acts as a {@link TableModel} for the list of suppliers.
 * And also deals with database transactions. Is a singleton to prevent data
 * inconsistency
 * @author George
 */
public class SupplierModel extends GMTableModel {
	private static SupplierModel instance;
	private static final String[] colNames = {"Name", "Telephone", "Current Orders"};
	private static final String table = "Supplier";
	private static final String query = "SELECT Supplier.\"Supplier Name\","
			+ " Supplier.Tel,"
			+ " COUNT(\"Parts Ordered\".\"Supplier ID\") AS num_of_orders"
			+ " FROM Supplier"
			+ " LEFT JOIN \"Parts Ordered\""
			+ " ON (Supplier.\"Supplier ID\" = \"Parts Ordered\".\"Supplier ID\")"
			+ " GROUP BY Supplier.\"Supplier ID\"";
	
	private SupplierModel() {
		super(table, query, colNames);		
	}
	
	/**
	 * Gets an instance of supplier model. Supplier model is a singleton so this
	 * is the only valid method of getting an SupplierModel object.
	 * Creates an instance if one does not already exist.
	 * @return the single instance of SupplierModel.
	 */
	public static SupplierModel getInstance() {
		if(instance == null) {
			instance = new SupplierModel();
		}
		return instance;
	}
	
	/**
	 * Add a new supplier to database.
	 * @param name name of supplier.
	 * @param tel telephone of supplier.
	 */
	public void add(String name, String tel) {
		// Build query string
		String query = "INSERT INTO Supplier VALUES (null, '" + name + "', '"
				+ tel + "')";
		
		// Insert into db
		try {
			Database.executeUpdate(query);
			changed();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			throw new NewUserFailedException("Database error: " + e.getMessage());
		}
	}
	
	/**
	 * Remove a supplier from database using supplier name.
	 * @param name the name of the supplier you wish to remove.
	 */
	public void remove(String name) {
		
		// Build query string
		String query = "DELETE FROM \"Parts Ordered\" "
				+ "WHERE \"Supplier ID\" IN "
				+ "(SELECT \"Supplier ID\" FROM Supplier "
				+ "WHERE \"Supplier Name\" = '" + name + "')";
		System.out.println(query);
		
		// update db
		try {
			Database.executeUpdate(query);
			OrderModel.getInstance().triggerChanged();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		
		
		// Build query string
		query = "DELETE FROM Supplier WHERE \"Supplier Name\" = '" + name + "'";
		
		// update db
		try {
			Database.executeUpdate(query);
			changed();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * Update a supplier in the database.
	 * @param oldName Original name of supplier.
	 * @param name new name
	 * @param tel new telephone number
	 */
	public void update(String oldName, String name, String tel) {
		// Build query string
		String query = "UPDATE Supplier SET \"Supplier Name\" = '" + name + "', Tel = '" + tel + "' WHERE \"Supplier Name\" = '" + oldName + "'";
		// update db
		try {
			Database.executeUpdate(query);
			changed();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * Get a string array of all the suppliers. This is used for the combo box
	 * for creating new orders.
	 * @return A array containing the names of all the suppliers.
	 */
	public String[] getSupplierList() {
		// Build query string
		String query = "SELECT \"Supplier Name\" FROM Supplier";
		// update db
		
		ArrayList<String> suppList = new ArrayList<>();
		try {
			ResultSet rs = Database.executeQuery(query);
			while(rs.next()) {
				suppList.add(rs.getString(1));
			}
			return suppList.toArray(new String[0]);
		} catch (SQLException e) {
			System.err.println(e.getMessage() + "THIS ONE");
			return new String[0];
		}
	}
	
	/**
	 * Get the id of a supplier from its name. Name is UNIQUE so maps 1:1.
	 * @param name the name of the supplier.
	 * @return the id of the supplier
	 */
	public int getSupplierID(String name) {
		// Build query string
		String query = "SELECT \"Supplier ID\" FROM Supplier WHERE \"Supplier Name\" = \"" + name + "\" ";
		// update db
		
		try {
			ResultSet rs = Database.executeQuery(query);
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return 1;
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch(columnIndex) {
			case 1: return String.class;
			case 2: return String.class;
			case 3: return Integer.class;
			default: return String.class;
		}
	}
}
