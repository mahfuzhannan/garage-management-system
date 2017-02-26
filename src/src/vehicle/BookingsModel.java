//Author: Stefan Ionascu
//Module: Vehicle Records
//Date: 27/03/2014

//BookingsModel sets the columns for the bookings table and updates the table with the 
//associated bookings, for a vehicle id
package vehicle;

import common.GMTableModel;
import javax.swing.JOptionPane;

public class BookingsModel extends GMTableModel
{
                private static final String[] colNames= {"Booking ID", "Start Date","End date", "Total cost", "Mileage"};
		private static String query = "";
		private static final String table= "\"Booking\"";
		private static String countQuery = "";
		private static BookingsModel instance;
		private int vehId;

		public BookingsModel()
		{
                    super(table,query,colNames);
                }
		public static BookingsModel getInstance()
		{
			if(instance==null)
			{
				instance = new BookingsModel();
			}
			return instance;
		}
               
		public void setId(int id)
		{
			vehId = id;
			query = "SELECT \"Booking ID\",\"Start Date\",\"End Date\",\"Total Cost\",Mileage FROM \"Booking\" WHERE \"VehicleVehicle ID\"="+vehId; 
			countQuery = "SELECT COUNT(*) FROM \"Booking\" WHERE \"VehicleVehicle ID\"="+vehId;
                        super.setQuery(query,countQuery); 
			super.changed();
		}	
}
