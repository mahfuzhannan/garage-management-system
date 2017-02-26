/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemain.service;

import assets.GMButton;
import assets.GMFormattedTextField;
import assets.Res;
import booking.Booking;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import schemain.Schemain;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

/**
 *
 * @author Mahfuz
 */
public final class ServiceView extends JDialog{
    private GMFormattedTextField endDateField, endTimeField,
            totalCostField, bookingTypeField, mileageField, schemainTypeField;
    private JComboBox customerIdField, bayField, mechanicField, serviceTypeField, startTimeField,
            vehicleIdField, nextServiceField;
    private JLabel label;
    private JDateChooser startDateField;
    private GMButton add, cancel;
    private final int w = 10;
    private final int h = 10;
    private final boolean update;

    public ServiceView(JFrame parent, boolean modal, boolean update) {
        super(parent, modal);
        this.update = update;
        init();
    }
    
    public void init() {
        this.getContentPane().setBackground(Res.BKG_COLOR);
        BorderLayout borderLayout = new BorderLayout(20, 10);
        setLayout(borderLayout);
        // main fields panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(14, 1, 5, 10));
        panel.setBackground(Res.BKG_COLOR);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        
        /*
        * CUSTOMER ID
        */
        label = new JLabel("Customer: ");
        label.setSize(w, h);
        label.setForeground(Res.FONT_COLOR);
        //customerIdField = new GMFormattedTextField(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
        //customerIdField.setColumns(w);
        customerIdField = new JComboBox();
        customerIdField.setSize(w, h);
        if(isUpdate()) {
            customerIdField.setEditable(false);
            checkCustomerId(true);
        }
        panel.add(label);
        panel.add(customerIdField);
        
        /*
        * VEHICLE ID
        */
        label = new JLabel("Vehicle ID: ");
        label.setSize(w, h);
        label.setForeground(Res.FONT_COLOR);
        //vehicleIdField = new GMFormattedTextField(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
        //vehicleIdField.setColumns(w);
        //vehicleIdField.setToolTipText("Enter ID the format 123");
        vehicleIdField = new JComboBox();
        vehicleIdField.setSize(w, h);
        if(!isUpdate())
            vehicleIdField.setEnabled(false);
        panel.add(label);
        panel.add(vehicleIdField);
        
        /*
        * START DATE
        */
        label = new JLabel("Start Date: ");
        label.setSize(w, h);
        label.setForeground(Res.FONT_COLOR);
        startDateField = new JDateChooser();
        startDateField.setDateFormatString("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        startDateField.setMinSelectableDate(cal.getTime());
        startDateField.setDate(cal.getTime());
        if(!isUpdate())
            startDateField.setEnabled(false);
        //startDateField.setEditable(false);
        panel.add(label);
        panel.add(startDateField);
        
        /*
        * START TIME
        */
        label = new JLabel("Start Time: ");
        label.setSize(w, h);
        label.setForeground(Res.FONT_COLOR);
        panel.add(label);
        //panel.add(startTimeField);
        startTimeField = new JComboBox();
        if(!isUpdate())
            startTimeField.setEnabled(false);
        panel.add(startTimeField);
        
        /*
        * END DATE
        */
        label = new JLabel("End Date: ");
        label.setSize(w, h);
        label.setForeground(Res.FONT_COLOR);
        endDateField = new GMFormattedTextField(new DefaultFormatterFactory(new DateFormatter(new SimpleDateFormat("yyyy-mm-dd"))));
        endDateField.setColumns(w);
        endDateField.setEditable(false);
        panel.add(label);
        panel.add(endDateField);
        
        /*
        * END TIME
        */
        label = new JLabel("End Time: ");
        label.setSize(w, h);
        label.setForeground(Res.FONT_COLOR);
        endTimeField = new GMFormattedTextField(new DefaultFormatterFactory(new DateFormatter(DateFormat.getTimeInstance((DateFormat.SHORT)))));
        endTimeField.setEditable(false);
        panel.add(label);
        panel.add(endTimeField);
        
        /*
        * BAYS
        */
        label = new JLabel("Bay: ");
        label.setSize(w, h);
        label.setForeground(Res.FONT_COLOR);
        bayField = new JComboBox();
        bayField.setModel(new DefaultComboBoxModel(new Integer[]{1,2,3,4,5,6}));
        bayField.setSize(w, h);
        if(!isUpdate())
            bayField.setEnabled(false);
        panel.add(label);
        panel.add(bayField);
        
        /*
        * MECHANIC ID
        */
        label = new JLabel("Mechanic: ");
        label.setSize(w,h);
        label.setForeground(Res.FONT_COLOR);
        //mechanicField = new GMFormattedTextField();
        //mechanicField.setText("1");
        //mechanicField.setEditable(false);
        mechanicField = new JComboBox();
        mechanicField.setSize(w, h);
        if(!isUpdate())
            mechanicField.setEnabled(false);
        panel.add(label);
        panel.add(mechanicField);
        
        /*
        * TOTAL COST
        */
        label = new JLabel("Total Cost: ");
        label.setSize(w, h);
        label.setForeground(Res.FONT_COLOR);
        totalCostField = new GMFormattedTextField(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0.00"))));
        totalCostField.setColumns(w);
        totalCostField.setText("150.00");
        totalCostField.setEditable(false);
        totalCostField.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.GREEN, Res.GREEN));
        panel.add(label);
        panel.add(totalCostField);
        
        /*
        * BOOKING TYPE
        */
        label = new JLabel("Booking Type: ");
        label.setSize(w, h);
        label.setForeground(Res.FONT_COLOR);
        bookingTypeField = new GMFormattedTextField();
        bookingTypeField.setColumns(w);
        bookingTypeField.setText("SCHEMAIN");
        bookingTypeField.setEditable(false);
        bookingTypeField.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.GREEN, Res.GREEN));
        panel.add(label);
        panel.add(bookingTypeField);

        /*
        * MILEAGE
        */
        label = new JLabel("Mileage: ");
        label.setSize(w, h);
        label.setForeground(Res.FONT_COLOR);
        mileageField = new GMFormattedTextField(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0.00"))));
        mileageField.setColumns(w);
        if(!isUpdate())
            mileageField.setEnabled(false);
        panel.add(label);
        panel.add(mileageField);
        
        /*
        * SCHEDULED MAINTENANCE TYPE
        */
        label = new JLabel("Schemain Type: ");
        label.setSize(w, h);
        label.setForeground(Res.FONT_COLOR);
        schemainTypeField = new GMFormattedTextField();
        schemainTypeField.setColumns(w);
        schemainTypeField.setText("SERVICE");
        schemainTypeField.setEditable(false);
        schemainTypeField.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.GREEN, Res.GREEN));
        panel.add(label);
        panel.add(schemainTypeField);
        
        /*
        * SERVICE TYPE
        */
        label = new JLabel("Service Type: ");
        label.setSize(w, h);
        label.setForeground(Res.FONT_COLOR);
        serviceTypeField = new JComboBox(new DefaultComboBoxModel(new String[]{"MILEAGE", "TIMEBASED"}));
        if(!isUpdate())
            serviceTypeField.setEnabled(false);
        panel.add(label);
        panel.add(serviceTypeField);
        
        
        /*
        * NEXT SERVICE
        */
        label = new JLabel("Next Service After: ");
        label.setSize(w, h);
        label.setForeground(Res.FONT_COLOR);
        nextServiceField = new JComboBox();
        nextServiceField.setEnabled(false);
        panel.add(label);
        panel.add(nextServiceField);
        
        // create title label
        label = new JLabel("Add Service");
        label.setFont(new Font("", 0, 26));
        label.setForeground(Res.FONT_COLOR);
        label.setBackground(Res.BKG_COLOR);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel titlePanel = new JPanel();
        titlePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        titlePanel.setBackground(Res.BKG_COLOR);
        titlePanel.add(label);
        
        // create buttons
        add = new GMButton("ADD");
        add.setEnabled(false);
        cancel = new GMButton("CANCEL");
        // create button panel
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new GridLayout(0, 2, 10, 10));
        btnPanel.setBackground(Res.BKG_COLOR);
        btnPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        // add buttons to panel
        btnPanel.add(add);  btnPanel.add(cancel);
        
        // add components to main container
        this.add(titlePanel, BorderLayout.PAGE_START);
        this.add(panel, BorderLayout.CENTER);
        this.add(btnPanel, BorderLayout.PAGE_END);
        
        this.setBackground(Res.BKG_COLOR);
        this.setSize(Res.WIDTH/4, Res.HEIGHT-50);
        //this.pack();
        
        this.setResizable(false);
        this.setLocationRelativeTo(super.getParent());
    }
    
    
    
    //////////////////////////////////////////////////////////
    //    ####### #     # ####### #     # #######  #####    //
    //    #       #     # #       ##    #    #    #     #   //
    //    #       #     # #       # #   #    #    #         //
    //    #####   #     # #####   #  #  #    #     #####    //
    //    #        #   #  #       #   # #    #          #   //
    //    #         # #   #       #    ##    #    #     #   //
    //    #######    #    ####### #     #    #     #####    //
    //////////////////////////////////////////////////////////
    
    public void addAddBtnListener(ActionListener add) {
        this.add.addActionListener(add);
    }
    
    public void addCancelBtnListener(ActionListener cancel) {
        this.cancel.addActionListener(cancel);
    }
    
    public void addCustomerIdListener(ActionListener key) {
        this.customerIdField.addActionListener(key);
    }
    
    public void addVehicleIdListener(ActionListener act) {
        this.vehicleIdField.addActionListener(act);
    }
    
    public void addStartDateListener(PropertyChangeListener prop) {
        this.startDateField.addPropertyChangeListener(prop);
    }
    
    public void addStartTimeListener(ActionListener act) {
        this.startTimeField.addActionListener(act);
    }
    
    public void addBayListener(ActionListener act) {
        this.bayField.addActionListener(act);
    }
    
    public void addServiceTypeListener(ActionListener act) {
        this.serviceTypeField.addActionListener(act);
    }
    
    public void addMileageListener(KeyListener key) {
        this.mileageField.addKeyListener(key);
    }
    
    public void addNextServiceListener(ActionListener act) {
        this.nextServiceField.addActionListener(act);
    }
    
    
    /**
     * Changes component border color in relation to value of component, 
     * RED referring to ILLEGAL and GREEN referring to CORRECT.
     * @param check 
     */
    public void checkCustomerId(boolean check) {
        if(check) {
           customerIdField.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.GREEN, Res.GREEN));
           customerIdField.setToolTipText(null);
        } else {
           customerIdField.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.RED, Res.RED));
           customerIdField.setToolTipText("Customer ID not found, please enter an existing Customer ID");
        }
    }
    
    /**
     * Changes component border color in relation to value of component, 
     * RED referring to ILLEGAL and GREEN referring to CORRECT.
     * @param check 
     */
    public void checkVehicleId(boolean check) {
        if(check) {
           vehicleIdField.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.GREEN, Res.GREEN));
           vehicleIdField.setToolTipText(null);
        } else {
           vehicleIdField.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.RED, Res.RED));
           vehicleIdField.setToolTipText("Customer: "+getCustomerId()+" has no vehicles");
        }
    }
    
    /**
     * Changes component border color in relation to value of component, 
     * RED referring to ILLEGAL and GREEN referring to CORRECT.
     * @param check 
     */
    public void checkStartTime(boolean check) {
        if(check) {
           startTimeField.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.GREEN, Res.GREEN));
           startTimeField.setToolTipText(null);
           endDateField.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.GREEN, Res.GREEN));
           endTimeField.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.GREEN, Res.GREEN));
        } else {
           startTimeField.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.RED, Res.RED));
           startTimeField.setToolTipText("Garage is closed");
           add.setEnabled(false);
        }
    }
    
    /**
     * Changes component border color in relation to value of component, 
     * RED referring to ILLEGAL and GREEN referring to CORRECT.
     * @param check 
     */
    public void checkBay(boolean check) {
        if(check) {
           bayField.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.GREEN, Res.GREEN));
           bayField.setToolTipText(null);
        } else {
           bayField.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.RED, Res.RED));
           bayField.setToolTipText("Garage is closed");
        }
    }
    
    /**
     * Changes component border color in relation to value of component, 
     * RED referring to ILLEGAL and GREEN referring to CORRECT.
     * @param check 
     */
    public void checkMechanic(boolean check) {
        if(check) {
           mechanicField.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.GREEN, Res.GREEN));
           mechanicField.setToolTipText(null);
        } else {
           mechanicField.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.RED, Res.RED));
           mechanicField.setToolTipText("No Mechanic available");
        }
    }
    
    /**
     * Changes component border color in relation to value of component, 
     * RED referring to ILLEGAL and GREEN referring to CORRECT.
     * @param check 
     */
    public void checkMileage(boolean check) {
        if(check) {
           mileageField.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.GREEN, Res.GREEN));
           mileageField.setToolTipText(null);
        } else {
           mileageField.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.RED, Res.RED));
           mileageField.setToolTipText("Enter a number");
        }
    }
    
    /**
     * Changes component border color in relation to value of component, 
     * RED referring to ILLEGAL and GREEN referring to CORRECT.
     * @param check 
     */
    public void checkNextService(boolean check) {
        if(check) {
           nextServiceField.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.GREEN, Res.GREEN));
           nextServiceField.setToolTipText(null);
        } else {
           nextServiceField.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.RED, Res.RED));
        }
    }
    
    /**
     * Method to display dialogue with error message
     * @param err 
     */
    public void showErrorMessage(String err) {
        JOptionPane.showMessageDialog(this, err, "ERROR", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Method to display dialogue with message
     * @param message 
     */
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, null, JOptionPane.INFORMATION_MESSAGE);        
    }
    
    public void exit() {
        this.setVisible(false);
        this.dispose();
    }
    
    
    
    ////////////////////////////////////////////////////////////////
    //    #####  ####### ####### ####### ####### ######   #####   //
    //   #     # #          #       #    #       #     # #     #  //
    //   #       #          #       #    #       #     # #        //
    //    #####  #####      #       #    #####   ######   #####   //
    //         # #          #       #    #       #   #         #  //
    //   #     # #          #       #    #       #    #  #     #  //
    //    #####  #######    #       #    ####### #     #  #####   //
    ////////////////////////////////////////////////////////////////

    public void setVehicleId(Integer[] vehicles) {
        this.vehicleIdField.setModel(new DefaultComboBoxModel(vehicles));
    }

    public void setBay(Integer[] bays) {
        this.bayField.setModel(new DefaultComboBoxModel(bays));
    }
    
    public void setMechanic(String[] mechanics/*String mechanic*/) {
        //this.mechanicField.setText(mechanic);
        this.mechanicField.setModel(new DefaultComboBoxModel(mechanics));
    }
    
    public void setStartDate(java.util.Date date) {
        this.startDateField.setDate(date);
    }

    public void setStartTime(String[] times) {
        this.startTimeField.setModel(new DefaultComboBoxModel(times));
    }
    
    public void setEndDate(Date date) {
        this.endDateField.setText(date.toString());
    }
    
    /**
     * 
     * @param time - String due to error of adding extra :00
     */
    public void setEndTime(String time) {
        this.endTimeField.setText(time);
    }
    
    public void setNextService(String[] services) {
        this.nextServiceField.setModel(new DefaultComboBoxModel(services));
    }
    
    
    ////////////////////////////////////////////////////////////////
    //    #####  ####### ####### ####### ####### ######   #####   //
    //   #     # #          #       #    #       #     # #     #  //
    //   #       #          #       #    #       #     # #        //
    //   #  #### #####      #       #    #####   ######   #####   //
    //   #     # #          #       #    #       #   #         #  //
    //   #     # #          #       #    #       #    #  #     #  //
    //    #####  #######    #       #    ####### #     #  #####   //
    ////////////////////////////////////////////////////////////////
    
    public boolean isUpdate() {
        return update;
    }
    
    public Integer getVehicleId() {
        if(vehicleIdField.getSelectedItem()==null) {
            return 0;
        }
        String[] split = vehicleIdField.getSelectedItem().toString().split(": ");
        return Integer.parseInt(split[0]);
    }
    
    public Integer getCustomerId() {
        if(customerIdField.getSelectedItem()==null) {
            return 0;
        }
        String[] split = customerIdField.getSelectedItem().toString().split(": ");
        return Integer.parseInt(split[0]);
    }
    
    public String getCustomer() {
        return customerIdField.getSelectedItem().toString();
    }
    
    public Integer getMechanicID() {
        if(mechanicField.getSelectedItem()==null) {
            return 0;
        }
        String[] split = mechanicField.getSelectedItem().toString().split(": ");
        return Integer.parseInt(split[0]);
    }
    
    public Date getStartDate() {
        try {
            return Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(startDateField.getDate()));
        } catch (Exception e) {        
            return null;
        }
    }
    
    public Time getStartTime() {
        try {
            return Time.valueOf(startTimeField.getSelectedItem().toString() + ":00");
        } catch (Exception e) {        
            return null;
        }
    }
    
    public Date getEndDate() {
        return Date.valueOf(endDateField.getText());
    }
    
    public Time getEndTime() {
        return Time.valueOf(endTimeField.getText() + ":00");
    }
    
    public Integer getBay() {
        return ((Integer)bayField.getSelectedItem());
    }
    
    public Double getTotalCost() {
        return Double.parseDouble(totalCostField.getText());
    }
    
    public Booking.BookingType getBookingType() {
        String bookingType = bookingTypeField.getText();
        if(bookingType.equalsIgnoreCase("DIAGREP"))
            return Booking.BookingType.DIAGREP;
        return Booking.BookingType.SCHEMAIN;
    }
    
    public Double getMileage() {
        try {
            return Double.parseDouble(mileageField.getText());
        } catch (Exception e) {        
            return null;
        }
    }
    
    public Schemain.SchemainType getSchemainType() {
        String schemainType = schemainTypeField.getText();
        if(schemainType.equalsIgnoreCase("MOT"))
            return Schemain.SchemainType.MOT;
        return Schemain.SchemainType.SERVICE;
    }
    
    public Service.ServiceType getServiceType() {
        String serviceType = ((String)serviceTypeField.getSelectedItem());
        if(serviceType.equalsIgnoreCase("MILEAGE"))
            return Service.ServiceType.MILEAGE;
        return Service.ServiceType.TIME;
    }
    
    public String getNextService() {
        return nextServiceField.getSelectedItem().toString();
    }

    public JComboBox getCustomerIdField() {
        return customerIdField;
    }

    public JComboBox getVehicleIdField() {
        return vehicleIdField;
    }
    
    public JDateChooser getStartDateField() {
        return startDateField;
    }

    public JComboBox getStartTimeField() {
        return startTimeField;
    }

    public GMFormattedTextField getEndDateField() {
        return endDateField;
    }

    public GMFormattedTextField getEndTimeField() {
        return endTimeField;
    }

    public JComboBox getBayField() {
        return bayField;
    }

    public JComboBox getMechanicField() {
        return mechanicField;
    }

    public JComboBox getServiceTypeField() {
        return serviceTypeField;
    }

    public GMFormattedTextField getMileageField() {
        return mileageField;
    }

    public JComboBox getNextServiceField() {
        return nextServiceField;
    }
    
    public GMButton getAdd() {
        return add;
    }
    
    
    
    
    
    
    public static void main(String[] args) {
        /*ServiceView view = new ServiceView();
        view.setSize(320, 440);
        view.setResizable(false);
        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        view.setLocationRelativeTo(null);
        view.setVisible(true);
        
        ServiceModel model = new ServiceModel();
        
        ServiceController controller = new ServiceController(model, view);*/
    }
}
