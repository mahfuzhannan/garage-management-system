//Author: Stefan Ionascu
//Module: Vehicle Records
//Date: 27/03/2014

//VehicleController has listeners to be used when a user press a button on the 
//main view.
package vehicle;

import assets.GMTable;
import common.Database;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class VehicleController{
	private VehicleModel model;
	private VehicleView view;

	public VehicleController(VehicleView view){
		model = VehicleModel.getInstance(false);
		this.view = view;

		addListeners();
	}
        
	private void addListeners()
	{
		view.addAddVehicleListener(new AddVehicleListener());
		view.lastServiceAddVehicleListener(new LastServiceListener());
		view.removeAddVehicleListener(new RemoveVehicleListener());
                view.updateAddVehicleListener(new UpdateVehicleListener());
                view.searchAddVehicleListener(new SearchListener());
	}
        
	public class SearchListener implements ActionListener
	{
        @Override
		public void actionPerformed(ActionEvent e)
		{
                    JTable table=view.getMainTable();
                    String searchReg = JOptionPane.showInputDialog("Type the registration number to search:");
                    TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(table.getModel());
                    table.setRowSorter(rowSorter);
                    try
                    {
                        if (searchReg.trim().equals("")) 
                        {
                            rowSorter.setRowFilter(null);
                        } 
                        else 
                        {
                            rowSorter.setRowFilter(RowFilter.regexFilter("^" + searchReg+"$"));
                            int rows = table.getRowCount();
                            if(rows==0)
                            {
                                JOptionPane.showMessageDialog(null,"No records matching: "+searchReg);
                            }
                        }
                    }
                    catch(NullPointerException ex)//cancel button
                    {
                        rowSorter.setRowFilter(null);
                    }
	}
    }
        
	public class LastServiceListener implements ActionListener
	{
        @Override
		public void actionPerformed(ActionEvent e)
		{
                    JTable table = view.getMainTable();
                    int row = view.getMainTable().getSelectedRow();
                    if(row==-1)
                    {
	        	JOptionPane.showMessageDialog(null,"Please select a vehicle.");
                        return;
                    }
                    int id=(int)table.getValueAt(row,0);
                    String reg = (String)table.getValueAt(row,2);
                    try
                    {
                        SimpleService service = model.getLastService(id);
                        if(service!=null)//there is at least service, not new vehicle
                        {
                           new LastServiceDialog(service,reg); 
                        }
                    }
                    catch(SQLException ex)
                    {
                        JOptionPane.showMessageDialog(null, "Cannot get last service detals.\nIf this is a new vehicle, it does not have any services.");
                    }
                    catch (ParseException ex) 
                    {}
            }
	}
	public class AddVehicleListener implements ActionListener
	{
        @Override
		public void actionPerformed(ActionEvent e)
		{
                    if(model.getCount("\"Customer Accounts\"")==0)
                    {
			JOptionPane.showMessageDialog(null,"No customers registered.Please register a customer first.");
                    }
                    else
                    {
                        new NewVehicleDialog("Add Vehicle",model);
                        GMTable mainTable = (GMTable) view.getMainTable();
                        mainTable.updateTable();
                    }
        }
	}

    class RemoveVehicleListener implements ActionListener
    {
		public void actionPerformed(ActionEvent e)
		{
	        
                    javax.swing.JTable table = view.getMainTable();
                    int selectedRow = table.getSelectedRow();
                    if(selectedRow==-1)
                    {
                            JOptionPane.showMessageDialog(null,"Please select a row to remove.");
                    }
                    else
                    {
                            int id = (int)table.getValueAt(selectedRow,0);
                            //confirm if user wants to delete
                            int input = JOptionPane.showConfirmDialog(view,"Are you sure you want to delete the vehicle with id:"+id+"?");
                            if(input==0)
                            {
                            //update database
                                if(!model.remove(id))

                                    JOptionPane.showMessageDialog(null,"Could not remove.");
                            }
                    }
		}
	}

    class UpdateVehicleListener implements ActionListener{
		public void actionPerformed(ActionEvent e)
		{
                    javax.swing.JTable table = view.getMainTable();
                    int selectedRow = table.getSelectedRow();
                    if(selectedRow==-1)
                    {
                            JOptionPane.showMessageDialog(null,"Please select a row to update.");
                    }
                    else
                    {
                            //get vehicle details
                            Vehicle vehicle= view.getVehicle(selectedRow);

                            NewVehicleDialog vehDialog=new NewVehicleDialog("Edit",vehicle,model);
                    }
		}
    }
    
}