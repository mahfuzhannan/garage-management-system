/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemain.mot;

import booking.Booking;
import java.sql.Date;
import java.sql.Time;
import schemain.Schemain;

/**
 * Mot object that is used to add an Mot to the database
 * @author Mahfuz
 */
public class Mot extends Schemain {
    private Integer motId;
    private Boolean pass;
    private String reason;
    
    public Mot(Integer bookingID, Integer schemainID, Integer vehicleID, 
            Integer customerID, Integer mechanicID, Date startDate, Time startTime, 
            Date endDate, Time endTime, Integer bay, Double totalCost, 
            Booking.BookingType type, Double mileage, Boolean isPaid, Schemain.SchemainType schemainType, 
            Integer motId, Boolean pass, String reason) {
        super(bookingID, schemainID, vehicleID, customerID, mechanicID, startDate, startTime, endDate, endTime, bay, 
                totalCost, type, mileage, isPaid, SchemainType.MOT);
        this.motId = motId;
        this.pass = pass;
        this.reason = reason;
    }

    public Integer getMotId() {
        return motId;
    }

    public void setMotId(Integer motId) {
        this.motId = motId;
    }

    public Boolean getPass() {
        return pass;
    }

    public void setPass(Boolean pass) {
        this.pass = pass;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
