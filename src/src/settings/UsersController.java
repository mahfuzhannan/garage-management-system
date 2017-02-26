/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package settings;

import static auth.AuthHelpers.hashFunction;
import java.security.SecureRandom;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

/**
 *
 * @author George
 */
public class UsersController {
	
	private JComponent parent;
	
	public UsersController(JComponent parent) {
		this.parent = parent;
	}
	public boolean add(String username, String password, String password2, boolean[] privs) {
		if (!validate(username, password, password2)) return false;
		
		// create salt to add to password before hashing.
		SecureRandom random = new SecureRandom();
		String salt = String.valueOf(random.nextInt(99999999));
		
		// Add salt to password and run hash function
		String hash = hashFunction(password + salt);
		
		// Pass off to model
		UsersModel.getInstance().add(username, hash, salt, privs);
		
		return true;
	}

	
	private boolean validate(String username, String password, String password2) {
		if (username.equals("")) {
			JOptionPane.showMessageDialog(parent, "Please enter a username");
			return false;
		}
		
		if(UsersModel.getInstance().userExists(username)) {
			JOptionPane.showMessageDialog(parent, "Username already exists");
			return false;
		}
		
		if (password.equals("")) {
			JOptionPane.showMessageDialog(parent, "Please enter a password");
			return false;
		}
		
		if (password.length() < 5) {
			JOptionPane.showMessageDialog(parent, "Password must be at least 5 characters");
			return false;
		}
		if (!password.equals(password2)) {
			JOptionPane.showMessageDialog(parent, "Passwords must match");
			return false;
		}
		
		return true;
	}
	
	public boolean addDefault () {
		if(UsersModel.getInstance().userExists("admin")) return false;
		
		// create salt to add to password before hashing.
		SecureRandom random = new SecureRandom();
		String salt = String.valueOf(random.nextInt(99999999));
		
		// Add salt to password and run hash function
		String hash = hashFunction("password" + salt);
		
		// Pass off to model
		UsersModel.getInstance().add("admin", hash, salt, new boolean[] {true,true,true,true,true,true});
		
		return true;
	}

	boolean changePass(int id, String pass1, String pass2) {
		if (pass1.equals("")) {
			JOptionPane.showMessageDialog(parent, "Please enter a password");
			return false;
		}
		
		if (pass1.length() < 5) {
			JOptionPane.showMessageDialog(parent, "Password must be at least 5 characters");
			return false;
		}
		if (!pass1.equals(pass2)) {
			JOptionPane.showMessageDialog(parent, "Passwords must match");
			return false;
		}
		
		// create salt to add to password before hashing.
		SecureRandom random = new SecureRandom();
		String salt = String.valueOf(random.nextInt(99999999));
		
		// Add salt to password and run hash function
		String hash = hashFunction(pass1 + salt);
		
		UsersModel.getInstance().changePass(id, hash, salt);
		
		return true;
	}
}
