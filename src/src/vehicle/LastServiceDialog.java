//Author: Stefan Ionascu
//Module: Vehicle Records
//Date: 27/03/2014

//LastServiceDialog shows the attributes of the last service associated with a vehicle
//The details of the service are taken from the SimpleService class
package vehicle;
import assets.Res;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class LastServiceDialog extends JDialog
{
	private JPanel mainPanel;
        private String reg;
	public LastServiceDialog(SimpleService service,String reg)
	{
                this.reg=reg;
		initPanel(service);
	}
	public void initPanel(SimpleService service)
	{
		this.getContentPane().setBackground(Res.BKG_COLOR);
		setModal(true);

		mainPanel = new JPanel(new GridBagLayout()); 
		mainPanel.setBackground(Res.BKG_COLOR);
		add(mainPanel);
		GridBagConstraints c = new GridBagConstraints();
		c.fill=GridBagConstraints.HORIZONTAL;
                
                c.gridx=0;
                c.gridy=0;
                JLabel label = new JLabel("Reg:"+reg);
		label.setForeground(Res.FONT_COLOR);
		mainPanel.add(label,c);
                
		c.gridx=0;
		c.gridy=1;
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String text = df.format(service.getDate());
		label = new JLabel("Date:"+text);
		label.setForeground(Res.FONT_COLOR);
		mainPanel.add(label,c);

		c.gridy=2;
		label = new JLabel("Mileage:"+service.getMileage());
		label.setForeground(Res.FONT_COLOR);
		mainPanel.add(label,c);

		c.gridy=3;
		label = new JLabel("Total cost:"+service.getCost());
		label.setForeground(Res.FONT_COLOR);
		mainPanel.add(label,c);

		add(mainPanel);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
}