/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package settings;

import common.Database;
import common.GMTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author George
 */
public class UsersModel extends GMTableModel {
	public static enum Modules {
		CUSTOMER, VEHICLE, DAR, PART, MAINT, SCHEDMAINT, USER
	}
	private static UsersModel instance;
	private static final String[] colNames = {"User ID", "Username", "Customers" , "Vehicles", "Parts", "Scheduled Maintenance", "Diagnostics", "Users"};
	private static final String table = "users";
	private static final String populateQuery = "SELECT userid, username, customerprivileges, vehicleprivileges, partprivileges, schedmaintprivileges, darprivileges, usersprivileges FROM users";
	
	private UsersModel() {
		super(table, populateQuery, colNames);
	}
	
	public static UsersModel getInstance() {
		if(instance == null) {
			instance = new UsersModel();
		}
		return instance;
	}

	// Creates a new user in the database. Returns false on an error.
    public void add(String username, String hash, String salt,
			boolean[] privs) {
		// Build query string
		String query = "INSERT INTO users VALUES (null, '" + username + "', '"
				+ hash + "', '" + salt + "'";
		for (int i = 0; i < privs.length; i++) {
			query += ", ";
			if (privs[i]) query += "1";
			else query += "0";
		}
		query += ",'grey')";
		
		// Insert into db
		try {
			Database.executeUpdate(query);
			changed();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			throw new NewUserFailedException("Database error: " + e.getMessage());
		}
	}
	
	//Checks if the user exists in the database.
	public boolean userExists(String username) {
		String query = "SELECT * FROM users WHERE username = '" + username + "'";
		try {
			ResultSet rs = Database.executeQuery(query);
			return rs.isBeforeFirst();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
		
	}
	
	
	// Delete a user from the database.
	public boolean remove(int id) {
		// Build query string
		String query = "DELETE FROM users WHERE userid = " + id;
		
                if(id==1) {
                    JOptionPane.showMessageDialog(null, "Cannot remove Admin", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }
		//Exec query
		try {
			Database.executeUpdate(query);
			changed();
			return true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
		
	}
	
	// Change a users password
	public boolean changePass(int id, String hash, String salt) {
		// Build query string
		String query = "UPDATE users SET password = '" + hash + "', salt = '" +salt + "' WHERE userid = " + id;
		
		//Exec query
		try {
			Database.executeUpdate(query);
			changed();
			return true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
	}
	
	// Change a users privs
	public boolean changePrivs(int id, int[] privs) {
		// Build query string
		String query = "UPDATE users SET "
				+ "customerprivileges = '" + privs[0] + "',"
				+ "vehicleprivileges = '" + privs[1] + "',"
				+ "darprivileges = '" + privs[2] + "',"
				+ "partprivileges = '" + privs[3] + "',"
				+ "schedmaintprivileges = '" + privs[4] + "',"
				+ "usersprivileges = '" + privs[5] + "'"
				+ "WHERE userid = '" + id + "' ";
		//Exec query
		try {
			Database.executeUpdate(query);
			changed();
			return true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
	}
		
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch(columnIndex) {
			case 0: return int.class;
			case 1: return String.class;
			default: return Boolean.class;
		}
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		if (col > 1) {
			if ((int)super.getValueAt(row, col) == 1) return Boolean.TRUE;
			else return Boolean.FALSE;
		} else return super.getValueAt(row, col);
	}
}
