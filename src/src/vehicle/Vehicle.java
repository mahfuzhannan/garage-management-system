//Author: Stefan Ionascu
//Module: Vehicle Records
//Date: 27/03/2014

//Vehicle represents a vehicle, used to insert a vehicle into the database,
package vehicle;

import common.Database;
import customer.Logic.Customer;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;

public final class Vehicle {
	private int id,warranty,customerID;
	private String type;
	private Warranty warrantyCompany;
	private String model;
	private String make;
	private double engineSize;
	private String fuel;
	private String colour;
	private Date motRenewalDate;
	private Date dateLastService;
	private Customer customer;
	private String regNumber;

	//for updating a vehicle
	public Vehicle(int vehicleID,String reg,String color,Date mot,int warranty)
	{
		id = vehicleID;
		regNumber = reg;
		colour=color;
		motRenewalDate=mot;
		this.warranty=warranty;

	}
	public Vehicle(String vehString)
	{
		List<String> states = Arrays.asList(vehString.split(","));
		type = states.get(0);
		make = states.get(1);
		model = states.get(2);
		fuel = states.get(3);
		engineSize = Double.parseDouble(states.get(4));
	}
	public Vehicle(int id,String reg,String carType,int warranty,int customer,String model,String make,double engineSize,String fuel,String colour,Date motRenewalDate) {
		this.id=id;
		regNumber = reg;
		this.type = carType;
		this.warranty = warranty;
                this.customerID = customer;
		this.model = model;
		this.make = make;
		this.engineSize = engineSize;
		this.fuel = fuel;
		this.colour = colour;
		this.motRenewalDate = motRenewalDate;
	}
	//pre-configured vehicles
	public Vehicle(String type, String model, String make, double engineSize, String fuel){
		this.type = type;
		this.model = model;
		this.make = make;
		this.engineSize = engineSize;
		this.fuel = fuel;
	}
        
   public Date getMotRenewalDate()
   {
               return motRenewalDate;
   }
    public int getCustomerId()
    {
    	return customerID;
    }    
        
    public int getId() 
    {
        return id;
    }
    
    public String getType() 
    {
	return type;
    }

	public void setType(String type) {
		this.type = type;
	}

	public Warranty getWarrantyCompany() {
		return this.warrantyCompany;
	}

	public void setWarrantyCompany(Warranty warrantyCompany) {
		this.warrantyCompany = warrantyCompany;
	}

	public String getModel() {
		return this.model;
	}

	/**
	 * 
	 * @param model
	 */
	public void setModel(String model) {
		this.model = model;
	}

	public String getMake() {
		return this.make;
	}

	/**
	 * 
	 * @param make
	 */
	public void setMake(String make) {
		this.make = make;
	}

	public double getEngineSize() {
		return this.engineSize;
	}

	/**
	 * 
	 * @param engineSize
	 */
	public void setEngineSize(double engineSize) {
		this.engineSize = engineSize;
	}

	public String getFuel() {
		return this.fuel;
	}


	public void setFuel(String fuel) {
		this.fuel = fuel;
	}

	public String getColour() {
		return this.colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public String getRegNumber() {
		return this.regNumber;
	}

	public void setRegNumber(String regNumber) {
		this.regNumber = regNumber;
	}
	//for combo box used in vehicledialog
	public String toString()
	{
		return type+","+make+","+model+","+fuel+","+engineSize;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public int getWarranty()
	{
		return warranty;
	}

}
