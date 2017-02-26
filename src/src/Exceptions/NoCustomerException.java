/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceptions;

/**
 *
 * @author mafz
 */
public class NoCustomerException extends RuntimeException {

    public NoCustomerException() {
        super("No Customers registered.\nPlease register a Customer first.");
    }

    
    
}
