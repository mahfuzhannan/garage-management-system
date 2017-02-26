/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package booking;

import java.sql.Date;
import java.sql.Time;

/**
 * Booking object to add to the Booking table
 * @author mafz
 */
public class Booking{
    
    private Integer bookingID, vehicleID, customerID, mechanicID;
    private Time startTime, endTime;
    private Date startDate, endDate;
    private Integer bay;
    private Double totalCost, mileage;
    private Boolean isPaid;
    private BookingType type;
    
    public enum BookingType {
        DIAGREP, SCHEMAIN;
    }

    public Booking(Integer bookingID, Integer vehicleID, Integer customerID, Integer mechanicID,
            Date startDate, Time startTime, Date endDate, Time endTime, Integer bay, 
            Double totalCost, BookingType type, Double mileage, Boolean isPaid) {
        this.bookingID = bookingID;
        this.customerID = customerID;
        this.vehicleID = vehicleID;
        this.mechanicID = mechanicID;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.bay = bay;
        this.totalCost = totalCost;
        this.type = type;
        this.mileage = mileage;
        this.isPaid = isPaid;
    }

    public Integer getBookingID() {
        return bookingID;
    }

    public void setBookingID(Integer bookingID) {
        this.bookingID = bookingID;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public Integer getVehicleID() {
        return vehicleID;
    }

    public void setMechanicID(Integer mechanicID) {
        this.mechanicID = mechanicID;
    }

    public Integer getMechanicID() {
        return mechanicID;
    }

    public void setVehicleID(Integer vehicleID) {
        this.vehicleID = vehicleID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }    

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public Integer getBay() {
        return bay;
    }

    public void setBay(Integer bay) {
        this.bay = bay;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public BookingType getType() {
        return type;
    }

    public void setType(BookingType type) {
        this.type = type;
    }

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }

    public Integer getIsPaid() {
        if(isPaid) return 1;
        return 0;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }
}
