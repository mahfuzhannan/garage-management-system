/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemain.service;

import common.Database;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import schemain.Schemain;

/**
 * Service object that is used to add a Service to the database
 * @author Mahfuz
 */
public class Service extends Schemain{
    private Integer serviceId;
    private String nextService;
    private ServiceType serviceType;
    public enum ServiceType {
        TIME, MILEAGE;
    }
    
    public Service(Integer bookingID, Integer schemainID, Integer vehicleID, 
            Integer customerID, Integer mechanicID, Date startDate, Time startTime, 
            Date endDate, Time endTime, Integer bay, Double totalCost, 
            BookingType type, Double mileage, Boolean isPaid, SchemainType schemainType, 
            Integer serviceId, ServiceType serviceType, String nextService) {
        super(bookingID, schemainID, vehicleID, customerID, mechanicID, startDate, startTime, endDate, endTime, bay, 
                totalCost, type, mileage, isPaid, SchemainType.SERVICE);
        this.serviceId = serviceId;
        this.serviceType = serviceType;
        this.nextService = nextService;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getNextService() {
        return nextService;
    }

    public void setNextService(String nextService) {
        this.nextService = nextService;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }
    
}
