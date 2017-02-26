/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemain.mot;

import Exceptions.NoCustomerException;
import Exceptions.NoVehicleException;
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
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import schemain.Schemain;

/**
 *
 * @author Mahfuz
 */
public class MotController {
    private final MotModel model;
    private final MotView view;
    private Schemain mot;

    public MotController(MotModel model, MotView view) {
        this.model = model;
        this.view = view;
        init();
        if(Database.getHolidays()==null) {
            try {
                Database.setHolidays();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Holidays not loaded!");
            }
        }
        model.setHolidays();
    }
    
    private void init() {
        // change action listener if its an update
        if(view.isUpdate())
            view.addAddBtnListener(new UpdateBtnListener());
        else
            view.addAddBtnListener(new AddBtnListener());
        view.addCancelBtnListener(new CancelBtnListener());
        view.addCustomerIdListener(new CustomerIdListener());
        view.addVehicleIdListener(new VehicleIdListener());
        view.addStartDateListener(new StartDateListener());
        view.addStartTimeListener(new StartTimeListener());
        view.addBayListener(new BayListener());
        view.addPassListener(new PassListener());
        view.addMileageListener(new MileageListener());
        setCustomers();
    }
    
    /**
     * Loads all customers in the database;
     * throws a NoCustomerException if no customers are registered.
     */
    private void setCustomers() {
        try {
            // set customerIdField with list of all customers retrieved from model
            view.getCustomerIdField().setModel(new DefaultComboBoxModel(model.getCustomers()));
            // if customerId==0 no customers exist
            if(view.getCustomerId()!=0) {
                // set selected field to first customer in list
                view.getCustomerIdField().setSelectedIndex(0);
                // set customers vehicles
                setVehicles();
                // set first set of fields enabled
                setFirstFields(true);
            } else {
                // set first set of fields disabled
                setFirstFields(false);
                // throw excetion as no customer exists
                throw new NoCustomerException();
            }
        } 
        catch (NoCustomerException ex) {
            view.showErrorMessage(ex.getMessage());
        } 
        catch (SQLException ex) {
            view.showErrorMessage("A Database Error Occured: "+getClass().toString()+".setCustomers()"+
                    "\n"+ex.getMessage());
        }
    }
    
    /**
     * Loads all vehicles in the database for selected customer;
     * throws a NoVehicleException if no customers are registered.
     */
    private void setVehicles() {
        try {
            // if customerId==0 no customers exist
            if(view.getCustomerId()!=0) {
                // get vehicles for given customer
                String[] vehicleModel = model.getVehicles(view.getCustomerId());
                // if no vehicles throw exception
                if(vehicleModel.length == 0) 
                    throw new NoVehicleException(view.getCustomer());
                // else set vehicles
                view.getVehicleIdField().setModel(new DefaultComboBoxModel(vehicleModel));
            }
        } catch (NoVehicleException ex) {
            view.showErrorMessage(ex.getMessage());
                // set first set of fields disabled since no vehicle
                setFirstFields(false);
        } 
catch (SQLException ex) {
            view.showErrorMessage("A Database Error Occured: "+getClass().toString()+".setVehicles()"+
                    "\n"+ex.getMessage());
        }
    }
    
    /**
     * Sets all booking fields for a booked Mot to enable editing
     * @param startDate - the startDate of the Mot
     * @param startTime - the startTime of the Mot
     * @param endDate - the endDate of the Mot
     * @param endTime - the endTime of the Mot
     * @param bay - the booked bay of the Mot
     * @param customerId - the customer ID of the Mot 
     */
    public void setMotUpdate(Date startDate, Time startTime, Date endDate, Time endTime,
            Integer bay, Integer customerId) {
        try {
            // get an mot object reference from model
            mot = model.getMot(startDate, startTime, endDate, endTime, bay, customerId);
            // set customer id
            view.getCustomerIdField().setSelectedItem(mot.getCustomerID());
            // set vehicles
            view.getVehicleIdField().setModel(new DefaultComboBoxModel(model.getVehicles(mot.getCustomerID())));
            // set start date
            // Calendar had to be used as errors with java.sql.Time and java.util.Time
            String[] splitDate = startDate.toString().split("-");
            int year = Integer.parseInt(splitDate[0]);
            int month = Integer.parseInt(splitDate[1]);
            int day = Integer.parseInt(splitDate[2]);
            Calendar calendar = new GregorianCalendar(year, month-1, day);
            view.getStartDateField().setDate(calendar.getTime());
            
            // set end date
            view.getEndDateField().setText(endDate.toString());
            // set end time
            String endTimeStr = endTime.toString();
            endTimeStr = endTimeStr.substring(0, endTimeStr.length()-3);
            view.getEndTimeField().setText(endTimeStr);
            // set bay
            view.getBayField().setSelectedItem(bay);
            // set Mechanic
            view.setMechanic(model.getMechanic(mot.getBay()));
            // set mileage
            view.getMileageField().setText(mot.getMileage().toString());
            
            // check if booking has already been completed
            if(startDate.compareTo(new Date(new java.util.Date().getTime())) <0) {
                // if booking has been completed allow changes to mot pass/fail to be made
                view.getPassField().setEnabled(true);
                
                // if passed
                if(((Mot)mot).getPass()) {
                    view.getPassField().setSelectedIndex(0);
                } 
                // if failed
                else {
                    view.getPassField().setSelectedIndex(1);
                    view.getReasonField().setEnabled(true);
                    view.getReasonField().setText(((Mot)mot).getReason());
                }
            }
        } catch (SQLException ex) {
            view.showErrorMessage("Mot Update Error\n"+ex.getMessage());
        }
    }
    
    /**
     * Get MotModel
     * @return - MotModel
     */
    public MotModel getModel() {
        return model;
    }
    
    /**
     * Checks all necessary fields are valid to enable Add button.
     */
    public void checkIfButtonToBeEnabled() {
        if(view.getCustomerId() != 0 && view.getVehicleId() != 0
                && view.getStartDate() != null && view.getStartTime() != null
                && view.getBay() != null && view.getMechanicID() != 0 && view.getMileage() != null)
            view.getAdd().setEnabled(true);
        else 
            view.getAdd().setEnabled(false);
    }
    
    /**
     * Set vehicleID and StartDate fields enabled/disabled
     * @param val - boolean to represent whether fields should be enabled
     */
    public void setFirstFields(boolean val) {
        view.getVehicleIdField().setEnabled(val);
        view.checkVehicleId(val);
        view.getStartDateField().setEnabled(val);
        view.revalidate();
    }
    
    /**
     * Set bay and mileage fields enabled/disabled
     * @param val - boolean to represent whether fields should be enabled
     */
    public void setSecondFields(boolean val) {
        Time endTime = view.getStartTime();
        String endTimeStr = (endTime.getHours()+1)+":00";
        view.setEndTime(endTimeStr); 
        view.getBayField().setEnabled(val);
        view.getMechanicField().setEnabled(val);
        view.getMileageField().setEnabled(val);
        view.revalidate();
    }
    
    /**
     * Set bay field to available bays.
     */
    public void setBays() {
        try {
            // check required fields are not null
            if(view.getStartDate()!=null && view.getEndDate()!=null && view.getStartTime()!=null && view.getEndTime()!=null) {    
                view.getBayField().setEnabled(true);
                view.getMechanicField().setEnabled(true);
                // get bays
                Integer[] availableBays = getBays();
                
                if(availableBays.length < 1 && view.isVisible()) {
                    view.getBayField().setEnabled(false);
                    view.getMechanicField().setEnabled(false);
                    view.showMessage("No Bays available for this time");
                } 
                else {
                    // set bays
                    view.getBayField().setModel(new DefaultComboBoxModel(availableBays));
                    // set mechanics
                    view.setMechanic(model.getMechanic(availableBays[0]));
                }
            } else {
                // disable bay field if required field are null
                view.getBayField().setEnabled(false);
            }
        } catch (SQLException | NullPointerException ex) {
            view.showErrorMessage(ex.getMessage());
        }
    }
    
    /**
     * Returns the available bays for dates and times
     * @return
     * @throws SQLException 
     */
    private Integer[] getBays() throws SQLException {       
        // get available bays
        Integer[] availableBays = model.getAvailableBays(view.getStartDate(), view.getStartTime(), view.getEndDate(), view.getEndTime());
        
        // if updating booking sort bays and add current booked bay
        if(view.isUpdate() && availableBays.length!=6) {
            Integer[] availableUpdateBays = new Integer[availableBays.length+1];
            Integer updateBay = mot.getBay();
            availableBays = sortBaysForUpdate(availableBays, availableUpdateBays, updateBay);
        }
        return availableBays;
    }
    
    /**
     * Sorts bays available in ascending order including the bay booked for update
     * @param availableBays
     * @param availableUpdateBays
     * @param updateBay
     * @return 
     */
    private Integer[] sortBaysForUpdate(Integer[] availableBays, Integer[] availableUpdateBays, Integer updateBay) {
        //-----------------------------------------------------------------
        //-------------------DOUBLE CHECK CODE BELOW-----------------------
        // logic to add selected bay
        for(int i=0; i<availableUpdateBays.length; i++) {
            if(i==availableBays.length) {
                if(updateBay<7) {
                    availableUpdateBays[i] = updateBay;
                    updateBay = 999;
                }
                else
                    availableUpdateBays[i] = availableBays[i-1];
            }
            else if(updateBay > 6) {
                availableUpdateBays[i] = availableBays[i-1];
            }
            else if(updateBay < availableBays[i]) {
                availableUpdateBays[i] = updateBay;
                updateBay = 10;
            } 
            else 
                availableUpdateBays[i] = availableBays[i];
        }
        return availableUpdateBays;
    }
    
    
    public class AddBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(view.getStartDate()==null) {
                view.showErrorMessage("Please input a correct date");
                return;
            }
            
            mot = new Mot(null, null, view.getVehicleId(), view.getCustomerId(), view.getMechanicID(), 
                view.getStartDate(), view.getStartTime(), view.getEndDate(), view.getEndTime(), view.getBay(),
                view.getTotalCost(), view.getBookingType(), view.getMileage(), false, 
                view.getSchemainType(), null, view.getPass(), null);
            
            try {
                model.add((Mot) mot);
                view.showMessage("Mot booking added!");
                view.exit();
            } catch (SQLException ex) {
                view.showErrorMessage("Error: Mot booking not added!\n"+ex.getMessage());
            }
        }
    }
    
    public class UpdateBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mot = new Mot(mot.getBookingID(), mot.getSchemainId(), view.getVehicleId(),
                view.getCustomerId(), view.getMechanicID(), view.getStartDate(), 
                view.getStartTime(), view.getEndDate(), view.getEndTime(), view.getBay(),
                view.getTotalCost(), view.getBookingType(), view.getMileage(), false, 
                view.getSchemainType(), null, null, null);
            
            try {
                model.update((Mot) mot);
                view.showMessage("Mot booking updated!");
                view.exit();
            } catch (SQLException ex) {
                view.showErrorMessage("Error: Mot booking not updated!\n"+ex.getMessage());
            }
        }        
    }
    
    public class CancelBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.exit();
        }
        
    }
    
    public class CustomerIdListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Integer customerId = view.getCustomerId();
            view.checkCustomerId(true);
            try {
                view.getVehicleIdField().setModel(new DefaultComboBoxModel(model.getVehicles(customerId)));
            } catch (SQLException ex) {
                view.showErrorMessage(ex.getMessage());
            }
            setFirstFields(true);
            checkIfButtonToBeEnabled();
        }
    }
    
    public class VehicleIdListener implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if(view.getVehicleId()==0) {
                view.checkVehicleId(false);
                view.showErrorMessage("Error: "+view.getCustomerId()+" has no vehicles, please add a vehicle to continue");
                view.exit();
            }
            else
                view.checkVehicleId(true);
            checkIfButtonToBeEnabled();
        }
    }
    
    public class StartDateListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            try {
                Date date = view.getStartDate();
                int day = date.getDay();
                if(day==0 || model.isHoliday(date)) {
                    view.setStartTime(new String[]{""});
                    view.checkStartTime(false);
                    view.getStartTimeField().setEnabled(false);
                    setSecondFields(false);
                }
                else {                    
                    if(day==6) {
                        view.setStartTime(model.getWeekendTimes());
                    }
                    else {
                        view.setStartTime(model.getWeekdayTimes());
                    }
                    
                    view.getStartTimeField().setEnabled(true);
                    view.checkStartTime(true);
                    setSecondFields(true);
                    
                    view.setEndDate(date);
                    setBays();
                    view.revalidate();
                }
            } catch (NullPointerException ex) {
                // do nothing
            }
            checkIfButtonToBeEnabled();
        }
    }
    
    public class StartTimeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Time time = view.getStartTime();
            time.setHours(time.getHours()+1);
            String str = time.toString();
            view.getEndTimeField().setText(str.substring(0, str.length()-3));
            setBays();
            checkIfButtonToBeEnabled();
        }
    }
    
    public class BayListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(view.getBay()!=0 || view.getBay() != null) {
                view.checkBay(true);
                try {
                    view.setMechanic(model.getMechanic(view.getBay()));
                    view.checkMechanic(true);
                } catch (SQLException ex) {
                    view.showErrorMessage("No Mechanic Avaliable!");
                    view.checkMechanic(false);
                }
            }
            else {
                view.checkBay(false);
                view.checkMechanic(false);
            }
            
            if(view.getMechanicID()==0)
                view.showMessage("There are no Mechanics for this bay!");
            
            checkIfButtonToBeEnabled();
        }
    }
    
    public class MileageListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {        }

        @Override
        public void keyPressed(KeyEvent e) {        }

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
            checkIfButtonToBeEnabled();
        }
               
    }
    
    public class PassListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(view.getPass())
                view.getReasonField().setEnabled(false);
            else {
                view.getReasonField().setEnabled(true);
                view.getReasonField().setText(null);
            }
            checkIfButtonToBeEnabled();
        }
    }
}
