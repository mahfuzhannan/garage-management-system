/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemain;

import booking.Booking;
import java.sql.Date;
import java.sql.Time;

/**
 * Scheduled Maintenance object to add to Scheduled Maintenance table
 * @author Mahfuz
 */
public abstract class Schemain extends Booking{
    private Integer schemainId;
    private SchemainType schemainType;
    
    public enum SchemainType {
        SERVICE, MOT;
    }

    public Schemain(Integer bookingID, Integer schemainID, Integer vehicleID, 
            Integer customerID, Integer mechanicID, Date startDate, Time startTime, 
            Date endDate, Time endTime, Integer bay, Double totalCost, 
            BookingType type, Double mileage, Boolean isPaid, SchemainType schemainType) {
        super(bookingID, vehicleID, customerID, mechanicID, startDate, startTime, endDate, endTime, bay, totalCost, 
                BookingType.SCHEMAIN, mileage, isPaid);
        this.schemainId = schemainID;
        this.schemainType = schemainType;
    }
    
    /**
     * Returns Scheduled Maintenance ID, used as primary key in Scheduled Maintenance Table
     * @return 
     */
    public Integer getSchemainId() {
        return schemainId;
    }
    
    /**
     * Set Scheduled Maintenance ID, used as primary key in Scheduled Maintenance Table.
     * @param schemainId 
     */
    public void setSchemainId(Integer schemainId) {
        this.schemainId = schemainId;
    }

    /**
     * Returns Scheduled Maintenance Type.
     * @return - either SchemainType.SERVICE or SchemainType.MOT
     */
    public SchemainType getSchemainType() {
        return schemainType;
    }

    /**
     * Set Scheduled Maintenance Type.
     * @param schemainType - either SchemainType.SERVICE or SchemainType.MOT
     */
    public void setSchemainType(SchemainType schemainType) {
        this.schemainType = schemainType;
    }
    
}
