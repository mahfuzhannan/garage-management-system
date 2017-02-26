/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author George
 */
public abstract class GMTableModel extends AbstractTableModel {
	// Backend data
	private int rowCount;
	private final int colCount;
	private String populateQuery, table, countQuery;
	private Object[][] data;
	private final String[] colNames;
	
	public GMTableModel(String table, String populateQuery, String[] colNames) {
		this.colNames = colNames;
		colCount = colNames.length;
		this.populateQuery = populateQuery;
		this.table = table;
		countQuery = null;
		if(!populateQuery.equals(""))
			populateData();
	}
	
	public GMTableModel(String table, String populateQuery, String[] colNames, String customCountQuery) {
		this.colNames = colNames;
		colCount = colNames.length;
		this.populateQuery = populateQuery;
		this.table = table;
		countQuery = customCountQuery;
		populateData();
	}
	
	protected void changed() {
		populateData();
		fireTableDataChanged();
	}
	
	public void triggerChanged() {
		changed();
	}
	
	// Populate data arrays.
	protected void populateData() {
		try {
			// Get row count
			String query;
		if(countQuery == null) query = "SELECT COUNT(*) FROM " + table + ";";
		else query = countQuery;
			ResultSet rs = Database.executeQuery(query);
			rs.next();
			rowCount = rs.getInt(1);
			data = new Object[colCount][rowCount];
			// Get data
			rs = Database.executeQuery(populateQuery);
                        try{
			for (int i = 0; rs.next(); i++) {
				for (int j = 0; j < colCount; j++) {
					data[j][i] = rs.getObject(j + 1);
				}
			}
                        }
                        catch(ArrayIndexOutOfBoundsException e)
                        {
                            JOptionPane.showMessageDialog(null,e.getMessage());
                        }
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			JOptionPane.showMessageDialog(null,e.getMessage());
		}
	}

	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;		
	}
	
	@Override
	public int getColumnCount() {
		return colCount;
	}
	
	@Override
	public String getColumnName(int columnIndex) { 
		return colNames[columnIndex];
	}
	
	@Override
	public int getRowCount() {
		return rowCount;
	}
	
	// Get the value at the specified xy coordinate
	@Override
	public Object getValueAt(int row, int col) {
		return data[col][row];
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int colIndex) {
		return false;
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int colIndex) {
		
	}

	public void setQuery(String query,String query2)
	{
                
		populateQuery = query;
		countQuery = query2;
	}
}
