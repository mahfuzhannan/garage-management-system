
package diagrep;

import booking.Booking;
import java.sql.Date;
import java.sql.Time;


/**
 * @author vc300
 */
public class DiagRep extends Booking{
    
    
    //for new booking
    DiagRep(Integer vehicleID, Integer customerID, Integer mechanicID,
            Date startDate, Time startTime, Date endDate, Time endTime, Integer bay, Double mileage) {
        super(null, vehicleID, customerID, mechanicID,
            startDate, startTime, endDate, endTime, bay, 
            0.0, BookingType.DIAGREP, mileage, false);
    }
    
    //contsructor for easy deletion
    DiagRep(Integer bookingID) {
        super(null, null, null, null,
            null, null, null, null, null, 
            0.0, BookingType.DIAGREP, 0.0, false);
        super.setBookingID(bookingID);
    }
    
    //for loading
    DiagRep(Integer bookingID, Integer vehicleID, Integer customerID, Integer mechanicID,
            Date startDate, Time startTime, Date endDate, Time endTime, Integer bay, 
            Double totalCost, Double mileage, Boolean isPaid) {
        super(bookingID, vehicleID, customerID, mechanicID,
            startDate, startTime, endDate, endTime, bay, 
            totalCost, BookingType.DIAGREP, mileage, isPaid);
    }
    
   
    
    
}
