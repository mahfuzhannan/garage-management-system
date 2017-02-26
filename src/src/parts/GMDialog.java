package parts;

import assets.GMButton;
import assets.Res;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A JDialog with GM styling and Confirm/Cancel buttons.
 * Extend this for creating data entry dialogs.
 * @author George
 */


public class GMDialog extends JDialog {
	
	
	protected JButton confirm, cancel;
	protected JPanel panel;
	private GMLabel title;
	
	/**
	 * Create a new GMDialog. 
	 * Content panel can be accessed by sub classes as panel.
	 * Buttons can be accessed from confirm or cancel.
	 */
	public GMDialog() {
		super.setModal(true);
		super.setBackground(Res.BKG_COLOR);
		setResizable(false);
		
		setLayout(new BorderLayout());
		
		title = new GMLabel();
		title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 24));
		title.setHorizontalAlignment(JLabel.CENTER);
		GMPanel titlePanel = new GMPanel(new GridLayout());
		titlePanel.add(title);
		add(titlePanel , BorderLayout.NORTH);
		
		JPanel buttonPanel = new GMPanel(new GridLayout(1, 2, 3, 0), 5);
		
		confirm = new GMButton("Confirm");
		buttonPanel.add(confirm);
		
		cancel = new GMButton("Cancel");
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPanel.add(cancel);
		
		add(buttonPanel, BorderLayout.SOUTH);
		
		panel = new GMPanel(new GridBagLayout(), 5);
		
		add(panel, BorderLayout.CENTER);
		
		setLocationRelativeTo(null);
		
	}
	
	@Override
	public void setTitle(String titleText) {
		//super.setTitle(titleText);
		title.setText(titleText);
	}
}
