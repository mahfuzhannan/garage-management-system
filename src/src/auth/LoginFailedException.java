/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auth;

/**
 *
 * @author George
 */
public class LoginFailedException extends RuntimeException {
	public LoginFailedException(String message) {
		super("Login Failed: " + message);
	}
}
