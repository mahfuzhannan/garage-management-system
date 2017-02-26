package parts;

import com.toedter.calendar.JDateChooser;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Dialog box for installing a part to a vehicle.
 * @author George
 */
public class NewInstallDialog extends GMDialog {
	
	private JTextField vehicleField, partField;
	private JDateChooser dateChooser;
	private final GMDialog thisDialog = this;
	
	ActionListener confirmListenerAdd = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			
			try {
				String date = new SimpleDateFormat("yyyy-MM-dd").format(dateChooser.getDate());
			int vehicleID = Integer.parseInt(vehicleField.getText());
			int partID = Integer.parseInt(partField.getText());
				InstalledPartsDialogModel.getInstance().add(date, vehicleID, partID);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(thisDialog, "Please check inputs are prensent and correct");
			}
		}
	};
	
	/**
	 * Create new NewInstallDialog. 
	 */
	public NewInstallDialog() {
		setTitle("Install Part");
		init();
		confirm.addActionListener(confirmListenerAdd);
		setVisible(true);
	}
	
	/**
	 * Set up UI
	 */
	private void init() {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(3, 3, 3, 3);
		
		// Add 3 labels.
		c.gridx = 0;
		c.gridy = 0;
		panel.add(new GMLabel("Vehicle ID:"), c);
		
		c.gridx = 0;
		c.gridy = 1;
		panel.add(new GMLabel("Part ID:"), c);
		
		c.gridx = 0;
		c.gridy = 2;
		panel.add(new GMLabel("Date Installed:"), c);
		
		c.weightx = 1;
		
		// Text fields for vehicle and part
		
		c.gridx = 1;
		c.gridy = 0;
		vehicleField = new JTextField();
		panel.add(vehicleField, c);
		
		c.gridx = 1;
		c.gridy = 1;
		partField = new JTextField();
		panel.add(partField, c);
		
		
		//Date chooser for date.
		c.gridx = 1;
		c.gridy = 2;
		dateChooser = new JDateChooser(new java.util.Date());
		dateChooser.setDateFormatString("yyyy-MM-dd");
		panel.add(dateChooser, c);
		
		pack();
	}
}
