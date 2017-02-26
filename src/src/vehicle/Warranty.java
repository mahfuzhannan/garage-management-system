//Author: Stefan Ionascu
//Date: 22/02/2015
package vehicle;

import java.util.Date;

public class Warranty 
{
    private String name;
    private Address address;
    private Date expiryDate;
    private int id;

public Warranty(String name, Address address,Date date)
{
    this.name=name;
    this.address=address;
    this.expiryDate=date;
}
public Warranty(int id,String name,Address address,Date date)
{
    this.name=name;
    this.address=address;
    this.expiryDate=date;
    this.id=id;
}

public int getId()
{
    return id;
}
public String getName()
{
    return name;
}
public void setName(String name)
{
    this.name=name;
}
public Address getAddress()
{
    return address;
}
public void setAddress(Address address)
{
    this.address=address;
}
public Date getExpiry()
{
    return expiryDate;
}
public void setExpiry(Date date)
{
    expiryDate = date;
}
}