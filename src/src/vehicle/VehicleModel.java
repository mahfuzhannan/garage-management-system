
package vehicle;

import common.Database;
import common.GMTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import parts.PartsInstalledModel;
import schemain.SchemainModel;


public class VehicleModel extends GMTableModel{
    private static final String[] colNames= {"Vehicle ID", "Type","Reg", "Make", "Model","Engine Size","Fuel Type","Color","MOT Renewal","Customer","Warranty"};
	private static final String table= "\"Vehicle\"";
	private static String query= "SELECT \"Vehicle ID\",Type,Reg,Make,Model,\"Engine Size\",\"Fuel Type\",Color,\"Mot Renewal Date\",\"Customer AccountsCustomer ID\" || \":\" || (SELECT Tel FROM \"Customer Accounts\" WHERE \"Customer ID\"=\"Customer AccountsCustomer ID\"),CASE WHEN \"WarrantyWarranty ID\"=0 THEN \"NoWarranty\" ELSE \"WarrantyWarranty ID\" END FROM Vehicle";
        private static VehicleModel instance;
    public VehicleModel() {
            super(table,query,colNames);   
    }
    public static VehicleModel getInstance(boolean toDelete)
    {
        if(instance==null)
        {
                if(!toDelete)
                {
                    instance = new VehicleModel();
                }
                else
                {
                    query = "";
                    instance = new VehicleModel();
                }
        }
            return instance;
    }
    //add a new vehicle
    public boolean add(Vehicle vehicle)
    {
        //type,reg,make,model,engine,fuel,color,mot renewal date,customer accounts,warranty
        String addQuery = "INSERT INTO Vehicle "
                +"VALUES(null,'"+vehicle.getType()+"', '"+vehicle.getRegNumber()+"', '"+vehicle.getMake()+"', '"+ vehicle.getModel()
                +"', '"+vehicle.getEngineSize()+"', '"+vehicle.getFuel()+"', '"
                + vehicle.getColour()+"', '"+vehicle.getMotRenewalDate()+"', "+vehicle.getCustomerId()+", "+vehicle.getWarranty()
                +");";
        try 
        {
                Database.executeUpdate(addQuery);//add to database
		super.changed();//update table
		return true;
	} 
        catch (SQLException e) 
        {
            JOptionPane.showMessageDialog(null,e.getMessage());
            return false;
	}
        
    }
    
    //check if a registration number is in the database already
    public boolean isIn(String reg)
    {
       boolean isIn = false; 
       try
       {
            String query = "SELECT COUNT(*) FROM Vehicle WHERE Reg='"+reg+"' COLLATE NOCASE;";
            ResultSet rs = Database.executeQuery(query);
            rs.next();
            if(rs.getInt(1)==1)//already a vehicle with same reg
                isIn=true;  
       }
       catch(SQLException e)
       {
            JOptionPane.showMessageDialog(null,e.getMessage());   
       }
       return isIn;
       
    }
    //remove all vehicles associated with a customer
    public void removeFor(int customerID)
    {
        String removeQuery = "DELETE FROM Vehicle WHERE \"Customer AccountsCustomer ID\" = "+customerID;
        try
        {
            //get vehicle ids for that customer and remove parts installed and bookings before actual vehicles
            String query = "SELECT \"Vehicle ID\" FROM Vehicle WHERE \"Customer AccountsCustomer ID\" = "+customerID;
            ResultSet rs = Database.executeQuery(query);
            while(rs.next())
            {
                int id = rs.getInt("Vehicle ID");
                remove(id);
            }
            //remove vehicles now
            Database.executeUpdate(removeQuery);
        }
        catch (SQLException e)
        {}
    }
    //remove vehicle and parts/bookings associated with that vehicle
    public boolean remove(int vehicleID)
    {
        String removeQuery = "DELETE FROM Vehicle WHERE \"Vehicle ID\" = "+vehicleID;
        
        try 
        {
            Database.executeUpdate(removeQuery);//add to database
            //delete parts installed on that vehicle
            PartsInstalledModel partsModel = PartsInstalledModel.getInstance();
            partsModel.removeAllWhereVehicle(vehicleID);
            //delete bookings associated with that vehicle
            new SchemainModel().removeAll(vehicleID);
            changed();//update table
            return true;
	} 
        catch (SQLException e) 
        {
		JOptionPane.showMessageDialog(null,e.getMessage());
		return false;
	}
    }
    
    //update vehicle
    public boolean update(Vehicle vehicle){   
        //Assume reg,color and warranty are the only details that can be updated on a vehicle        
        String updateQuery = "UPDATE Vehicle SET "
                + "Reg = '"+vehicle.getRegNumber()+"', "
                + "Color = '" + vehicle.getColour() + "', "
                + "\"WarrantyWarranty ID\" = '" + vehicle.getWarranty()
                + "' WHERE \"Vehicle ID\" = " + vehicle.getId();
        try 
        {
            Database.executeUpdate(updateQuery);
            changed();
            return true;
	    } 
        catch (SQLException e) 
        {
            JOptionPane.showMessageDialog(null,e.getMessage());
            return false;
	    }
    }
    public int updateWarranty(Warranty warranty){   
        //Assume reg,color and warranty are the only details that can be updated on a vehicle    
        Address address = warranty.getAddress();
        String updateQuery = "UPDATE Warranty SET "
                + "\"Company Name\" = '"+warranty.getName()+"', "
                + "Expiry = '" + warranty.getExpiry() + "', "
                + "Line1 = '" + address.getLine1() + "', "
                + "City = '" + address.getCity() + "', "
                + "Postcode = '" + address.getPostcode()
                + "' WHERE \"Warranty ID\" = " + warranty.getId();
        try 
        {
            Database.executeUpdate(updateQuery);
            changed();
            return warranty.getId();
	} 
        catch (SQLException e) 
        {
            return 0;
	}
    }
    //return associated warranty with the parameter vehicle(return Warranty object)
    public Warranty getWarranty(int warrantyId)throws SQLException
    {
        String query = "SELECT * FROM Warranty WHERE \"Warranty ID\" = "+warrantyId;
        ResultSet rs = Database.executeQuery(query);
        rs.next();
        
        String company=rs.getString("Company Name");
        String expiry = rs.getString("Expiry");
        java.util.Date date;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            date = format.parse(expiry);
        }
        catch(ParseException e)
        {
            date = null;
        }
        String line1=rs.getString("Line1");
        String city=rs.getString("City");
        String postcode=rs.getString("Postcode");
        Address address = new Address(line1,city,postcode);
        return new Warranty(warrantyId,company,address,new java.sql.Date(date.getTime()));     
    }

    //return an array of all customer ids
    public int[] getCustomerIds()throws SQLException
    {
            String query = "SELECT \"Customer ID\" FROM \"Customer Accounts\"";
            ResultSet rs = Database.executeQuery("SELECT * FROM \"Customer Accounts\"");
            int count=getCount("\"Customer Accounts\"");
            int[] ids = new int[count];

            rs = Database.executeQuery(query);
            int i=0;
            while(rs.next())
            {
                ids[i]=rs.getInt("Customer ID");
                i++;
            }
            return ids;
       }
        
    //return an array of all the customer telephone numbers
    public long[] getCustomerTelephones() throws SQLException
    {
            String query = "SELECT Tel FROM \"Customer Accounts\"";
            ResultSet rs = Database.executeQuery("SELECT * FROM \"Customer Accounts\"");
            int count=getCount("\"Customer Accounts\"");

            long[] telephones = new long[count];

            rs = Database.executeQuery(query);
            int i=0;
            while(rs.next())
            {
                telephones[i]=rs.getLong("Tel");
                i++;
            }
            return telephones;
    }
    //return numbers of rows in a table
    public int getCount(String tableName)
    {
        try
        {
            ResultSet rs = Database.executeQuery("SELECT COUNT(*) FROM "+tableName);
            rs.next();
            return rs.getInt(1);
        }
        catch(SQLException e)
        {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
        return -1;
    }
    public String[] getCustomerNames(int type)throws SQLException
    {
        String query = "";
        String typeStr = "";
        int count=getCount("\"Customer Accounts\"");
        String[] names = new String[count];
        if(type==0)//first names
        {
            query = "SELECT \"First Names\" FROM \"Customer Accounts\"";
            typeStr = "First Names";
        }
        else//last names
        {
            query = "SELECT \"Last Name\" FROM \"Customer Accounts\"";
            typeStr = "Last Name";
        }
        ResultSet rs = Database.executeQuery(query);
            int i=0;
            while(rs.next())
            {
                names[i]=rs.getString(typeStr);
                i++;
            }
            return names;
    }
    //add warranty to database and return id of new entry
    public int addWarranty(Warranty warranty)
    {
        Address address = warranty.getAddress();
        String addQuery = "INSERT INTO Warranty "
                +"VALUES(null,'"+warranty.getName()+"', '"+warranty.getExpiry()+"', '"
                +address.getLine1()+"', '"+address.getCity()+"', '"+address.getPostcode()
                +"');";
        ResultSet rs;
        try
        {
            Database.executeUpdate(addQuery);
        }
        //go to last row which is the newly added address
        catch(SQLException e)
        {
            JOptionPane.showMessageDialog(null,"SQLException: "+e.getMessage()+"\nAdding warranty.");
        }
        int warrantyID=getCount("Warranty");
        return warrantyID;
    }
    public void changed()
    {
        super.changed();
        
    }

    public SimpleService getLastService(int vehId)throws SQLException,ParseException
    {
        String query = "SELECT * FROM Booking JOIN \"Scheduled Maintenance\" ON "
                + "\"Scheduled Maintenance\".\"BookingBooking ID\" = Booking.\"Booking ID\" "
                + "JOIN Service ON Service.\"Scheduled MaintenanceScheduled Maintenance ID\" = "
                + "\"Scheduled Maintenance\".\"Scheduled Maintenance ID\" "
                + "WHERE Booking.\"VehicleVehicle ID\" = '" + vehId+"' ORDER BY \"End date\" DESC ;";
                    ResultSet rs = Database.executeQuery(query);
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleService service = new SimpleService(rs.getDouble("Mileage"),df.parse(rs.getString("End date")),rs.getDouble("Total Cost"));
                    return service;
    }

}//end class
