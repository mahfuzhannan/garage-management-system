package customer.GUI;

//Haricharran Sampat

import assets.GMButton;
import assets.GMComboBox;
import assets.GMFormattedTextField;
import common.Database;
import customer.Logic.CustomerController;
import customer.Logic.Customer;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class CustomerDialog extends JDialog{

        /* dialog of customer details
           since update and add require exactly the same fields
           the same dialog can be used
        */
    
	 static GMButton confirm;
        
         GMButton cancel;
       
         GMFormattedTextField fnField = new GMFormattedTextField();
         GMFormattedTextField lnField = new GMFormattedTextField();
  
         String [] custTypes ={"private","business"} ;
         GMComboBox typField = new GMComboBox(custTypes);
         
         GMFormattedTextField phField = new GMFormattedTextField();
         GMFormattedTextField emField = new GMFormattedTextField();
         
         GMFormattedTextField adLine1 = new GMFormattedTextField();
         GMFormattedTextField adLine2 = new GMFormattedTextField();
         GMFormattedTextField adCity = new GMFormattedTextField();
         GMFormattedTextField adPcode = new GMFormattedTextField();
        
         
	private CustomerController controller;
        
        private boolean isAdd= false;
        
        String [] addresses;
        
        
        //constructor for add dialog
	public CustomerDialog(){
		
                setTitle("Add");
                setModal(true);
                
                controller = new CustomerController( new Customer());
                isAdd = true;
		init();
		
	}


        
	//constructor for update dialog
	public CustomerDialog( int cid){
              
		setTitle("Update");
           
                controller = new CustomerController(new Customer(cid));
		init();
		setFields(cid);
                
	}
        
        
        
        //when confirm button is pressed
        //decide which action to take add or update
        ActionListener confirmLstn = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    
                    String fn = fnField.getText();
                    String ln = lnField.getText();
                    String typ = (String)typField.getSelectedItem();
                    String ph = phField.getText();
                    String em = emField.getText();
                   
                    addresses[0] = adLine1.getText();
                    addresses[1]= adLine2.getText();
                    addresses[2] = adCity.getText();
                    addresses[3] = adPcode.getText();
                    
                    
                    
                    if(isAdd){
                    
                      
                        String ad = combine(addresses);
                      
                        
                        if(validate(fn,ln,typ,ad,ph,em)){
                       
                            controller.add(fn, ln, typ, ad, ph, em);
    
                        }
                        
                        else{
                            //allow the user to resume editing
                            return;
                        }
                    
                    }
                    
                    else{
                        
                        String ad = combine(addresses);
                        
                        if(validate(fn,ln,typ,ad,ph,em)){
                            
                           controller.update(fn, ln, typ, ad, ph, em);
                        }
                        
                        else{
                            return;
                        }
                       
                    }
                    
                    
                    //update affected table
                    CustomerView.custTable.setModel(Database.getTable(Database.executeQuery("SELECT * FROM \"Customer Accounts\" ")));

                    //save memory
                    dispose();
                    
                    } 
                
                catch (SQLException ex) {
                    Logger.getLogger(CustomerDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
	
        
    
	
        
        //sets dialog layout
	public void init(){
	       
                addresses = new String[4];	
		
            
                fnField.setPreferredSize(new Dimension(100, 25));
                lnField.setPreferredSize(new Dimension(100, 25));
                typField.setPreferredSize(new Dimension(100, 25));
                     
                adLine1.setPreferredSize(new Dimension(100, 25));
                adLine2.setPreferredSize(new Dimension(100, 25));
                adCity.setPreferredSize(new Dimension(100, 25));
                adPcode.setPreferredSize(new Dimension(100, 25));
               
                phField.setPreferredSize(new Dimension(100, 25));
                emField.setPreferredSize(new Dimension(100, 25));
               
                
		
		JPanel panel = new JPanel(new GridBagLayout());
		add(panel);
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
                c.weightx =1;
                c.weighty =3;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(new JLabel("First Name:"), c);
		
                
		c.gridx = 0;
		c.gridy = 1;
		panel.add(new JLabel("Last Name:"), c);
		
		c.gridx = 0;
		c.gridy = 2;
		panel.add(new JLabel("Type:"), c);
		
		c.gridx = 0;
		c.gridy = 3;
		panel.add(new JLabel("Address Line 1:"), c);
		
		c.gridx = 0;
		c.gridy = 4;
                panel.add(new JLabel("Address Line 2:"), c);
		
                c.gridx = 0;
		c.gridy = 5;
                panel.add(new JLabel("Address City:"), c);
	
                c.gridx = 0;
		c.gridy = 6;
                panel.add(new JLabel("Address Postcode:"), c);
                
                c.gridx = 0;
		c.gridy = 7;
                panel.add(new JLabel("Phone Num:"), c);
                
                c.gridx = 0;
		c.gridy = 8;
                panel.add(new JLabel("Email:"), c);
                
                c.weightx =5;
                c.weighty =3;
		c.gridx = 1;
		c.gridy = 0;
		panel.add(fnField, c);
		
		c.gridx = 1;
		c.gridy = 1;
		panel.add(lnField, c);
		
		c.gridx = 1;
		c.gridy = 2;
		panel.add(typField, c);
		
		c.gridx = 1;
		c.gridy = 3;
		panel.add(adLine1, c);
		
		c.gridx = 1;
		c.gridy = 4;
                panel.add(adLine2, c);
		
                c.gridx = 1;
		c.gridy = 5;
                panel.add(adCity, c);
		
                c.gridx = 1;
		c.gridy = 6;
                panel.add(adPcode, c);
                
                c.gridx = 1;
		c.gridy = 7;
                panel.add(phField, c);
                
                c.gridx = 1;
		c.gridy = 8;
                panel.add(emField, c);
		
		c.gridx = 0;
		c.gridy = 9;
              
                confirm = new GMButton("Confirm");
                confirm.addActionListener(confirmLstn);
		panel.add(confirm, c);
		
		cancel = new GMButton("Cancel");
	
		c.gridx = 1;
		c.gridy = 9;
		panel.add(cancel, c);
                
                cancel.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });
		
		
		pack();
	
                setSize(300, 700);
                setResizable(false);
                setLocationRelativeTo(null);
                setVisible(true);
                
	}
        
	
      
        
        
        
	//sets value of each field 
	public void setFields(int cid){
		
		fnField.setText(controller.model.getFirstName());
		lnField.setText(controller.model.getLastName());
	
                typField.setSelectedItem(controller.model.getType());
                
		phField.setText(controller.model.getPhoneNum());
                emField.setText(controller.model.getEmail());
                
                split(controller.model.getAddress());
                adLine1.setText(addresses[0]);
                adLine2.setText(addresses[1]);
                adCity.setText(addresses[2]);
                adPcode.setText(addresses[3]);
          
	}
	
        
        
        // validate user input for each field
        //specifies what format of data is acceptable for each field
        public boolean validate(String fn,String ln, String typ, String addr, String ph, String em){

            
            String txtFmt = "[a-zA-Z\\s]{1,30}";
                    
            
            String lnFmt = "[a-zA-z]+";
           
            
            //UK Address Format  = house/building number + ___ + postcode 
            //each address line can not contain a ',' since they are supposed to 
            //be individual lines
            String adFmt = "[^,]{1,30}";
                
            String phFmt = "[0-9]{10,13}";
            
            //includes letters numbers and non letter characters
            String emFmt = "[a-zA-Z0-9\\W_]+@[a-zA-Z0-9\\W]+";
            int emMaxLength = 60;
            
 
            
            if (!(fn.matches(txtFmt))){
                
                //output message 
                JOptionPane.showMessageDialog(rootPane, "invalid first name");
                return false;
            }
            
            if(!(ln.matches(txtFmt))){
                JOptionPane.showMessageDialog(rootPane, "invalid last name");
                return false;
            }
            
            if(!(typ.equals("private") || typ.equals("business"))){
                JOptionPane.showMessageDialog(rootPane, "invalid type");
                return false;
            }
            
            
            for(int i=0; i<addresses.length; i++){
                if(!addresses[i].matches(adFmt)){
                    JOptionPane.showMessageDialog(rootPane, "invalid address");
                    System.out.println(i);
                    return false;
                }
                
            }
            
             if(!(ph.matches(phFmt))){
                JOptionPane.showMessageDialog(rootPane, "invalid phone number");
                return false;
            }
            
             if(!(em.matches(emFmt) && em.length()>0 && em.length()<emMaxLength)){
                JOptionPane.showMessageDialog(rootPane, "invalid email");
                return false;
            }
            
            return true;
            
        }
        
        
        
          public String combine(String[] ad){
            
              String total = "";
              
            for(int i=0; i< ad.length-1; i++){
                
                total += ad[i]+",";
                
            }
            
            total+=ad[3];
            return total;
        }
          
          
          public String div(String x, int pos){
              
              for(int i=0; i<x.length(); i++){
                  
                  if(x.charAt(i) == ','){
                      addresses[pos] = x.substring(0, i);
                      return x.substring(i+1, x.length());
                  }
                   
              }
              
             return null;
          }
          
          
          public void split(String addr){
             
              for(int i=0; i<addresses.length-1; i++){
                  addr = div(addr, i);
                  
              }
              
              addresses[addresses.length-1]= addr;
                
          }
        
        
          
        
        
}//end class
