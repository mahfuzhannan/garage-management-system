//Author: Stefan Ionascu
//Module: Vehicle Records
//Date: 27/03/2014

//NewWarrantyDialog presents a dialog for the user to use in order to add a new 
//warranty to the database or to edit/view a current one.

package vehicle;

import assets.GMFormattedTextField;
import assets.Res;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

class NewWarrantyDialog extends JDialog
{
	private JButton confirm,cancel;
	private JLabel nameLabel,expiryLabel,line1Label,cityLabel,postcodeLabel;
	private GMFormattedTextField nameField,expiryField,line1Field,cityField,postcodeField;
	private Warranty warranty2;
	private NewVehicleDialog view;
	private JPanel mainPanel;
	private ArrayList<GMFormattedTextField> fields;
	private VehicleModel model;

	//new warranty
	public NewWarrantyDialog(NewVehicleDialog view,VehicleModel model)
	{
                super(view);
		this.model=model;
		
		this.view=view;
		initPanel(true);
	}

	//already warranty in database
	public NewWarrantyDialog(NewVehicleDialog view, Warranty warranty,VehicleModel model)
	{
		super(view);
		this.view = view;
		this.model=model;
                this.warranty2=warranty;
      
		initPanel(false);
	}

	public void initPanel(boolean isNew)
	{
		this.getContentPane().setBackground(Res.BKG_COLOR);
		setResizable(false);
		setModal(true);
		mainPanel = new JPanel(new GridBagLayout()); 
		mainPanel.setBackground(Res.BKG_COLOR);
		add(mainPanel);

		GridBagConstraints c = new GridBagConstraints();
		c.fill=GridBagConstraints.HORIZONTAL;
		
		fields = new ArrayList<GMFormattedTextField>();

		c.gridx=0;
		c.gridy=0;
		nameLabel = new JLabel("Company Name:");
		nameLabel.setForeground(Res.FONT_COLOR);
		mainPanel.add(nameLabel,c);
		c.gridx=1;
		nameField = new GMFormattedTextField();
		fields.add(nameField);
		mainPanel.add(nameField,c);

		c.gridx=0;
		c.gridy=1;
		expiryLabel = new JLabel("Expiry Date:");
		expiryLabel.setForeground(Res.FONT_COLOR);
		mainPanel.add(expiryLabel,c);
		c.gridx=1;
		expiryField = new GMFormattedTextField();
		fields.add(expiryField);
		mainPanel.add(expiryField,c);

		c.gridx=0;
		c.gridy=2;
		line1Label = new JLabel("Line1:");
		line1Label.setForeground(Res.FONT_COLOR);
		mainPanel.add(line1Label,c);
		c.gridx=1;
		line1Field = new GMFormattedTextField();
		fields.add(line1Field);
		mainPanel.add(line1Field,c);

		c.gridx=0;
		c.gridy=3;
		cityLabel = new JLabel("City:");
		cityLabel.setForeground(Res.FONT_COLOR);
		mainPanel.add(cityLabel,c);
		c.gridx=1;
		cityField = new GMFormattedTextField();
		fields.add(cityField);
		mainPanel.add(cityField,c);

		c.gridx=0;
		c.gridy=4;
		postcodeLabel = new JLabel("Postcode:");
		postcodeLabel.setForeground(Res.FONT_COLOR);
		mainPanel.add(postcodeLabel,c);
		c.gridx=1;
		postcodeField = new GMFormattedTextField();
		fields.add(postcodeField);
		mainPanel.add(postcodeField,c);

		//---BUTTONS---
		c.gridy=5;
		c.gridx=0;
		confirm = new JButton("Confirm");
		confirm.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
                            if(view.checkCompleted(fields))
                            {
                                    String expiryStr = expiryField.getText();
                                    if(view.isDate(expiryStr))
                                    {
                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                            java.util.Date expiry = view.parseDate(expiryStr,format);

                                            //compare with current date to see if its in the past
                                            java.util.Date today = new java.util.Date();
                                            if(expiry.compareTo(today)<=0)
                                            {
                                                JOptionPane.showMessageDialog(null,"Please type a future date.");
                                                return;
                                            }
                                            java.sql.Date expiryDate = new java.sql.Date(expiry.getTime());
                                            //parse address
                                            Address address = new Address(line1Field.getText(),cityField.getText(),postcodeField.getText());
                                            Warranty warranty = new Warranty(nameField.getText(),address,expiryDate);
                                            if(isNew)
                                            {
                                                //add warranty to database
                                                int warrantyId = model.addWarranty(warranty);
                                                
                                                view.setWarranty(warrantyId,nameField.getText(),warranty);
                                                dispose();
                                            }
                                            else
                                            {
                                                //update current warranty
                                                Warranty warranty3= new Warranty(warranty2.getId(),nameField.getText(),address,expiryDate);
                                                model.updateWarranty(warranty3);
                                                view.setWarranty(warranty2.getId(), nameField.getText(), warranty3);
                                                dispose();
                                            }

                                            
                                    }
                                    
                                    else
                                    {
                                            JOptionPane.showMessageDialog(null,"Please use the format YYYY-MM-DD for expiry date");
                                    }
                            }//END if(checkcompleted)
                            else
                            {
                                    JOptionPane.showMessageDialog(null,"Complete all fields before confirming.");
                            }
                        }
                });
	mainPanel.add(confirm,c);
        c.gridx=1;
	
	cancel = new JButton("Cancel");
	cancel.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e)
		{
			dispose();
		}
	});
	mainPanel.add(cancel,c);
	

	try
	{
		init(warranty2);//set warranty fields to warranty already existent
	}
	catch(NullPointerException e)//no warranty or new warranty is added 
	{}
        
        add(mainPanel);
	pack();
	setLocationRelativeTo(null);
	setVisible(true);

	}

	public void init(Warranty warranty)
	{
		nameField.setText(warranty.getName());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String expiry = format.format(warranty.getExpiry());
                expiryField.setText(expiry);

                Address address = warranty.getAddress();
                line1Field.setText(address.getLine1());
                cityField.setText(address.getCity());
                postcodeField.setText(address.getPostcode());
	}
}