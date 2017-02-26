/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diagrep;


import common.Database;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

/**
 * @author vc300
 */
class BookingController {
    
     
    private final BookingView view;
    private final BookingModel model;
    private DiagRep booking;
    
     BookingController(BookingView view, BookingModel model) {
        this.view = view;
        this.model = model;
         try {
                // set customerIdField with list of all customers retrieved from model
                view.getCustomerField().setModel(new DefaultComboBoxModel(model.getCustomers()));
                if(Database.getHolidays()==null) {
                    try {Database.setHolidays();
                    } catch (IOException ex) {
                        view.showMessage(true, "Holidays not loaded!");
                    }
                }
                model.setHolidays();
            } catch (SQLException ex) {
                view.showMessage(true,"D&R Booking Initialisation Error\n");
            }
            init();
    }
     
     
     //for update
     BookingController(BookingView view, DiagRepModel model, Integer bookingID, 
                        Date startDate, Time startTime, Date endDate, Time endTime) {
        this.view = view;
        this.model = model;
        if(Database.getHolidays()==null) {
            try {Database.setHolidays();
            } catch (IOException ex) {
                view.showMessage(true, "Holidays not loaded!");
            }
        }
        model.setHolidays();
            
        try {
            // fetch booking object from DB
            booking = model.getBooking(bookingID,startDate,startTime,endDate,endTime);
            // set Customer ID
            String[] custom = {model.getCustomerIDAndName(booking.getCustomerID())};
            view.getCustomerField().setModel(new DefaultComboBoxModel(custom));
            view.getCustomerField().setSelectedItem(model.getCustomerIDAndName(booking.getCustomerID())); 
            view.getCustomerField().setEnabled(false);
            // set vehicles
            view.getVehicleIdField().setModel(new DefaultComboBoxModel(model.getVehicles(booking.getCustomerID())));
            view.getVehicleIdField().setEnabled(false);
            //Set Start Date
            view.getStartDateField().setDate(booking.getStartDate());
            // Set End Date
            view.getEndDateField().setDate(booking.getEndDate());
            // Set End time
            String endTimeStr = booking.getEndTime().toString();
            view.getEndTimeField().setSelectedItem(endTimeStr.substring(0, endTimeStr.length()-3));
            // Set Bay
            Integer[] custom1 = {booking.getBay()};
            view.getBayField().setModel(new DefaultComboBoxModel(custom1));
            view.getBayField().setSelectedItem(custom1);
            // Set Mechanic
            view.setMechanic(model.getMechanic(booking.getBay()));
            // Set Total Cost
            view.getTotalCostField().setText(booking.getTotalCost().toString());
            // Set Mileage
            view.getMileageField().setText(booking.getMileage().toString());
        } catch (SQLException ex) {
            view.showMessage(true,"D&R Booking Update Error\n");
        }
        init();
    }
     
     //assign and initialise listeners for D&R booking
    private void init() {
        
        view.addCustomerListener(new BookingController.CustomerListener());
        view.addVehicleListener(new BookingController.VehicleListener());
        view.addStartDateListener(new BookingController.StartDateListener());
        view.addEndDateListener(new BookingController.EndDateListener());
        view.addEndTimeListener(new BookingController.EndTimeListener());
        view.addBayListener(new BookingController.BayListener());
        view.addMileageListener(new BookingController.MileageListener());
        if(view.isUpdate())
            {
                view.addAddListener(new BookingController.UpdateListener());
            }
        else
            {
                view.addAddListener(new BookingController.AddListener());
            }
    }
    
     ///////////////////////////////////////////////////////////
    ////////////////Action Listener Classes////////////////////
    ///////////////////////////////////////////////////////////
    
     private class AddListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            DiagRep booking = new DiagRep(view.getVehicleId(), view.getCustomerId(), view.getMechanicID(),
                view.getStartDate(), view.getStartTime(), view.getEndDate(), view.getEndTime(), view.getBay(), view.getMileage()); 
       
            try {
                model.add(booking);
                view.showMessage(false,"Diagnosis & Repair booking added! ");
                view.exit();
            } catch (SQLException ex) {
                view.showMessage(true,"Error: Service booking not added!\n");
            }
        }
        
    }
     
    
     
    private class UpdateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            booking = new DiagRep(booking.getBookingID(), view.getVehicleId(),
                view.getCustomerId(), view.getMechanicID(), view.getStartDate(), 
                view.getStartTime(), view.getEndDate(), view.getEndTime(), view.getBay(),
                view.getTotalCost(), view.getMileage(), false); 
       
            try {
                model.update(booking);
                view.showMessage(false,"Diagnosis & Repair booking updated!");
                view.exit();
            } catch (SQLException ex) {
                view.showMessage(true,"Error: Diagnosis & Repair booking not updated!\n");
            }
        }        
    }
    
    
    
    private class CustomerListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Integer customerId = view.getCustomerId();
            view.checkCustomerId(true);
            try {
                view.getVehicleIdField().setModel(new DefaultComboBoxModel(model.getVehicles(customerId)));
            } catch (SQLException ex) {
                view.showMessage(true,ex.getMessage());
            }
            setGroup1Visibility(true);
        }
    }
    
    private class VehicleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(view.getVehicleId()==0) {
                view.checkVehicleId(false);
                view.showMessage(true,"Error: "+view.getCustomerId()+" has no vehicles, please add a vehicle to continue");
                view.exit();
            }
            else
                view.checkVehicleId(true);
        }
    }
    
    
    private class StartDateListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            try {
                Date date = view.getStartDate();
                int day = date.getDay();
                if(day==0 |model.isHoliday(date)) {
                    view.setStartTime(new String[]{""});
                    setGroup2Visibility(false);
                }
                else {
                    
                    if(day==6) {
                        view.setStartTime(model.getWeekendTimes());
                    }
                    else {
                        view.setStartTime(model.getWeekdayTimes());
                    }
                    setGroup2Visibility(true);
                    
                    view.revalidate();
                }
            } catch (NullPointerException ex) {
            }
        }
    }
    
    
    
    
    private class EndDateListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            try {
                Date date = view.getEndDate();
                int day = date.getDay();
                if(day==0||model.isHoliday(date)) {
                    view.setEndTime(new String[]{""});
                    setGroup3Visibility(false);
                }
                else {
                    if(day==6) {
                        view.setEndTime(model.getWeekendTimes());
                    }
                    else {
                        view.setEndTime(model.getWeekdayTimes());
                    }
                    setGroup3Visibility(true);
                    
                    view.revalidate();
                }
            } catch (NullPointerException ex) {
            }
        }
    }
    
    
    
    
    
    private class EndTimeListener implements ActionListener {


        @Override
        public void actionPerformed(ActionEvent e) {
            
                              setBays();
                              setGroup4Visibility(true);
        }
    }
    
    
    
     
     
    private class BayListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(view.getBay()!=0 || view.getBay() != null) {
                view.checkBay(true);
                try {
                    view.setMechanic(model.getMechanic(view.getBay()));
                    view.checkMechanic(true);
        view.getAdd().setEnabled(true);
                } catch (SQLException ex) {
                    view.showMessage(true,"No Mechanic Avaliable!");
                    view.checkMechanic(false);
                }
            }
            else {
                view.checkBay(false);
                view.checkMechanic(false);
            }
        }
    }
    
    private class MileageListener implements KeyListener {

        @Override
        public void keyReleased(KeyEvent e) {
            try {
                if(view.getMileage()!= 0)
                    view.checkMileage(true);
                else
                    view.checkMileage(false);
            } catch(Exception ex) {
                view.checkMileage(false);
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }
               
    }
    
    
    
    
    
     ///////////////////////////////////////////////////////////
    ////////////////GUI Guidance Methods////////////////////
    ///////////////////////////////////////////////////////////
    
    
    //after vehicle selected, make other fields accessible
    private void setGroup1Visibility(boolean val) {
        view.getVehicleIdField().setEnabled(val);
        view.checkVehicleId(val);
        view.getStartDateField().setEnabled(val);
        view.getEndDateField().setEnabled(val);
    }
    
    //after Date/Time selected, make other fields accessible
    private void setGroup2Visibility(boolean val) {
        view.getStartTimeField().setEnabled(val);
        view.getEndTimeField().setEnabled(val);
        view.checkStartTime(val);
        view.checkEndTime(val);
    }
    
    //After all dates/times are chosen
    private void setGroup3Visibility(boolean val) {
        view.getBayField().setEnabled(val);
        view.getMileageField().setEnabled(val);
    }
    
    //after Bay is chosen
    private void setGroup4Visibility(boolean val) {
        view.getMechanicField().setEnabled(val);
        view.getMileageField().setEnabled(val);
    }
   
    
    private void setBays() {
        try {
            if(view.getStartDate()!=null && view.getEndDate()!=null && view.getStartTime()!=null && view.getEndTime()!=null) {    
                Integer[] availableBays = model.getAvailableBays(view.getStartDate(), view.getStartTime(), view.getEndDate(), view.getEndTime());
                view.getBayField().setModel(new DefaultComboBoxModel(availableBays));
                //view.setMechanic(model.getMechanic(availableBays[0]));
        }
        } catch (SQLException ex) {
            view.showMessage(true,ex.getMessage());
        }
    }
    
    
}
