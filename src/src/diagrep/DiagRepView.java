
package diagrep;

import assets.GMButton;
import assets.GMTable;
import assets.Res;
import common.View;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 * @author vc300
 */
public class DiagRepView extends JPanel{
    private GMTable table, todayTable, partsTable;
    private GMButton add, addPart, remove, update, addDuration;
    private final View parentView;
    
    public DiagRepView(View parentView) {
        this.parentView = parentView;
        init();
    }
    
    //initialize view components
    private void init() {
        // set layout
        this.setLayout(new BorderLayout(10, 10));
        
         // Init GMTable D&R Booking table 
        table = new GMTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel tableButtonPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        tableButtonPanel.setBackground(Res.BKG_COLOR);
        JPanel tablePanel = new JPanel(new BorderLayout(5,5));
        tablePanel.setBorder(new TitledBorder(new EtchedBorder(), "Diagnosis & Repair Bookings", 1, TitledBorder.CENTER, null, Res.FONT_COLOR));
        tablePanel.setBackground(Res.BKG_COLOR);
        
        // Init Buttons
        add = new GMButton("Add Diagnosis & Repair Booking");
        addPart = new GMButton("Add new Part");
        update = new GMButton("Update Booking");
        remove = new GMButton("Remove Booking");
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 3, 5, 5));
        buttonPanel.setBackground(Res.BKG_COLOR);
        buttonPanel.add(add);
        tableButtonPanel.add(remove);
        tableButtonPanel.add(update);
        tableButtonPanel.add(addPart);
        addDuration = new GMButton("Add Duration of Work");
        tableButtonPanel.add(addDuration);
        
        tablePanel.add(tableButtonPanel, BorderLayout.PAGE_START);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Init GMTable for todays D&R Bookings
        todayTable = new GMTable();
        table.setRowSelectionAllowed(true);
        JScrollPane todayScrollPane = new JScrollPane(todayTable);
        JPanel todayPanel = new JPanel(new GridLayout());
        todayPanel.setBackground(Res.BKG_COLOR);
        todayPanel.setBorder(new TitledBorder(new EtchedBorder(), "Today", 1, TitledBorder.CENTER, null, Res.FONT_COLOR));
        todayPanel.add(todayScrollPane);
        
        // Init GMTable for parts installed
        partsTable = new GMTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane partsScrollPane = new JScrollPane(partsTable);
        JPanel partsPanel = new JPanel(new GridLayout());
        partsPanel.setBackground(Res.BKG_COLOR);
        partsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Parts To Installed", 1, TitledBorder.CENTER, null, Res.FONT_COLOR));
        partsPanel.add(partsScrollPane);
        
        
        
        // GridBagLayout Main Panel
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        JPanel mainPanel = new JPanel(gridBagLayout);
        
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 2;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        mainPanel.add(tablePanel, gridBagConstraints);
        gridBagConstraints.weightx = 1.2;
        gridBagConstraints.gridx = 1;
        mainPanel.add(todayPanel, gridBagConstraints);
        gridBagConstraints.weightx = 1.2;
        gridBagConstraints.gridx = 2;
        mainPanel.add(partsPanel, gridBagConstraints);
        
        // maine Layout
        JPanel layoutPanel = new JPanel();
        layoutPanel.setLayout(new GridLayout());
        layoutPanel.setBackground(Res.BKG_COLOR);
        mainPanel.setBackground(Res.BKG_COLOR);
        /// add elements
        layoutPanel.add(mainPanel);
        this.add(buttonPanel, BorderLayout.PAGE_START);
        this.add(layoutPanel, BorderLayout.CENTER);
        this.setBackground(Res.BKG_COLOR);

        this.setVisible(true);
    }
    
    
    void updateTable() {
        table.updateTable();
        todayTable.updateTable();
        partsTable.updateTable();
        revalidate();
    }
    
      ///////////////////////////////////////////////////////////
    /////////////////////Listener Plugs///////////////////////////////
    ///////////////////////////////////////////////////////////
    
    
    void addTableListener(MouseListener e) {
        table.addMouseListener(e);
    }
    
    void addAddListener(ActionListener e) {
        add.addActionListener(e);
    }
    
    void addAddPartListener(ActionListener e) {
        addPart.addActionListener(e);
    }
    
    void addRemoveListener(ActionListener e) {
        remove.addActionListener(e);
    }
    
    void addUpdateListener(ActionListener e) {
        update.addActionListener(e);
    }
    
    void addDurationListener(ActionListener e) {
        addDuration.addActionListener(e);
    }
    
    
    ///////////////////////////////////////////////////////////
    /////////////////////Show Msg Methods///////////////////////////////
    ///////////////////////////////////////////////////////////
    
    void showMessage(boolean error,String msg) {
        if(error)
                {JOptionPane.showMessageDialog(this, msg, "ERROR", JOptionPane.ERROR_MESSAGE);
                }
        else
                {JOptionPane.showMessageDialog(this, msg, null, JOptionPane.INFORMATION_MESSAGE);
                }
                
    }
    
    
    ///////////////////////////////////////////////////////////
    /////////////////////Getters///////////////////////////////
    ///////////////////////////////////////////////////////////
    
    GMTable getTable() {
        return table;
    }
    
    GMTable getTodayTable() {
        return todayTable;
    }

    GMTable getPartsTable() {
        return partsTable;
    }
    
    View getParentView() {
        return parentView;
    }
}
