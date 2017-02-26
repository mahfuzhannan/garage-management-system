package parts;

import assets.GMButton;
import assets.Res;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 * The main part view. Contains 3 tables for suppliers/part stock/part orders
 * @author George
 */


public class PartMainView extends JPanel {
	
	PartModel model;
	
	JTable table, suppliersTable, ordersTable;
	GMButton newPart, editPart, orderPart, deletePart,
			newSupplier, editSupplier, deleteSupplier,
			fulfillOrder, editOrder, deleteOrder;
	
	
	public PartMainView() {
		super(new BorderLayout(10, 10));
		
		// Set up model.
		model = PartModel.getInstance();
		
		// Set up buttons
		newPart = new GMButton("Add");
		newPart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new NewPartDialog();
			}
		});
		editPart = new GMButton("Edit");
		editPart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(partIsSelected()){
					int row = table.getSelectedRow();
					int id = (int) table.getValueAt(row, 0);
					String name = (String) table.getValueAt(row, 1);
					String desc = (String) table.getValueAt(row, 2);
					int stock = (int) table.getValueAt(row, 3);
					double price = (double) table.getValueAt(row, 4);
					Part part = new Part(id, name, desc, stock, (float)price);
					new NewPartDialog(part);
				}
			}
		});
		orderPart = new GMButton("Order More");
		orderPart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (partIsSelected())
					if (suppliersTable.getRowCount() == 0) JOptionPane.showMessageDialog(null, "No Suppliers to order from. Please add a supplier");
					else new NewOrderDialog((int)table.getValueAt(table.getSelectedRow(), 0));
			}
		});
		
		
		deletePart = new GMButton("Remove");
		deletePart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(partIsSelected()){
					model.remove((int)table.getValueAt(table.getSelectedRow(), 0));
				}
			}
		});
		
		GMButton viewInstalled = new GMButton("Installed Parts");
		viewInstalled.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new InstalledPartsView();
			}
		});
		
		// Main Button panel
        JPanel btnPanel = new GMPanel();
		btnPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        btnPanel.setLayout(new GridLayout(0, 5, 5, 5));
		btnPanel.add(newPart);
		btnPanel.add(editPart);
		btnPanel.add(orderPart);
		btnPanel.add(deletePart);
		btnPanel.add(viewInstalled);
		
		
		// Create main table.
		table = new JTable(model);
		table.setSelectionBackground(Res.BKG_COLOR);
		table.setSelectionForeground(Res.BTN_FONT_COLOR);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
		JPanel mainTablePanel = new GMPanel(new BorderLayout());
        mainTablePanel.setBorder(new TitledBorder(new EtchedBorder(), "Parts Stock", 1, TitledBorder.CENTER, null, Res.FONT_COLOR));
        mainTablePanel.add(scrollPane, BorderLayout.CENTER);
		mainTablePanel.add(btnPanel, BorderLayout.NORTH);
		
		// Create suppliers table
        suppliersTable = new JTable(SupplierModel.getInstance());
        JScrollPane supplierPane = new JScrollPane(suppliersTable);
        JPanel supplierPanel = new GMPanel(new BorderLayout());
        supplierPanel.setBorder(new TitledBorder(new EtchedBorder(), "Suppliers", 1, TitledBorder.CENTER, null, Res.FONT_COLOR));
        supplierPanel.add(supplierPane,BorderLayout.CENTER);
		
		//Suppliers Button Panel
		JPanel suppliersButtonPanel = new GMPanel(new GridLayout(1, 3, 5, 5));
		suppliersButtonPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
		newSupplier = new GMButton("Add");
		newSupplier.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new NewSupplierDialog();
			}
		});
		suppliersButtonPanel.add(newSupplier);
		editSupplier = new GMButton("Edit");
		suppliersButtonPanel.add(editSupplier);
		editSupplier.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(supplierIsSelected()){
					int row = suppliersTable.getSelectedRow();
					String oldName = (String) suppliersTable.getValueAt(row, 0);
					String tel = (String) suppliersTable.getValueAt(row, 1);
					new NewSupplierDialog(oldName, tel);
				} 
			}
		});
		deleteSupplier = new GMButton("Remove");
		suppliersButtonPanel.add(deleteSupplier);
		deleteSupplier.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(supplierIsSelected())
					SupplierModel.getInstance().remove((String)suppliersTable
							.getValueAt(suppliersTable.getSelectedRow(), 0));
			}
		});
		supplierPanel.add(suppliersButtonPanel, BorderLayout.NORTH);
		
		// Create orders table
        ordersTable = new JTable(OrderModel.getInstance());
        JScrollPane orderPane = new JScrollPane(ordersTable);
        JPanel orderPanel = new GMPanel(new BorderLayout());
        orderPanel.setBorder(
				new TitledBorder(
					new EtchedBorder(), "Current Orders", 1, 
						TitledBorder.CENTER, null, Res.FONT_COLOR)
		);
        orderPanel.add(orderPane, BorderLayout.CENTER);
		JPanel ordersButtonPanel = new GMPanel(new GridLayout(0, 3, 5, 5));
		ordersButtonPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
		
		fulfillOrder = new GMButton("Fulfill");
		fulfillOrder.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(orderIsSelected())
				OrderModel.getInstance().fulfill(OrderModel.getInstance()
						.getOrder((int)ordersTable
								.getValueAt(ordersTable.getSelectedRow(), 0)));
			}
		});
		ordersButtonPanel.add(fulfillOrder);
		
		editOrder = new GMButton("Edit");
		editOrder.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(orderIsSelected())
					new NewOrderDialog(OrderModel.getInstance()
						.getOrder((int)ordersTable
								.getValueAt(ordersTable.getSelectedRow(), 0)));
			}
		});
		ordersButtonPanel.add(editOrder);
		
		deleteOrder = new GMButton("Remove");
		deleteOrder.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(orderIsSelected())
				OrderModel.getInstance().remove((int)ordersTable
						.getValueAt(ordersTable.getSelectedRow(), 0));
				
			}
		});
		ordersButtonPanel.add(deleteOrder);
		orderPanel.add(ordersButtonPanel, BorderLayout.NORTH);
		
		// Panel for the three tables
		GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel mainPanel = new GMPanel(gridBagLayout);
        gbc.fill = GridBagConstraints.BOTH;
		
		gbc.gridheight = 2;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 3;
		gbc.weighty = 1;
		mainPanel.add(mainTablePanel,gbc);
		
		gbc.gridheight = 1;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		mainPanel.add(orderPanel,gbc);
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 1;
		mainPanel.add(supplierPanel,gbc);
		
		add(mainPanel, BorderLayout.CENTER);
		
		setBackground(Res.BKG_COLOR);
		
		
	}
	
	// These next few methods check to see if each of the tables has been
	// selected. Gives an error message if not to alert the user to select
	// something.
	
	private boolean orderIsSelected() {
		if (ordersTable.getSelectedRow()!=-1){
			return true;
		} else {
			JOptionPane.showMessageDialog(null, "Please select an order");
			return false;
		}
	}
	
	private boolean partIsSelected() {
		if (table.getSelectedRow()!=-1){
			return true;
		} else {
			JOptionPane.showMessageDialog(null, "Please select a part");
			return false;
		}
	}
	
	private boolean supplierIsSelected() {
		if (suppliersTable.getSelectedRow()!=-1){
			return true;
		} else {
			JOptionPane.showMessageDialog(null, "Please select a supplier");
			return false;
		}
	}
}
