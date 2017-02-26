package parts;

import common.Database;
import java.sql.SQLException;

/**
 * Model for PartsInstalled. Only has ability to "cascade" delete.
 * @author George
 */
public class PartsInstalledModel {
	private static PartsInstalledModel instance;
	public static PartsInstalledModel getInstance() {
		if(instance == null) {
			instance = new PartsInstalledModel();
		}
		return instance;
	}
	
	private PartsInstalledModel() {
		//Empty private constructor.
	}
	
	public boolean removeAllWherePart(int partid) {
		// Build query string
		String query = "DELETE FROM \"Parts Installed\" WHERE \"Parts StockPart ID\" = " + partid;
		
		// update db
		try {
			Database.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
		
	}
	
	public boolean removeAllWhereVehicle(int vehicleid) {
		// Build query string
		String query = "DELETE FROM \"Parts Installed\" WHERE \"VehicleVehicle ID\" = " + vehicleid;
		
		// update db
		try {
			Database.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
		
	}
}
