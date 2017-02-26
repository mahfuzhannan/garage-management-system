package parts;

import common.Database;
import common.GMTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Model for Orders Table. Acts as a {@link TableModel} for the list of orders.
 * And also deals with database transactions. Is a singleton to prevent data
 * inconsistency
 * @author George
 */
public class OrderModel extends GMTableModel {
	static OrderModel instance;
	
	// Set up DB query and column headers.
	private static final String[] colNames = {"ID", "Part", "#", "Supplier", "Due"};
	private static final String table = "\"Parts Ordered\"";
	private static final String query = "SELECT \"Parts Ordered\".\"Order ID\","
			+ " \"Parts Stock\".\"Part Name\",\"Parts Ordered\".Quantity,"
			+ " Supplier.\"Supplier Name\", \"Parts Ordered\".\"Date Expected\""
			+ "  FROM \"Parts Ordered\", Supplier, \"Parts Stock\""
			+ " WHERE \"Parts Stock\".\"Part ID\" = \"Parts Ordered\".\"Part ID\""
			+ " AND Supplier.\"Supplier ID\" = \"Parts Ordered\".\"Supplier ID\"";
	
	private OrderModel() {
		super(table, query, colNames);
	}
	
	/**
	 * Gets an instance of OrderModel. OrderModel is a singleton so this
	 * is the only valid method of getting an OrderModel object.
	 * Creates an instance if one does not already exist.
	 * @return The single instance of order model.
	 */
	public static OrderModel getInstance() {
		if(instance == null) {
			instance = new OrderModel();
		}
		return instance;
	}
	
	/**
	 * Add a new order to the database. 
	 * @param order The order to be added to the DB
	 */
	public void add(Order order) {
		// Build query string
		String query = "INSERT INTO \"Parts Ordered\" VALUES(null, '"
				+ order.getQuantity() + "', '"
				+ order.getDateOrdered() + "', '"
				+ order.getDateExpected() + "', "
				+ order.getSupplierID() + ", "
				+ order.getPartID()
				+ ")";
		
		// Insert into db
		try {
			Database.executeUpdate(query);
			super.changed();
			SupplierModel.getInstance().triggerChanged();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * Updates a record in the database with data from an {@link Order} object.
	 * @param order Contains the data that will overwrite that database data.
	 */
	public void update(Order order) {
		// Build query string
		String query = "UPDATE \"Parts Ordered\" SET "
				+ "Quantity = " + order.getQuantity() + ", "
				+ "\"Date Ordered\" = '" + order.getDateOrdered() + "', "
				+ "\"Date Expected\" = '" + order.getDateExpected() + "', "
				+ "\"Supplier ID\" = " + order.getSupplierID()
				+ " WHERE \"Order ID\" = " + order.getId();
		
		// Update DB
		try {
			Database.executeUpdate(query);
			// Trigger changed to update any Tables.
			// Trigger changed in supplier as suppliers may have changed number
			// of orders.
			super.changed();
			SupplierModel.getInstance().triggerChanged();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * Delete a record from the database. 
	 * @param id id of order to be deleted.
	 */
	public void remove(int id) {
		// Build query string
		String query = "DELETE FROM \"Parts Ordered\" WHERE \"Order ID\" = " + id;
		
		// update db
		try {
			Database.executeUpdate(query);
			changed();
			SupplierModel.getInstance().triggerChanged();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * Fulfills an order. That is deletes order from order table
	 * and updates stock count of the item that was ordered.
	 * @param order order to be fulfilled.
	 */
	public void fulfill(Order order) {
		// Delete order from database
		// Build query string
		System.out.println(order.getId());
		String query = "DELETE FROM \"Parts Ordered\" WHERE \"Order ID\" = " + order.getId();
		
		// update db
		try {
			Database.executeUpdate(query);
			changed();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		
		// Update parts with quantity
		// Build query string
		query = "UPDATE \"Parts Stock\" SET "
				+ "\"Stock\" = \"Stock\" + " + order.getQuantity()
				+ " WHERE \"Part ID\" = " + order.getPartID();
		
		// update db
		try {
			Database.executeUpdate(query);
			PartModel.getInstance().triggerChanged();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * Get an {@link Order} object populated with data from database.
	 * @param id id of the order you want to populate the object with
	 * @return Order object.
	 */
	public Order getOrder(int id) {
		// Build query string
		String query = "SELECT * FROM \"Parts Ordered\" WHERE \"Order ID\" =  " + id;
		
		// Insert into db
		try {
			ResultSet rs = Database.executeQuery(query);
			rs.next();
			int quantity = rs.getInt(2);
			int supplierID = rs.getInt(5);
			int partID = rs.getInt(6);
			String dateOrdered = rs.getString(3);
			String dateExpected = rs.getString(4);
			return new Order(id, quantity, supplierID, partID, dateOrdered, dateExpected);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
	
	/**
	 * Remove all Orders which have a given partid. This is to emulate 
	 * Database cascade delete. Dont know why we didnt use that.
	 * @param partid id of part which has been deleted and references of should be removed.
	 */
	public void removeAllWherePart(int partid) {
		// Build query string
		String query = "DELETE FROM \"Parts Ordered\" WHERE \"Part ID\" = " + partid;
		
		// update db
		try {
			Database.executeUpdate(query);
			changed();
			SupplierModel.getInstance().triggerChanged();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * Remove all Orders which have a given supplierid. This is to emulate 
	 * Database cascade delete.
	 * @param suppid id of supplier which has been deleted and references of should be removed.
	 */
	public void removeAllWhereSupplier(int suppid) {
		// Build query string
		String query = "DELETE FROM \"Parts Ordered\" WHERE \"Supplier ID\" = " + suppid;
		
		// update db
		try {
			Database.executeUpdate(query);
			changed();
			SupplierModel.getInstance().triggerChanged();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}
}
