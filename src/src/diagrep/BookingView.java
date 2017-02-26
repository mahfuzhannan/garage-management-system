
package diagrep;

import assets.GMButton;
import assets.GMFormattedTextField;
import assets.Res;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;
import java.sql.Date;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

/**
 * @author vc300
 */
class BookingView extends JDialog{
    
    
    private GMFormattedTextField totalCost, bookingType, mileage;
    private GMButton add;
    private JComboBox customerIdField, bayField, startTimeField, endTimeField,
            vehicleIdField, mechanicField;
    private JLabel label;
    private JDateChooser startDateField, endDateField;
    private final int w = 10;
    private final int h = 10;
    private final boolean update;
    
    BookingView(JFrame parent, boolean update) {
        super(parent,true);
        this.update = update;
        init();
    }
    
    
    //Initialise Booking view components
    private void init() {
        this.getContentPane().setBackground(Res.BKG_COLOR);
        BorderLayout borderLayout = new BorderLayout(20, 10);
        setLayout(borderLayout);
        // main fields panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(14, 1, 5, 10));
        panel.setBackground(Res.BKG_COLOR);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        
        label = new JLabel("Customer: ");
        label.setSize(w, h);
        label.setForeground(Res.FONT_COLOR);
        customerIdField = new JComboBox();
        customerIdField.setSize(w, h);
        panel.add(label);
        panel.add(customerIdField);
        
        label = new JLabel("Vehicle ID: ");
        label.setSize(w, h);
        label.setForeground(Res.FONT_COLOR);
        vehicleIdField = new JComboBox();
        vehicleIdField.setSize(w, h);
        panel.add(label);
        panel.add(vehicleIdField);
        
        label = new JLabel("Start Date: ");
        label.setSize(w, h);
        label.setForeground(Res.FONT_COLOR);
        startDateField = new JDateChooser();
        startDateField.setDateFormatString("yyyy-MM-dd");
        startDateField.setMinSelectableDate(new java.util.Date());
        panel.add(label);
        panel.add(startDateField);
        
        label = new JLabel("Start Time: ");
        label.setSize(w, h);
        label.setForeground(Res.FONT_COLOR);
        panel.add(label);
        startTimeField = new JComboBox();
        panel.add(startTimeField);
        
        label = new JLabel("End Date: ");
        label.setSize(w, h);
        label.setForeground(Res.FONT_COLOR);
        endDateField = new JDateChooser();
        endDateField.setDateFormatString("yyyy-MM-dd");
        endDateField.setMinSelectableDate(new java.util.Date());
        panel.add(label);
        panel.add(endDateField);
        
        
        panel.add(label);
        panel.add(endDateField);
        
        //end time
       label = new JLabel("End Time: ");
        label.setSize(w, h);
        label.setForeground(Res.FONT_COLOR);
        panel.add(label);
        endTimeField = new JComboBox();
        panel.add(endTimeField);
       //bays
        label = new JLabel("Bay: ");
        label.setSize(w, h);
        label.setForeground(Res.FONT_COLOR);
        bayField = new JComboBox();
        bayField.setSize(w, h);
        panel.add(label);
        panel.add(bayField);
        
        label = new JLabel("Mechanic: ");
        label.setSize(w,h);
        label.setForeground(Res.FONT_COLOR);
         mechanicField = new JComboBox();
        mechanicField.setEditable(false);
        panel.add(label);
        panel.add(mechanicField);
        
        label = new JLabel("Total Cost: ");
        label.setSize(w, h);
        label.setForeground(Res.FONT_COLOR);
        totalCost = new GMFormattedTextField(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0.00"))));
        totalCost.setColumns(w);
        totalCost.setText("");
        totalCost.setEditable(false);
        totalCost.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.GREEN, Res.GREEN));
        panel.add(label);
        panel.add(totalCost);
        
        label = new JLabel("Booking Type: ");
        label.setSize(w, h);
        label.setForeground(Res.FONT_COLOR);
        bookingType = new GMFormattedTextField();
        bookingType.setColumns(w);
        bookingType.setText("DiagRep");
        bookingType.setEditable(false);
        bookingType.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.GREEN, Res.GREEN));
        panel.add(label);
        panel.add(bookingType);

        label = new JLabel("Mileage: ");
        label.setSize(w, h);
        label.setForeground(Res.FONT_COLOR);
        mileage = new GMFormattedTextField(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0.00"))));
        mileage.setColumns(w);
        
        panel.add(label);
        panel.add(mileage);
        
        label = new JLabel("Add Diagnosis & Repair");
        label.setFont(new Font("", 0, 26));
        label.setForeground(Res.FONT_COLOR);
        label.setBackground(Res.BKG_COLOR);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel titlePanel = new JPanel();
        titlePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        titlePanel.setBackground(Res.BKG_COLOR);
        titlePanel.add(label);
        
        add = new GMButton("ADD");
        add.setEnabled(false);
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new GridLayout(0, 2, 10, 10));
        btnPanel.setBackground(Res.BKG_COLOR);
        btnPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        btnPanel.add(add);  
        
        this.add(titlePanel, BorderLayout.PAGE_START);
        this.add(panel, BorderLayout.CENTER);
        this.add(btnPanel, BorderLayout.PAGE_END);
        
        this.setBackground(Res.BKG_COLOR);
        this.setSize(Res.WIDTH/4, Res.HEIGHT-50);
        
        this.setResizable(false);
        this.setLocationRelativeTo(super.getParent());
        if(!update){
            customerIdField.setEditable(false);
            vehicleIdField.setEnabled(false);
            startDateField.setEnabled(false);
            startTimeField.setEnabled(false);
            endDateField.setEnabled(false);
            endTimeField.setEnabled(false);
            bayField.setEnabled(false);
            mileage.setEnabled(false);
        }
    }
    
    boolean isUpdate() {
        return update;
    }
    
    
    
    
    ///////////////////////////////////////////////////////////
    /////////////////////Listeners///////////////////////////////
    ///////////////////////////////////////////////////////////
    
    void addAddListener(ActionListener add) {
        this.add.addActionListener(add);
    }
    
    
    void addCustomerListener(ActionListener act) {
        this.customerIdField.addActionListener(act);
    }
    
    void addVehicleListener(ActionListener act) {
        this.vehicleIdField.addActionListener(act);
    }
    
    void addStartDateListener(PropertyChangeListener act) {
        this.startDateField.addPropertyChangeListener(act);
    }
    
    void addEndTimeListener(ActionListener act) {
        this.endTimeField.addActionListener(act);
    }
    
    void addEndDateListener(PropertyChangeListener act) {
        this.endDateField.addPropertyChangeListener(act);
    }
    
    void addBayListener(ActionListener act) {
        this.bayField.addActionListener(act);
    }
    
    void addMileageListener(KeyListener key) {
        this.mileage.addKeyListener(key);
    }
    
    
    
    
    
    
    
    ///////////////////////////////////////////////////////////
    /////////////////////Component Check interface///////////////////////////////
    ///////////////////////////////////////////////////////////
    
    //check front end to show if valid or invalid entries
    
    void checkCustomerId(boolean check) {
        if(check) {
           customerIdField.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.GREEN, Res.GREEN));
           customerIdField.setToolTipText(null);
        } else {
           customerIdField.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.RED, Res.RED));
           customerIdField.setToolTipText("Customer ID not found, please eneter an existing Customer ID");
        }
    }
    
    void checkVehicleId(boolean check) {
        if(check) {
           vehicleIdField.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.GREEN, Res.GREEN));
           vehicleIdField.setToolTipText(null);
        } else {
           vehicleIdField.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.RED, Res.RED));
           vehicleIdField.setToolTipText("Customer: "+getCustomer()+" has no vehicles");
        }
    }
    
    
    void checkStartTime(boolean check) {
        if(check) {
           startTimeField.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.GREEN, Res.GREEN));
           startTimeField.setToolTipText(null);
        } else {
           startTimeField.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.RED, Res.RED));
           startTimeField.setToolTipText("Garage is closed");
        }
    }
    
    void checkEndTime(boolean check) {
        if(check) {
           endTimeField.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.GREEN, Res.GREEN));
           endTimeField.setToolTipText(null);
        } else {
           endTimeField.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.RED, Res.RED));
           endTimeField.setToolTipText("Garage is closed");
        }
    }
    void checkBay(boolean check) {
        if(check) {
           bayField.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.GREEN, Res.GREEN));
        } else {
           bayField.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.RED, Res.RED));
        }
    }
    
    
    void checkMechanic(boolean check){
        if(check) {
           mechanicField.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.GREEN, Res.GREEN));
           mechanicField.setToolTipText(null);
        } else {
           mechanicField.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.RED, Res.RED));
           mechanicField.setToolTipText("No Mechanic available");
        }
    }
    
    
    
    void checkMileage(boolean check) {
        if(check) {
           mileage.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.GREEN, Res.GREEN));
           mileage.setToolTipText(null);
        } else {
           mileage.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.RED, Res.RED));
           mileage.setToolTipText("Enter a number");
        }
    }
   
   
    ///////////////////////////////////////////////////////////
    /////////////////////Setters///////////////////////////////
    ///////////////////////////////////////////////////////////
    
    
    void setVehicleId(Integer[] vehicles) {
        this.vehicleIdField.setModel(new DefaultComboBoxModel(vehicles));
    }

    void setBay(Integer[] bays) {
        this.bayField.setModel(new DefaultComboBoxModel(bays));
    }
    
    void setMechanic(String[] mechanics) {
        this.mechanicField.setModel(new DefaultComboBoxModel(mechanics));
    }
    
    void setStartDate(java.util.Date date) {
        this.startDateField.setDate(date);
    }

    void setStartTime(String[] times) {
        this.startTimeField.setModel(new DefaultComboBoxModel(times));
    }
    
    void setEndDate(java.util.Date date) {
        this.endDateField.setDate(date);
    }
    
    void setEndTime(String[] times) {
        this.endTimeField.setModel(new DefaultComboBoxModel(times));
    }
    
    
    
    ///////////////////////////////////////////////////////////
    /////////////////////Message Functions/////////////////////
    ///////////////////////////////////////////////////////////
   
    
    void showMessage(boolean error,String msg) {
        if(error)
                {JOptionPane.showMessageDialog(this, msg, "ERROR", JOptionPane.ERROR_MESSAGE);
                }
        else
                {JOptionPane.showMessageDialog(this, msg, null, JOptionPane.INFORMATION_MESSAGE);
                }
                
    }
    
    
    
    ///////////////////////////////////////////////////////////
    /////////////////////Getters///////////////////////////////
    ///////////////////////////////////////////////////////////
     
      
    Integer getMechanicID() {
        if(mechanicField.getSelectedItem()==null) {
            return 0;
        }
        String[] split = mechanicField.getSelectedItem().toString().split(": ");
        return Integer.parseInt(split[0]);
    }
    
    Integer getVehicleId() {
        if(vehicleIdField.getSelectedItem()==null) {
            return 0;
        }
        String[] split = vehicleIdField.getSelectedItem().toString().split(": ");
        return Integer.parseInt(split[0]);
    }
    
    Integer getCustomerId() {
        if(customerIdField.getSelectedItem()==null) {
            return 0;
        }
        String[] split = customerIdField.getSelectedItem().toString().split(": ");
        return Integer.parseInt(split[0]);
    }
    
    String getCustomer() {
        return customerIdField.getSelectedItem().toString();
    }
    
    
    Date getStartDate() {
        try {
            return Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(startDateField.getDate()));
        } catch (Exception e) {        
            return null;
        }
    }
    
    Time getStartTime() {
        try {
            return Time.valueOf(startTimeField.getSelectedItem().toString() + ":00");
        } catch (Exception e) {        
            return null;
        }
    }
    
    Date getEndDate() {
        return Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(endDateField.getDate()));
    }
    
    Time getEndTime() {
         try {
            return Time.valueOf(endTimeField.getSelectedItem().toString() + ":00");
        } catch (Exception e) {        
            return null;
        }
    }
    
    Integer getBay() {
        return ((Integer)bayField.getSelectedItem());
    }
    
    Double getTotalCost() {
        try {
            return Double.parseDouble(totalCost.getText());
        } catch (Exception e) {        
            return 0.0;
        }
    }
    
    
    Double getMileage() {
        try {
            return Double.parseDouble(mileage.getText());
        } catch (Exception e) {        
            return 0.0;
        }
    }
    
    
    
     ///////////////////////////////////////////////////////////
    /////////////////////GUI Component Getters///////////////////////////////
    ///////////////////////////////////////////////////////////
    
    
    JComboBox getCustomerField() {
        return customerIdField;
    }

    JComboBox getVehicleIdField() {
        return vehicleIdField;
    }
    
    
    JComboBox getMechanicField() {
        return mechanicField;
    }
    
    JDateChooser getStartDateField() {
        return startDateField;
    }

    JComboBox getStartTimeField() {
        return startTimeField;
    }

    JDateChooser getEndDateField() {
        return endDateField;
    }

    JComboBox getEndTimeField() {
        return endTimeField;
    }

    JComboBox getBayField() {
        return bayField;
    }
    
    GMFormattedTextField getTotalCostField() {
        return totalCost;
    }
    
    GMFormattedTextField getMileageField() {
        return mileage;
    }

    GMButton getAdd() {
        return add;
    }
    
    ///////////////////////////////////////////////////////////
    /////////////////////dispose method///////////////////////////////
    ///////////////////////////////////////////////////////////
    
     void exit() {
        this.setVisible(false);
        this.dispose();
    }

    
}
