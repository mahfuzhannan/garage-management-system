//Author: Stefan Ionascu
//Module: Vehicle Records
//Date: 27/03/2014

//Address represents a real address associated with a warranty(has less
//attributes than a customer address)
package vehicle;
public class Address 
{
    private String line1;
    private String city;
    private String postcode;
    
    public Address(String line1,String city,String postcode)
    {
        this.line1=line1;
        this.city=city;
        this.postcode=postcode;
    }
    public String getLine1()
    {
        return line1;
    }
    public void setLine1(String line1)
    {
        this.line1=line1;
    }
    public String getCity()
    {
        return city;
    }
    public void setCity(String city)
    {
        this.city=city;
    }
    public String getPostcode()
    {
        return postcode;
    }
    public void setPostcode(String postcode)
    {
        this.postcode=postcode;
    }
}
