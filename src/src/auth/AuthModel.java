/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auth;


import static auth.AuthHelpers.hashFunction;
import common.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JLabel;
import settings.UsersController;

public class AuthModel extends java.util.Observable {
	private static AuthModel instance;
	private boolean[] privileges;
	private boolean loggedIn;
	private String currentUser;

	private AuthModel() {
		currentUser = "Logged Out";
		loggedIn = false;
		privileges = new boolean[6];
		UsersController c = new UsersController(new JLabel());
		if(c.addDefault())System.out.println("Created new admin user. Password: password");
	}
	
	public static AuthModel getInstance() {
		if (instance == null) {
			instance = new AuthModel();
		} 
		return instance;
	}
	
	// Checks if user exists in the database and updates permissions.
	public void login(String username, String password) {
		try{	
			// Query database based on username.
			ResultSet rs = Database.executeQuery("SELECT * FROM"
					+ " users WHERE username = '" + username + "'");
			
			// Throw exception if no such user exists
			if (!rs.isBeforeFirst()) {
				throw new LoginFailedException("Invalid Username");
			}
			String salt = rs.getString("salt");
			String hash = hashFunction(password + salt);
			
			// If password is correct set attributes.
			// Else throw exception saying password is incorrect
			if (hash.equals(rs.getString("password"))) {
				// Set all relevant user details.
				
				currentUser = rs.getString("username");
				privileges[0] = rs.getBoolean("customerprivileges");
				privileges[1] = rs.getBoolean("vehicleprivileges");
				privileges[2] = rs.getBoolean("darprivileges");
				privileges[3] = rs.getBoolean("partprivileges");
				privileges[4] = rs.getBoolean("schedmaintprivileges");
				privileges[5] = rs.getBoolean("usersprivileges");
				loggedIn = true;
				
				// Tell observers that the login has been updated.
				changed();
			} else {
				throw new LoginFailedException("Incorrect Password");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			// Throw exception if sql cant run/cant connect to database
			// This shouldnt ever happen.
			throw new LoginFailedException("Database Error.");
		}
	}
	
	// Logout clears everything.
	public void logout() {
		currentUser = "Logged Out";
		loggedIn = false;
		privileges = new boolean[6];
		changed();
	}
	
	private void changed() {
		setChanged();
		notifyObservers();
	}
	
	// GETTERS FOR EVERYTHING
	public String getUsername() {
		return currentUser;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}
	
	public boolean[] getPrivileges() {
		return privileges;
	}
}
