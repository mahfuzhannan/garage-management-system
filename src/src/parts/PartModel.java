package parts;

import common.Database;
import common.GMTableModel;
import java.sql.SQLException;

/**
 * Model for part stock Table. Acts as a {@link TableModel} for the list of parts.
 * And also deals with database transactions. Is a singleton to prevent data
 * inconsistency
 * @author George
 */
public class PartModel extends GMTableModel {
	static PartModel instance;
	
	// Set up DB query and column headers.
	private static final String[] colNames = {"Part ID", "Name", "Description", "Stock", "Price"};
	private static final String table = "\"Parts Stock\"";
	private static final String query = "SELECT * FROM \"Parts Stock\"";
	
	private PartModel() {
		super(table, query, colNames);
	}
	
	/**
	 * Gets an instance of PartModel. PartModel is a singleton so this
	 * is the only valid method of getting a PartModel object.
	 * Creates an instance if one does not already exist.
	 * @return The single instance of part model.
	 */
	public static PartModel getInstance() {
		if(instance == null) {
			instance = new PartModel();
		}
		return instance;
	}
	
	/**
	 * Adds a new part to the database.
	 * @param part The part object from where to get the data.
	 */
	public void add(Part part) {
		// Build query string
		String query = "INSERT INTO \"Parts Stock\" VALUES(null, '"
				+ part.getName() + "', '"
				+ part.getDesc()+ "', "
				+ part.getStock() + ","
				+ part.getPrice()
				+ ")";
		
		// Insert into db
		try {
			Database.executeUpdate(query);
			super.changed();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * Update part in database
	 * @param part part object to retrieve data from
	 */
	public void update(Part part) {
		// Build query string
		String query = "UPDATE \"Parts Stock\" SET "
				+ "\"Part Name\" = '" + part.getName() + "', "
				+ "\"Part Description\" = '" + part.getDesc() + "', "
				+ "\"Stock\" = " + part.getStock() + ", "
				+ "\"Price\" = " + part.getPrice()
				+ " WHERE \"Part ID\" = " + part.getId();
		
		System.out.println(query);
		
		// update db
		try {
			Database.executeUpdate(query);
			changed();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * Remove the part with given id from database.
	 * @param id id of part to remove.
	 */
	public void remove(int id) {
		OrderModel.getInstance().removeAllWherePart(id);
		// Build query string
		String query = "DELETE FROM \"Parts Stock\" WHERE \"Part ID\" = " + id;
		
		// update db
		try {
			Database.executeUpdate(query);
			changed();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch(columnIndex) {
			case 1: return int.class;
			case 2: return String.class;
			case 3: return String.class;
			case 4: return int.class;
			default: return String.class;
		}
	}
}

