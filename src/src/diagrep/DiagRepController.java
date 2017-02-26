
package diagrep;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 * @author vc300
 */
public class DiagRepController {
    
    private final DiagRepView view;
    private final DiagRepModel model;
    
    
     public DiagRepController(DiagRepView view, DiagRepModel model) {
        this.view = view;
        this.model = model;
        addActionListeners();
    }
     
    
     //initialize and add action listeners
    private void addActionListeners() {
        view.addAddListener(new AddBookingListener());
        view.addUpdateListener(new UpdateListener());
        view.addAddPartListener(new AddPartListener());
        view.addRemoveListener(new RemoveListener());
        view.addDurationListener(new DurationListener());
        refresh();
    }
    
    
    ///////////////////////////////////////////////////////////
    ////////////////Action Listener Classes////////////////////
    ///////////////////////////////////////////////////////////
    
   //refresh tables/set table models
    void refresh() {
        try {
            view.getTable().setModel(model.getTableModel());
            view.getTodayTable().setModel(model.getTodayTableModel());
            view.getPartsTable().setModel(model.getPartsTableModel());
            view.updateTable();
        } catch (SQLException ex) {
        }
    }
    
    
    class AddBookingListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
                // Create & Init booking update
            BookingView bookingView = new BookingView(view.getParentView(), false);
            BookingController bookingController = new BookingController( bookingView,model);
            bookingView.setVisible(true);
            refresh();
        }
    }
    
    
    public class AddPartListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
             if(view.getTable().getSelectedRowCount()>0) {
                
                ArrayList<Object> list = getSelectedRow(view.getTable());
                
                try {
                    String vehicleID = (String)list.get(2);
                    String date = (list.get(6).toString());
                    if(view.getPartsTable().getSelectedRowCount()>0) {
                
                        ArrayList<Object> list1 = getSelectedRow(view.getPartsTable());
                        Integer partID = (Integer)list1.get(0);
                        
                        model.insertPart(date,vehicleID,partID);
                        refresh();
                    view.showMessage(false,"New part installation recorded successfuly!");
                    }else {
                        view.showMessage(true,"Please select a row from parts table!");
                    }
                     
                } catch (Exception ex) {
                    view.showMessage(true,"Part not Installed! \n check again the infomration entered");
                }
                   
            } else {
                view.showMessage(true,"Please select a row from booking table!");
            }
        }
             
        }
    
    class RemoveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(view.getTable().getSelectedRowCount()>0) {
                
                ArrayList<Object> list = getSelectedRow(view.getTable());
                //double check deletion process
                int choice = JOptionPane.showConfirmDialog(view, "Are you sure you want to remove this Booking?");

                if((choice==JOptionPane.NO_OPTION)||(choice==JOptionPane.CANCEL_OPTION))
                    return; 
                try {
                    Integer bookingID = (Integer)list.get(1);
                    //Constructor with only arg id
                    DiagRep booking = new DiagRep(bookingID);
                    model.remove(booking);
                    refresh();
                    view.showMessage(false,"Booking removed!");
                } catch (Exception ex) {
                    view.showMessage(true,"Booking not removed!\n");
                }
                   
            } else {
                view.showMessage(true,"Please select a row!");
            }
        }
    }
    
    
    class DurationListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(view.getTable().getSelectedRowCount()>0) {
                
                ArrayList<Object> list = getSelectedRow(view.getTable());
                //double check deletion process
                try{
                    int duration = Integer.parseInt(JOptionPane.showInputDialog(view,"Enter number of hours worked: "));
               
                    Integer bookingID = (Integer)list.get(1);
                    //Constructor with only arg id
                    DiagRep booking = new DiagRep(bookingID);
                    model.addCost(duration, bookingID);
                    refresh();
                    view.showMessage(false,"Duration added to Total Cost!");
                } catch (NumberFormatException | SQLException ex) {
                    view.showMessage(true,"Duration not valid!\n");
                }
                
            } else {
                view.showMessage(true,"Please select a row!");
            }
        }
    }
        
       
    
    class UpdateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(view.getTable().getSelectedRowCount()>0) {
                ArrayList<Object> list = getSelectedRow(view.getTable());
                //to resolve time bugs, reference bug solution from mafz
                  Integer bookingId = Integer.parseInt(list.get(1).toString());
                 Date startDate = Date.valueOf(list.get(4).toString());
                Time startTime = Time.valueOf(list.get(5).toString()+":00");
                Date endDate = Date.valueOf(list.get(6).toString());
                Time endTime = Time.valueOf(list.get(7).toString()+":00");
                
                // Create & Init booking update
                BookingView bookingView = new BookingView(view.getParentView(), true);
                BookingController bookingController = new BookingController(bookingView,model,bookingId,startDate,startTime,endDate,endTime);
                bookingView.setVisible(true);
                
            }
            else {
                view.showMessage(true,"Please select a row!");
            }
            refresh();
        }
    }
    
   
    static ArrayList<Object> getSelectedRow(JTable table) {
        //get selected row number
        int row = table.getSelectedRow();
        // column count
        int columns = table.getModel().getColumnCount();
        ArrayList<Object> list = new ArrayList<>();
        
        for(int i=0; i<columns; i++) {
            list.add(table.getValueAt(row, i));
        }
        
        return list;
    }
    
}
