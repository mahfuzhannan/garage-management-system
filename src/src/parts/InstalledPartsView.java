package parts;

import assets.GMButton;
import assets.Res;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * Dialog for installed parts list. Contains buttons to
 * add and delete installed parts.
 * @author George
 */
public class InstalledPartsView extends JDialog {
	private JTable table;

	/**
	 * Create the view.
	 */
	public InstalledPartsView() {
		
		// Create table for the list. Add a model for installed parts.
		table = new JTable(InstalledPartsDialogModel.getInstance());
		setLayout(new BorderLayout());
		setBackground(Res.BKG_COLOR);
		setTitle("Installed Parts");
		
		// table needs to be in scroll pane to show headers.
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);
		
		
		// Add the 2 buttons.
		GMPanel btnPanel = new GMPanel(new GridLayout(1, 2, 3, 3));
		GMButton addBtn = new GMButton("Add");
		addBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new NewInstallDialog();
			}
		});
		GMButton removeBtn = new GMButton("Remove");
		removeBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Check if a row is selected.
				if (table.getSelectedRow()!=-1) {
					//Extract id from table.
					int id = (int)table.getValueAt(table.getSelectedRow(), 0);
					// Remove from database.
					InstalledPartsDialogModel.getInstance().remove(id);
				}
			}
		});
		
		btnPanel.add(addBtn);
		btnPanel.add(removeBtn);
		
		add(btnPanel, BorderLayout.SOUTH);
		
		pack();
		setModal(true);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	
}
