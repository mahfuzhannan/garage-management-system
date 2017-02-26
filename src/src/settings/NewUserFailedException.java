/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package settings;

/**
 *
 * @author George
 */
public class NewUserFailedException extends RuntimeException {
	public NewUserFailedException(String message) {
		super("Creating New User Failed: " + message);
	}
}
