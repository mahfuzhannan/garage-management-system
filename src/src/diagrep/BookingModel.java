
package diagrep;

import common.Database;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;


/**
 * @author vc300
 */
class BookingModel extends booking.BookingModel{
    
    BookingModel() {
        super();
    }
    
   
    
     ///////////////////////////////////////////////////////////
    /////////////////////Getters///////////////////////////////
    ///////////////////////////////////////////////////////////
    //get booking form DB
    DiagRep getBooking(Integer bookingID, Date startDate, Time startTime, Date endDate, Time endTime) throws SQLException {
        DiagRep booking;
        String query = "SELECT * FROM Booking WHERE \"Booking ID\" = "+ bookingID+";";
      
        try (ResultSet rs = Database.executeQuery(query)) {
 
            booking = new DiagRep(rs.getInt("Booking ID"),rs.getInt("VehicleVehicle ID"), rs.getInt("Customer AccountsCustomer ID"), rs.getInt("MechanicMechanic ID"),
                startDate, startTime, endDate, endTime, rs.getInt("Bay"), rs.getDouble("Total Cost"), rs.getDouble("Mileage"), false);

        }
        return booking;
    }
    
    
    //overrides the inherited method and adds new limitations to the availability of bays
    //as repairs are of variable time and further checks must be done to produce a list of available bays.
        //produces available bays when common logic of booking times is applied
    @Override
    public Integer[] getAvailableBays(Date startDate, Time startTime, Date endDate, Time endTime) throws SQLException {
        Integer[] superBays = super.getAvailableBays(startDate, startTime, endDate, endTime);
        
        ArrayList<Integer> bays = new ArrayList<>();
        for(int i=0; i<superBays.length; i++)
            bays.add(superBays[i]);
        
        String query = "SELECT bay FROM Booking WHERE "
                
                //  *******s*****d***********************e*********
                +"((\"Start Date\" > '"+startDate+"') AND (\"End Date\" < '"+endDate+"'))OR "
                //  *******s*****d********              e
                +"((\"Start Date\" < '"+startDate+"') AND (\"End Date\" < '"+endDate+"')AND (\"End Date\" > '"+startDate+"')) OR"
                //          s      d********************e*********
                +"((\"Start Date\" > '"+startDate+"') AND (\"End Date\" > '"+endDate+"')AND (\"Start Date\" < '"+startDate+"'))OR"
                +"((\"Start Date\" > '"+startDate+"') AND (\"End Date\" < '"+endDate+"')) OR"
                 +"((\"Start Date\" = '"+startDate+"') AND (\"End Date\" > '"+startDate+"') AND"
                        + "(\"Start Time\" <= '"+startTime+"'))OR "
                 +"((\"Start Date\" = '"+startDate+"') AND (\"End Date\" = '"+startDate+"') AND (\"End Date\" != '"+endDate+"') AND"
                        + "(\"Start Time\" >= '"+startTime+"'))OR "
                //          s*st**d        e
                //          s   **st**e**     d   
                +"((\"Start Date\" = '"+startDate+"') AND (\"End Date\" = '"+startDate+"') AND"
                    + "(\"End Time\" > '"+endTime+"') AND (\"Start Time\" < '"+startTime+"')) OR"
                //          s   st**e**     d   
                +"((\"Start Date\" = '"+startDate+"') AND (\"End Date\" = '"+startDate+"') AND (\"End Date\" = '"+endDate+"') AND"
                    + "(\"Start Time\" > '"+startTime+"') AND (\"End Time\" > '"+endTime+"')AND (\"Start Time\" < '"+endTime+"'))OR"
                //          s   **st**e    d   
                +"((\"Start Date\" = '"+startDate+"') AND (\"End Date\" = '"+startDate+"') AND (\"End Date\" = '"+endDate+"') AND"
                    + "(\"End Time\" > '"+startTime+"') AND (\"Start Time\" < '"+startTime+"')) OR"
                //          s   st**e    d   
                +"((\"Start Date\" = '"+startDate+"') AND (\"End Date\" = '"+startDate+"') AND (\"End Date\" = '"+endDate+"') AND"
                    + "(\"End Time\" < '"+endTime+"') AND (\"Start Time\" > '"+startTime+"')) OR"
                 //         
                +"((\"Start Date\" = '"+endDate+"') AND"
                    + "(\"End Time\" > '"+startTime+"'));";
                
               

        ResultSet rs = Database.executeQuery(query);
        
        while(rs.next()) { 
            Integer bay = rs.getInt(1);
            if(bays.contains(bay))
                bays.remove(bay);
        }
        
        return bays.toArray(new Integer[bays.size()]);
    }
    
    
    
    
    
     ///////////////////////////////////////////////////////////
    /////////////////////Setters///////////////////////////////
    ///////////////////////////////////////////////////////////
    
    
     
    //add Booking to DB
    void add(DiagRep booking) throws SQLException {
       double totalCost =0.0;
            
        String addQuery = "INSERT INTO Booking ("
                + "\"Start Date\", "
                + "\"Start Time\","
                + "\"End Date\", "
                + "\"End Time\", "
                + "Bay, "
                + "\"Total Cost\", "
                + "Type, "
                + "Mileage, "
                + "isPaid, "
                + "\"MechanicMechanic ID\", "
                + "\"VehicleVehicle ID\", "
                + "\"Customer AccountsCustomer ID\") VALUES "
            + "('"+booking.getStartDate()+"','"+booking.getStartTime()+"','"
            +booking.getEndDate()+"','"+booking.getEndTime()+"','"+booking.getBay()+"','"
            + totalCost + "','"+booking.getType()+"','"+booking.getMileage()+"','"+booking.getIsPaid()+"','"
            +booking.getMechanicID()+"','"+booking.getVehicleID()+"','"+booking.getCustomerID()+"');";
        Database.executeUpdate(addQuery);
        try (ResultSet rs1 = Database.executeQuery("SELECT \"Booking ID\" FROM Booking ORDER BY \"Booking ID\" DESC LIMIT 1;")) {
            booking.setTotalCost(totalCost);
            booking.setBookingID(rs1.getInt("Booking ID"));
        }
    
    }
    
    
    
    void insertPart(String dateInstalled, String vehicleID,int partID) throws SQLException {
        
                String query1 = "INSERT INTO Warranty VALUES(null, 'Warranties Inc.', date('" + dateInstalled + "', '+1 year'), 'Garage Street','London', 'N1 5dw')";
        
		Database.executeUpdate(query1);
		query1 = "SELECT last_insert_rowid() FROM Warranty";
		ResultSet rs = Database.executeQuery(query1);
		rs.next();
		int warrantyID = rs.getInt(1);
                String query2 = "INSERT INTO \"Parts Installed\" VALUES(null, '" + dateInstalled + 
                        "', (SELECT \"Vehicle ID\" FROM \"Vehicle\" where \"Reg\"='"+ vehicleID + "'), "+ warrantyID + ", "+ partID + ")";

		Database.executeUpdate(query2);   
                
                String query3 = "UPDATE \"Parts Stock\" SET Stock=(Stock -1) WHERE \"Part ID\"="+partID+";";

		Database.executeUpdate(query3);   
                
                String query4 = "UPDATE \"Booking\" SET \"Total Cost\"= \"Total Cost\" + (SELECT \"Price\" From \"Parts Stock\" WHERE \"Part ID\"="+partID+");";

		Database.executeUpdate(query4);  
    }

    void addCost(int duration, Integer bookingID) throws SQLException {
         String query3 = "UPDATE \"Booking\" SET \"Total Cost\"=(\"Total Cost\" + ("+duration+" * (SELECT Mechanic.\"Hourly Rate\" FROM Mechanic WHERE \"Mechanic ID\" = (SELECT \"Mechanic ID\" FROM Booking WHERE \"Booking ID\"="+bookingID+")))) WHERE \"Booking ID\"="+bookingID+";";

		Database.executeUpdate(query3);   
    }
    
}
