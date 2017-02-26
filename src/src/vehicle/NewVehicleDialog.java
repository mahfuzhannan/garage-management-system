//Author: Stefan Ionascu
//Module: Vehicle Records
//Date: 27/03/2014

//NewVehicleDialog presents a dialog for the user to use in order to add a new 
//vehicle to the database or to edit a current one.
//It creates a NewWarrantyDialog to add/edit warranty details.

package vehicle;

import assets.GMButton;
import assets.GMFormattedTextField;
import assets.Res;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class NewVehicleDialog extends JDialog
{	
	private int customerSelected;
	private int warrantyId;
	private Vehicle returnVehicle;
	private	int vehicleID;
	private JPanel mainPanel;
	private JLabel warrantyLabel,warranty2Label,regLabel,typeLabel,modelLabel,makeLabel,engineLabel,fuelLabel,colourLabel,motLabel;
	private JComboBox preConfiguredVehicles,customersAvailable;
	private GMButton confirm,cancel,addWarranty;
	private GMFormattedTextField customerField,regField,typeField,modelField,makeField,engineField,fuelField,colourField,motField;
	private ArrayList<GMFormattedTextField> fields;//to iterate over and check if all fields are completed before trying to add a new vehicle
        private final VehicleModel model;
        private Vehicle vehicleToEdit;
        private NewVehicleDialog instance;
        private Warranty warranty;
    
//for editing a vehicle
public NewVehicleDialog(String title,Vehicle veh,VehicleModel model)
{
    instance = this;//to pass to NewWarrantyDialog, to set the warranty details
    vehicleID = veh.getId();
    this.model = model;
    vehicleToEdit=veh;
	setTitle(title);
        try
        {
            initPanel(false);
        }
        catch(SQLException e)
        {
            JOptionPane.showMessageDialog(null,"Problem with database.");
            dispose();
        }
	
}

//for adding a new vehicle
public NewVehicleDialog(String title,VehicleModel model)
{   
        instance = this;//to pass to NewWarrantyDialog, to set the warranty details
	this.model = model;
	setTitle(title);
        try
        {
            initPanel(true);
        }
        catch(SQLException e)
        {
            JOptionPane.showMessageDialog(null,"Problem with database.");
            dispose();
        }
}

private void initPanel(boolean isNew)throws SQLException
{
        setResizable(false);
	this.getContentPane().setBackground(Res.BKG_COLOR);
        setModal(true);
        
	//-------INITIALISE COMPONENTS-------------------
	mainPanel = new JPanel(new GridBagLayout()); 
	mainPanel.setBackground(Res.BKG_COLOR);
	add(mainPanel);
	GridBagConstraints c = new GridBagConstraints();
	c.fill=GridBagConstraints.HORIZONTAL;

	if(isNew)//add vehicle
	{
		//----DROP-DOWN PRE-CONFIGURED VEHICLES-------
		c.gridx=0;
		c.gridy=0;
		JLabel label = new JLabel("Select a vehicle:"); 
		label.setForeground(Res.FONT_COLOR);
		mainPanel.add(label,c);
		//add pre-configured vehicles
		Vehicle[] vehicles = generateVehicles();
		preConfiguredVehicles = new JComboBox(vehicles);
		c.gridx=1;
		//check when an item is selected
		preConfiguredVehicles.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) 
			{
					Object item = preConfiguredVehicles.getSelectedItem();
					vehicleToEdit=(Vehicle) item;
					init(item,false);
			}
		});
		mainPanel.add(preConfiguredVehicles,c);

		c.gridx=0;
		c.gridy=1;
		label = new JLabel("Select a customer:");
		label.setForeground(Res.FONT_COLOR);
		mainPanel.add(label,c);
		//get all customers from database
                    
                    int[] customersIds=model.getCustomerIds();
                    String[] firstNames=model.getCustomerNames(0);
                    String[] lastNames=model.getCustomerNames(1);
                
		//convert customer details to array of strings
		String[] customers = new String[firstNames.length];
		for(int i=0;i<firstNames.length;i++)
		{
			//each String will look like this:"1 Stefan Ionascu"
			customers[i] = customersIds[i]+" "+firstNames[i]+" "+lastNames[i];
		}
                
		//create dropdown to select customer
		customersAvailable =  new JComboBox(customers);
		c.gridx=1;
		//check when customer has been selected
		customersAvailable.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) 
			{
					Object item = customersAvailable.getSelectedItem();
					String customer = (String)item;
					String[] details = customer.split(" ");
					customerSelected = Integer.parseInt(details[0]);
			}
		});
		mainPanel.add(customersAvailable,c);


	}
	else//edit vehicle
	{
		c.gridx=0;
		c.gridy=0;
		JLabel label = new JLabel("<html><b>Edit a vehicle:</b></html>");
		label.setForeground(Res.FONT_COLOR);
		mainPanel.add(label,c);
		c.gridy=1;
		label = new JLabel("Customer:");
		label.setForeground(Res.FONT_COLOR);
		mainPanel.add(label,c);
		c.gridx=1;
		customerField = new GMFormattedTextField();
		customerField.setText(""+vehicleToEdit.getCustomerId());
		customerField.setEditable(false);
		mainPanel.add(customerField,c);
	}

	fields = new ArrayList<GMFormattedTextField>();
	//Vehicle details
	c.gridx=0;
	c.gridy=2;
	regLabel = new JLabel("Reg:");
	regLabel.setForeground(Res.FONT_COLOR);
	mainPanel.add(regLabel,c);
	c.gridx=1;
	regField = new GMFormattedTextField();
	fields.add(regField);
	mainPanel.add(regField,c);

	c.gridx=0;
	c.gridy=3;
	typeLabel = new JLabel("Type(car,van,truck):");
	typeLabel.setForeground(Res.FONT_COLOR);
	mainPanel.add(typeLabel, c);
	c.gridx=1;
	typeField = new GMFormattedTextField();
	fields.add(typeField);
	mainPanel.add(typeField,c);

	c.gridx=0;
	c.gridy=4;
	modelLabel = new JLabel("Model:");
	modelLabel.setForeground(Res.FONT_COLOR);
	mainPanel.add(modelLabel, c);
	c.gridx=1;
	modelField = new GMFormattedTextField();
	fields.add(modelField);
	mainPanel.add(modelField,c);

	c.gridx=0;
	c.gridy=5;
	makeLabel = new JLabel("Make:");
	makeLabel.setForeground(Res.FONT_COLOR);
	mainPanel.add(makeLabel, c);
	c.gridx=1;
	makeField = new GMFormattedTextField();
	fields.add(makeField);
	mainPanel.add(makeField,c);

	c.gridx=0;
	c.gridy=6;
	engineLabel = new JLabel("Engine:");
	engineLabel.setForeground(Res.FONT_COLOR);
	mainPanel.add(engineLabel, c);
	c.gridx=1;
	engineField = new GMFormattedTextField();
	fields.add(engineField);
	mainPanel.add(engineField,c);

	c.gridx=0;
	c.gridy=7;
	fuelLabel = new JLabel("Fuel:");
	fuelLabel.setForeground(Res.FONT_COLOR);
	mainPanel.add(fuelLabel, c);
	c.gridx=1;
	fuelField = new GMFormattedTextField();
	fields.add(fuelField);
	mainPanel.add(fuelField,c);

	c.gridx=0;
	c.gridy=8;
	colourLabel = new JLabel("Colour:");
	colourLabel.setForeground(Res.FONT_COLOR);
	mainPanel.add(colourLabel, c);
	c.gridx=1;
	colourField = new GMFormattedTextField();
	fields.add(colourField);
	mainPanel.add(colourField,c);

	c.gridx=0;
	c.gridy=9;
	motLabel = new JLabel("MOT RenewalDate:");
	motLabel.setForeground(Res.FONT_COLOR);
	mainPanel.add(motLabel, c);
	c.gridx=1;
	motField = new GMFormattedTextField();
	fields.add(motField);
	mainPanel.add(motField,c);

	if(!isNew)//to edit a vehicle
		init(vehicleToEdit,true);
	
	c.gridx=0;
	c.gridy=10;
	warrantyLabel = new JLabel("Warranty:");
	warrantyLabel.setForeground(Res.FONT_COLOR);
	mainPanel.add(warrantyLabel,c);
	warranty2Label = new JLabel("Click Add Warranty to add/edit a warranty.");
	warranty2Label.setForeground(Res.FONT_COLOR);
	c.gridx=1;
	mainPanel.add(warranty2Label,c);
	
        //----BUTTONS----
	c.gridx=0;
	c.gridy=11;
	confirm = new GMButton("Confirm");

	if(isNew)//ADD
	{
		confirm.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e)
			{
				//check if all fields are completed
				if(checkCompleted(fields))
				{	
                                        //check if reg already in the database
                                        String vehReg = regField.getText().trim();
                                        if(model.isIn(vehReg))
                                        {
                                            JOptionPane.showMessageDialog(null,"Registration number already in database.");
                                            return;
                                        }
					String motStr = motField.getText();
					String engineStr = engineField.getText();
					//check format of fields
					if(isDate(motStr)&&isDouble(engineStr))
					{
                                                //check type 
                                                String vehType = typeField.getText().trim();
                                                if(!vehType.equals("car")&&!vehType.equals("van")&&!vehType.equals("truck"))
                                                {
                                                    JOptionPane.showMessageDialog(null,"The type must be either 'car','van' or 'truck'.");
                                                    return;
                                                }
                                                
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	                                        java.util.Date mot = parseDate(motStr,format);
                                                
                                                //compare with current date to see if its in the past
                                                java.util.Date today = new java.util.Date();
                                                if(mot.compareTo(today)<=0)
                                                {
                                                    JOptionPane.showMessageDialog(null,"Please type a future date for MOT.");
                                                    return;
                                                }
	         
                                                //convert to sql date
                                                java.sql.Date motDate = new java.sql.Date(mot.getTime());
						double engine=Double.parseDouble(engineStr);
						if(customerSelected==0)//first one in database is 1
						{
							customerSelected=1;
						}
						returnVehicle = new Vehicle(vehicleID,regField.getText(),typeField.getText(),warrantyId,customerSelected,modelField.getText(),makeField.getText(),engine,fuelField.getText(),colourField.getText(),motDate);
                                                model.add(returnVehicle);
						dispose();
                    }
                                        else
					{
						JOptionPane.showMessageDialog(null,"Please use the format YYYY-MM-DD for MOT date\nAnd a decimal for engine size(e.g.1.3,1.4)");
					}
                                }
				else
				{
					JOptionPane.showMessageDialog(null,"Complete all fields before confirming.");
				}
			}
		});
	}
	else//edit vehicle
	{
		confirm.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e)
			{
				//check if all fields are completed
				if(checkCompleted(fields))
				{
					String motStr = motField.getText();
                                        //check if reg already in the database
                                        String vehReg = regField.getText().trim();
                                        if(!vehicleToEdit.getRegNumber().equals(vehReg))
                                        {
                                            JOptionPane.showMessageDialog(null,"First if passed");
                                            if(model.isIn(vehReg))
                                            {
                                                JOptionPane.showMessageDialog(null,"Registration number already in database.");
                                                return;
                                            }
                                        }
					//check format of fields
					if(isDate(motStr))
					{
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	                                        java.util.Date mot = parseDate(motStr,format);

                                                //compare with current date to see if its in the past
                                                java.util.Date today = new java.util.Date();
                                                if(mot.compareTo(today)<=0)
	                    {
	                    	JOptionPane.showMessageDialog(null,"Please type a future date for expiry.");
	                    	return;
	                    }
	                    //convert to sql date
	    				java.sql.Date motDate = new java.sql.Date(mot.getTime());

	    				returnVehicle= new Vehicle(vehicleID,regField.getText(),colourField.getText(),motDate,warrantyId);
	    				model.update(returnVehicle);
	    				dispose();
				}
					else//date not valid
					{
						JOptionPane.showMessageDialog(null,"Please use the format YYYY-MM-DD for MOT date");
					}
                                }
				else//not all fields completed
				{
					JOptionPane.showMessageDialog(null,"Complete all fields before confirming.");
				}
			}
		});
	}
	mainPanel.add(confirm,c);

	c.gridx=1;
	cancel = new GMButton("Cancel");
	cancel.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e)
		{
			dispose();
		}
	});
	mainPanel.add(cancel,c);

	c.gridx=2;
	addWarranty = new GMButton("Add Warranty");
	if(isNew)
	{
		addWarranty.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				new NewWarrantyDialog(instance,model);
			}
                });
	}
	else
	{
		addWarranty.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				try
				{	
                                        int warrantyId2 = vehicleToEdit.getWarranty();
                     
                                        if(warrantyId2!=0)//there is a warranty already
                                        {
                                            
                                            Warranty warranty2 = model.getWarranty(warrantyId2);
                                            new NewWarrantyDialog(instance,warranty2,model);
                                        }
                                        else if(warranty!=null)//warranty was just added/changed
                                        {
                                            
                                            Warranty returned = model.getWarranty(warranty.getId());
                                            Address address = returned.getAddress();
                                            Warranty warranty2 = new Warranty(warranty.getId(),returned.getName(),address,returned.getExpiry());
                                            
                                            new NewWarrantyDialog(instance,warranty2,model);
                                        }
                                        else//no warranty
                                        {
                                            
                                            new NewWarrantyDialog(instance,model);
                                        }
				}
				catch(SQLException ex)
				{
					JOptionPane.showMessageDialog(null,ex.getMessage());
				}
			}
                });
	}
	
	mainPanel.add(addWarranty,c);
	//set window
        pack();
	add(mainPanel);
	setLocationRelativeTo(null);//center of screen
	setVisible(true);		
}

//parse string to java.util.Date
public java.util.Date parseDate(String str,SimpleDateFormat format) 
{
    java.util.Date date;
    try
    {
        date = format.parse(str);
    }
    catch(ParseException e)
    {
        date = null;
    }
    return date;
}
private boolean isDouble(String str)
{
	try
	{
		Double.parseDouble(str);
		return true;
	}
	catch(NumberFormatException e)
	{
		return false;
	}
}
public boolean isDate(String str)
{
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    format.setLenient(false);
	try//valid date
	{
		format.parse(str);
		return true;
	}
	catch(ParseException e)//invalid date
	{
		return false;
	}
}
//set fields to details of vehicle selected from drop down
private void init(Object veh,boolean isEditable)
{   
        
        Vehicle vehicle =(Vehicle)veh;
	typeField.setText(vehicle.getType());
        //order of make and model is wrong in the drop down box so 
        //we need to set differently when a vehicle from dropdown is selected
        if(isEditable)
        {
           makeField.setText(vehicle.getMake());
           modelField.setText(vehicle.getModel());
        }
        else
        {
            makeField.setText(vehicle.getModel());
            modelField.setText(vehicle.getMake());
        }
	
	fuelField.setText(vehicle.getFuel());
	engineField.setText(vehicle.getEngineSize()+"");	

	if(isEditable)
	{
		typeField.setEditable(false);
		makeField.setEditable(false);
		modelField.setEditable(false);
		fuelField.setEditable(false);
		engineField.setEditable(false);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String mot = format.format(vehicle.getMotRenewalDate());
                motField.setText(mot);
                motField.setForeground(Color.GREEN);
                colourField.setText(vehicle.getColour());
                colourField.setForeground(Color.GREEN);
                regField.setText(vehicle.getRegNumber());
                regField.setForeground(Color.GREEN);
                
	}

}

//check if all fields are completed
public boolean checkCompleted(ArrayList<GMFormattedTextField> fields)
{
	for(int i=0;i<fields.size();i++)
	{
		if(fields.get(i).getText().trim().equals(""))
			return false;
	}
	return true;
}
private Vehicle[] generateVehicles()
{
	//50 different vehicle configurations
	//model, make, engine size and fuel type
	//Assumption: generated vehicles can be different each time
	Vehicle[] vehicles = new Vehicle[50];
	String[] makes = {"Audi","Ford","Mercedes","Volkswaggen","Scania"};
	String[] models = {"A1","A3","A6","Fiesta","Focus","Mondeo","Sprinter","Viano","Transporter","P-Series"};
	double[] engines = {1.0,1.1,1.2,1.3,1.4,1.5,2.0,2.1,2.2,7.0,8.0,9.0};
	String[] fuels = {"petrol","diesel"};
	//String type, String model, String make, double engineSize, String fuel
	//Audis
	vehicles[0]=new Vehicle("car",makes[0],models[0],engines[0],fuels[0]);
	vehicles[1]=new Vehicle("car",makes[0],models[0],engines[1],fuels[0]);
	vehicles[2]=new Vehicle("car",makes[0],models[0],engines[2],fuels[1]);
	vehicles[3]=new Vehicle("car",makes[0],models[0],engines[3],fuels[1]);
	vehicles[4]=new Vehicle("car",makes[0],models[1],engines[0],fuels[0]);
	vehicles[5]=new Vehicle("car",makes[0],models[1],engines[2],fuels[0]);
	vehicles[6]=new Vehicle("car",makes[0],models[1],engines[3],fuels[1]);
	vehicles[7]=new Vehicle("car",makes[0],models[1],engines[0],fuels[0]);
	vehicles[8]=new Vehicle("car",makes[0],models[1],engines[4],fuels[1]);
	vehicles[9]=new Vehicle("car",makes[0],models[2],engines[0],fuels[0]);
	vehicles[10]=new Vehicle("car",makes[0],models[2],engines[8],fuels[0]);
	vehicles[11]=new Vehicle("car",makes[0],models[2],engines[7],fuels[1]);
	vehicles[12]=new Vehicle("car",makes[0],models[2],engines[6],fuels[0]);
	vehicles[13]=new Vehicle("car",makes[0],models[2],engines[5],fuels[1]);

	//Fords
	vehicles[14]=new Vehicle("car",makes[1],models[3],engines[0],fuels[0]);
	vehicles[15]=new Vehicle("car",makes[1],models[3],engines[1],fuels[0]);
	vehicles[16]=new Vehicle("car",makes[1],models[3],engines[2],fuels[1]);
	vehicles[17]=new Vehicle("car",makes[1],models[3],engines[3],fuels[1]);
	vehicles[18]=new Vehicle("car",makes[1],models[4],engines[4],fuels[0]);
	vehicles[19]=new Vehicle("car",makes[1],models[4],engines[5],fuels[0]);
	vehicles[20]=new Vehicle("car",makes[1],models[4],engines[6],fuels[1]);
	vehicles[21]=new Vehicle("car",makes[1],models[4],engines[7],fuels[1]);
	vehicles[22]=new Vehicle("car",makes[1],models[5],engines[7],fuels[1]);
	vehicles[23]=new Vehicle("car",makes[1],models[5],engines[3],fuels[0]);
	vehicles[24]=new Vehicle("car",makes[1],models[5],engines[5],fuels[0]);
	vehicles[25]=new Vehicle("car",makes[1],models[5],engines[4],fuels[1]);
	vehicles[26]=new Vehicle("car",makes[1],models[5],engines[3],fuels[0]);

	//Mercedes vans
	vehicles[27]=new Vehicle("van",makes[2],models[6],engines[6],fuels[0]);
	vehicles[28]=new Vehicle("van",makes[2],models[6],engines[7],fuels[0]);
	vehicles[29]=new Vehicle("van",makes[2],models[6],engines[7],fuels[1]);
	vehicles[30]=new Vehicle("van",makes[2],models[6],engines[6],fuels[1]);
	vehicles[31]=new Vehicle("van",makes[2],models[7],engines[6],fuels[0]);
	vehicles[32]=new Vehicle("van",makes[2],models[7],engines[7],fuels[0]);
	vehicles[33]=new Vehicle("van",makes[2],models[7],engines[7],fuels[1]);
	vehicles[34]=new Vehicle("van",makes[2],models[7],engines[6],fuels[1]);

	//Volkswaggen vans
	vehicles[35]=new Vehicle("van",makes[3],models[8],engines[6],fuels[0]);
	vehicles[36]=new Vehicle("van",makes[3],models[8],engines[7],fuels[0]);
	vehicles[37]=new Vehicle("van",makes[3],models[8],engines[8],fuels[1]);
	vehicles[38]=new Vehicle("van",makes[3],models[8],engines[6],fuels[1]);
	vehicles[39]=new Vehicle("van",makes[3],models[8],engines[6],fuels[0]);
	vehicles[40]=new Vehicle("van",makes[3],models[8],engines[7],fuels[0]);
	vehicles[41]=new Vehicle("van",makes[3],models[8],engines[8],fuels[1]);
	vehicles[42]=new Vehicle("van",makes[3],models[8],engines[8],fuels[1]);

	//Trucks
	vehicles[43]=new Vehicle("truck",makes[4],models[9],engines[11],fuels[0]);
	vehicles[44]=new Vehicle("truck",makes[4],models[9],engines[9],fuels[0]);
	vehicles[45]=new Vehicle("truck",makes[4],models[9],engines[10],fuels[1]);
	vehicles[46]=new Vehicle("truck",makes[4],models[9],engines[11],fuels[1]);
	vehicles[47]=new Vehicle("truck",makes[4],models[9],engines[9],fuels[0]);
	vehicles[48]=new Vehicle("truck",makes[4],models[9],engines[10],fuels[0]);
	vehicles[49]=new Vehicle("truck",makes[4],models[9],engines[9],fuels[1]);


	//TO-DO:generate 50 different vehicle configs and store in vehicles array
        return vehicles;
}
public int getCustomerSelected()
{
	return customerSelected;
}
//set warranty added for this vehicle
public void setWarranty(int warrantyId2,String name,Warranty warranty)
{
	this.warrantyId=warrantyId2;
	warranty2Label.setText("Warranty added. Company Name:"+name);
        this.warranty = warranty;
}
	
}//END class