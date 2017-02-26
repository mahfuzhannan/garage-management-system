//Author: Stefan Ionascu
//Module: Vehicle Records
//Date: 27/03/2014

//SimpleService represents a simple version of a service with only 3 attributes
//to be used to present the information about a service.
package vehicle;
import java.util.Date;
import javax.swing.JOptionPane;
public class SimpleService
{
	private double mileage;
	Date date;
	double totalCost;

	public SimpleService(double mileage,Date date,double totalCost)
	{
		this.mileage=mileage;
		this.date=date;
		this.totalCost=totalCost;
	}
	public double getMileage()
	{
		return mileage;
	}
	public Date getDate()
	{
		return date;
	}
	public double getCost()
	{
		return totalCost;
	}
}