/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author George
 */
public class AuthHelpers {
	// Hash Function to store passwords securely in the database.
	public static String hashFunction(String s) {
		// Get bytes from string.
		byte[] byteData = s.getBytes();
		
		// Hash 1000 times.
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			for (int i = 0; i < 1000; i++) {
				byteData = md.digest(byteData);
			}
		} catch (NoSuchAlgorithmException e) {
			System.err.println(e.getMessage());
		}
		
		// Convert back to a hex String.
		// Why does java not have a method for this?
		final char[] hexArray = "0123456789ABCDEF".toCharArray();
		char[] hexChars = new char[byteData.length * 2];
		for ( int j = 0; j < byteData.length; j++ ) {
			int v = byteData[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
}
