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
public class NoVehicleException extends RuntimeException{

    public NoVehicleException(String customer) {
        super("No Vehicles registered for " + customer +".\nPlease add a Vehicle.");
    }
    
}
