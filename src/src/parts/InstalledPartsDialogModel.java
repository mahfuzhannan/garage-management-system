package parts;

import common.Database;
import common.GMTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The model for the installed parts page. Implemented poorly as 
 * I put it here as an insurance for if the DAR module was not completed.
 * @author George
 */
public class InstalledPartsDialogModel extends GMTableModel {
	
	private static InstalledPartsDialogModel instance;
	
	
	private static final String[] colNames = {"ID", "Part", "Vehicle Reg", "Customer Last Name", "Install Date", "Warranty Expiration"};
	private static final String table = "\"Parts Installed\"";
	private static final String query = "SELECT \"Parts Installed\".\"Parts Installed ID\","
			+ " \"Parts Stock\".\"Part Name\",Vehicle.Reg,"
			+ " \"Customer Accounts\".\"Last Name\", "
			+ " \"Parts Installed\".\"Date Installed\", "
			+ " Warranty.Expiry "
			+ " FROM \"Parts Installed\""
			+ " INNER JOIN \"Parts Stock\" ON \"Parts Installed\".\"Parts StockPart ID\" = \"Parts Stock\".\"Part ID\""
			+ " INNER JOIN Vehicle ON \"Parts Installed\".\"VehicleVehicle ID\" = Vehicle.\"Vehicle ID\""
			+ " INNER JOIN Warranty ON \"Parts Installed\".\"WarrantyWarranty ID\" = Warranty.\"Warranty ID\""
			+ " INNER JOIN \"Customer Accounts\" ON Vehicle.\"Customer AccountsCustomer ID\" = \"Customer Accounts\".\"Customer ID\"";
	
	private InstalledPartsDialogModel() {
		super(table, query, colNames);
	}
	
	/**
	 * Get an instance of InstalledPartsDialogModel. Implemented as
	 * Singleton to avoid data inconsistency.
	 * @return
	 */
	public static InstalledPartsDialogModel getInstance() {
		if(instance == null) {
			instance = new InstalledPartsDialogModel();
		}
		return instance;
	}
	
	/**
	 * Add a new Installed part to the database. Throws exceptions.
	 * Its awfully quick and dirty.
	 * @param date Date which the part was installed.
	 * @param vehicleId The vehicle the part is being installed to.
	 * @param partId The part which is being installed.
	 * @throws Exception
	 */
	public void add(String date, int vehicleId, int partId) throws Exception {
		
		// First create new warranty for the part. Expires a year after
		// installation.
		
		// Build query string
		String query = "INSERT INTO Warranty VALUES(null, '"
				+ "Warranties Inc.', "
				+ "date('" + date + "', '+1 year'), "
				+ "'Business Street', "
				+ "'London', "
				+ "'E1 111')";
		
		// Insert into db
		Database.executeUpdate(query);
		super.changed();
		
		int warrantyId = 0;
		
		// Get the id for the warranty just inserted back from the db.
		
		// Build query string
		query = "SELECT last_insert_rowid() FROM Warranty";
		
		// Query DB.
		ResultSet rs = Database.executeQuery(query);
		rs.next();
		warrantyId = rs.getInt(1);
		super.changed();
		
		// Now insert data into parts intalled table.
		
		// Build query string
		query = "INSERT INTO \"Parts Installed\" VALUES(null, '"
				+ date + "', "
				+ vehicleId + ", "
				+ warrantyId + ", "
				+ partId + ")";
		
		// Insert into db
		
		Database.executeUpdate(query);
		super.changed();
	}
	
	public void remove(int id) {
		// Build query string
		String query = "DELETE FROM \"Parts Installed\" WHERE \"Parts Installed ID\" = " + id;
		
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
