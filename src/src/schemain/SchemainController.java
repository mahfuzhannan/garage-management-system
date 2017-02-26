/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package schemain;

import assets.GMTable;
import booking.Booking;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import schemain.mot.Mot;
import schemain.mot.MotCompleteDialog;
import schemain.mot.MotController;
import schemain.mot.MotModel;
import schemain.mot.MotView;
import schemain.service.Service;
import schemain.service.ServiceController;
import schemain.service.ServiceModel;
import schemain.service.ServiceView;

/**
 * Handles communication between SchemainView and SchemainModel.
 * @author Mahfuz
 */
public class SchemainController {
    private final SchemainView view;
    private final SchemainModel model;
    private final ServiceModel serviceModel;
    private final MotModel motModel;
    private SearchSchemain searchSchemain;

    public SchemainController(SchemainView view, SchemainModel model, ServiceModel serviceModel, MotModel motModel) {
        this.view = view;
        this.model = model;
        this.serviceModel = serviceModel;
        this.motModel = motModel;
        init();
    }
    
    public final void init() {
        // set event listeners
        view.addAddServiceBtnListener(new AddServiceListener());
        view.addAddMotBtnListener(new AddMotListener());
        view.addUpdateBtnListener(new UpdateListener());
        view.addRemoveBtnListener(new RemoveListener());
        view.addSearchBtnListener(new SearchListener());
        view.addMotTableListener(new MotTableListener());
        searchSchemain = new SearchSchemain(view.getMainView(), true, model);
        refreshTable();
    }
    
    /**
     * Resets the values of the table from the Database, This should be called after
     * any changes to the Database have been made.
     */
    public void refreshTable() {
        try {
            view.getTable().setModel(model.getTableModel());
            view.getServiceTable().setModel(model.getServiceTableModel());
            view.getMotTable().setModel(model.getMotTableModel());
            //view.showMessage("Refreshed!");
            view.updateTable();
        } catch (SQLException ex) {
            view.showErrorMessage(ex.getMessage());
        }
    }
    
    /**
     * Handles the <code>ActionListener</code> when Add Service is pressed.
     */
    public class AddServiceListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ServiceView serviceView = new ServiceView(view.getMainView(), true, false);
            ServiceController serviceController = new ServiceController(serviceModel, serviceView);
            serviceView.setVisible(true);
            refreshTable();
        }
    }
    
    /**
     * Handles the <code>ActionListener</code> when Add Mot is pressed.
     */
    public class AddMotListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            MotView motView = new MotView(view.getMainView(), true, false);
            MotController motController = new MotController(motModel, motView);
            motView.setVisible(true);
            refreshTable();
        }
    }
    
    /**
     * Handles the <code>ActionListener</code> when Remove is pressed.
     */
    public class RemoveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // check if row is selected
            if(view.getTable().isRowSelected(view.getTable().getSelectedRow())) {
                // Get an arraylist of row values
                ArrayList<Object> list = model.getTableRowAsArrayList(view.getTable());
                
                if(((String)list.get(2)).equals("SERVICE")) {
                    removeService(list);
                } else { 
                    removeMot(list);
                }
            } else {
                view.showMessage("Please select a row!");
            }
            refreshTable();
        }
        
        /**
         * Removes a Service.
         * @param list - <code>ArrayList<Object></code> should be retrieved from the main table in SchemainView.
         */
        private void removeService(ArrayList<Object> list) {
            // Check if the user wants to delete the database entry
            int choice = JOptionPane.showConfirmDialog(view, "Are you sure you want to remove this Service?");

            if(choice==JOptionPane.NO_OPTION || choice==JOptionPane.CANCEL_OPTION)
                return; // if no is pressed return to main view

            // else remove booking
            try {
                // only set Booking ID and Schemain ID to remove as these are unique for all bookings
                int bookingID = (Integer)list.get(1);
                Booking booking = new Service(bookingID, model.getSchemainID(bookingID), null, null, null, null, null, null, null, null, null, null, null, false, null, null, null, null);
                serviceModel.remove((Service)booking);
                view.showMessage("Service removed!");
            } catch (Exception ex) {
                view.showErrorMessage("Service not removed!\n"+ex.getMessage());
            }
        }
        
        /**
         * Removes an Mot.
         * @param list - <code>ArrayList<Object></code> should be retrieved from the main table in SchemainView.
         */
        private void removeMot(ArrayList<Object> list) {
            // Check if the user wants to delete the database entry
            int choice = JOptionPane.showConfirmDialog(view, "Are you sure you want to remove this Mot?");
            
            if(choice==JOptionPane.NO_OPTION || choice==JOptionPane.CANCEL_OPTION)
                return;
            
            // else remove booking
            try {
                // only set Booking ID and Schemain ID to remove as these are unique for all bookings
                int bookingID = (Integer)list.get(1);
                Booking booking = new Mot(bookingID, model.getSchemainID(bookingID), null, null, null, null, null, null, null, null, null, null, null, false, null, null, null, null);
                motModel.remove((Mot)booking);
                view.showMessage("Mot removed!");
            } catch (Exception ex) {
                view.showErrorMessage("Mot not removed!\n"+ex.getMessage());
            }
        }
    }
    
    /**
     * Handles the <code>ActionListener</code> when Update is pressed.
     */
    public class UpdateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(view.getTable().isRowSelected(view.getTable().getSelectedRow())) {
                // Get an arraylist of row values
                ArrayList<Object> list = model.getTableRowAsArrayList(view.getTable());
                
                // get values of the selected rows; used to create a Booking object
                Date startDate = Date.valueOf(list.get(5).toString());
                Time startTime = Time.valueOf(list.get(6).toString()+":00");
                Date endDate = startDate;
                Time endTime = Time.valueOf(list.get(7).toString()+":00");
                Integer bay = Integer.parseInt(list.get(8).toString());
                String[] split = list.get(0).toString().split(":");
                Integer customerId = Integer.parseInt(split[0]);
                
                // Update for a Service
                if(((String)list.get(2)).equals("SERVICE")) {
                    //JOptionPane.showMessageDialog(view, "SERVICE!");
                    ServiceView serviceView = new ServiceView(view.getMainView(), true, true);
                    ServiceController serviceController = new ServiceController(serviceModel, serviceView);
                    // set key previous details to be used to create an Mot object
                    serviceController.setServiceUpdate(startDate, startTime, endDate, endTime, bay, customerId);
                    serviceView.setVisible(true);
                } 
                
                // else Update for an MOT
                else { 
                    //JOptionPane.showMessageDialog(view, "MOT!");
                    MotView motView = new MotView(view.getMainView(), true, true);
                    MotController motController = new MotController(motModel, motView);
                    // set key previous details to be used to create an Mot object
                    motController.setMotUpdate(startDate, startTime, endDate, endTime, bay, customerId);
                    motView.setVisible(true);
                }
            }
            
            // if no row has been selected
            else {
                view.showMessage("Please select a row!");
            }
            refreshTable();
        }
    }
    
    /**
     * Handles <code>MouseListener</code> for when Mot Table row is double clicked;
     * displays and Mot Completed Dialog where the user selects option to PASS or FAIL Mot.
     */
    public class MotTableListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {        }

        @Override
        public void mousePressed(MouseEvent e) {
            GMTable table = view.getMotTable();
            ArrayList<Object> list = model.getTableRowAsArrayList(table);
            Time startTime = Time.valueOf(list.get(2)+":00");
            Time endTime = Time.valueOf(list.get(3)+":00");
            java.util.Date utilDate = new java.util.Date();
            Date date = new Date(utilDate.getTime());
            Integer bay = Integer.parseInt(list.get(6).toString());
            //Integer customerId = In
            if(e.getClickCount()==2) {
                try {
                    MotCompleteDialog mcd = new MotCompleteDialog(view.getMainView(), true, new MotModel().getMot(date, startTime, date, endTime, bay, null));
                    mcd.setVisible(true);
                } catch (SQLException ex) {
                    view.showErrorMessage("Error: no booking available\n"+ex.getMessage());
                    ex.printStackTrace();
                }
            }
            refreshTable();
        }

        @Override
        public void mouseReleased(MouseEvent e) {        }
        @Override
        public void mouseEntered(MouseEvent e) {        }
        @Override
        public void mouseExited(MouseEvent e) {        }
        
    }
    
    public class SearchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            searchSchemain.setLocationRelativeTo(null);
            searchSchemain.setVisible(true);
            refreshTable();
        }
        
    }
}
