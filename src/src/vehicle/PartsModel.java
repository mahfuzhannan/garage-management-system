//Author: Stefan Ionascu
//Module: Vehicle Records
//Date: 27/03/2014

//PartsModel sets the columns for the parts installed table and updates the table with the 
//associated parts installed, for a vehicle id
package vehicle;

import common.GMTableModel;
import javax.swing.JOptionPane;

class PartsModel extends GMTableModel
{
	    private static final String[] colNames= {"Part ID", "Date installed","Vehicle", "Warranty", "Stock"};
		private static String query = "";
		private static final String table= "\"Parts Installed\"";
		private static String countQuery = "";
		private static PartsModel instance;
		private int vehId;

		public PartsModel()
		{
                    super(table,query,colNames);
                }
		public static PartsModel getInstance()
		{
			if(instance==null)
			{
				instance = new PartsModel();
			}
			return instance;
		}
		public void setId(int id)
		{
			vehId = id;
			query = "SELECT \"Parts Installed ID\",\"Date Installed\",\"VehicleVehicle ID\" || \":\" || (SELECT Reg FROM Vehicle WHERE \"Vehicle ID\"="+vehId+"),\"WarrantyWarranty ID\",\"Parts StockPart ID\" || \":\" || (SELECT \"Part Name\" FROM \"Parts Stock\" WHERE \"Parts StockPart ID\"=\"Part ID\") FROM \"Parts Installed\" WHERE \"VehicleVehicle ID\"="+vehId; 
			countQuery = "SELECT COUNT(*) FROM \"Parts Installed\" WHERE \"VehicleVehicle ID\"="+vehId;
                        super.setQuery(query,countQuery); 
			super.changed();
		}
}
