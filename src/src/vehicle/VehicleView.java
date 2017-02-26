package vehicle;
import assets.GMButton;
import assets.GMComboBox;
import assets.GMTable;
import assets.Res;
import common.Database;
import common.View;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import static javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
public final class VehicleView extends JPanel {
    private VehicleModel vehModel;
    private PartsModel partModel;
    private BookingsModel bookingsModel;

    private GMTable vehicleTable,partsTable,bookingsTable;
    private GMButton addVehicle, removeVehicle,updateVehicle,searchVehicle,seeLastService;
    
    public VehicleView()
    {
        //--------SET UP VIEW/ADD COMPONENTS(INITIALISE)------------
        //models
        vehModel = VehicleModel.getInstance(false);
        partModel = PartsModel.getInstance();
        bookingsModel = BookingsModel.getInstance();
        //layout
        this.setLayout(new BorderLayout(10, 10));
        
        //---------BUTTONS-------------------------------
        addVehicle = new GMButton("Add Vehicle");
        removeVehicle = new GMButton("Remove Vehicle");
        updateVehicle = new GMButton("Update Vehicle");
        searchVehicle = new GMButton("Search Vehicle");
        seeLastService = new GMButton("See Last Service");
        //button panel
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new GridLayout(0, 5, 5, 5));
        btnPanel.setBackground(Res.BKG_COLOR);
        btnPanel.add(addVehicle);
        btnPanel.add(removeVehicle);
        btnPanel.add(updateVehicle);
        btnPanel.add(searchVehicle);
        btnPanel.add(seeLastService);

        //---------MAIN TABLE-------------------------------
        vehicleTable = new GMTable(vehModel);
        vehModel.changed();
        vehicleTable.setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);       
        JScrollPane scrollPane = new JScrollPane(vehicleTable);
        vehicleTable.setRowSelectionAllowed(true);
        
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new TitledBorder(new EtchedBorder(), "Vehicle Records", 1, TitledBorder.CENTER, null, Res.FONT_COLOR));
        tablePanel.setBackground(Res.BKG_COLOR);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.add(btnPanel, BorderLayout.NORTH);
        
        //------PARTS TABLE-----
        partsTable = new GMTable(partModel);
        partsTable.updateTable();
        JScrollPane partsPane = new JScrollPane(partsTable);
        JPanel partsPanel = new JPanel(new BorderLayout());
        partsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Parts Installed", 1, TitledBorder.CENTER, null, Res.FONT_COLOR));
        partsPanel.setBackground(Res.BKG_COLOR);
        partsPanel.add(partsPane,BorderLayout.CENTER);


        vehicleTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent event) 
        {
            int row = vehicleTable.getSelectedRow();
            
            if (!event.getValueIsAdjusting()&&row > -1) 
            {
                int id = (int) vehicleTable.getValueAt(row, 0);
                
                //GET PARTS ASSOCIATED WITH VEHICLE id
                partModel.setId(id);
                partsTable.updateTable();//adjust columns

                //GET BOOKINGS ASSOCIATED WITH VEHICLE id
                bookingsModel.setId(id);
                bookingsTable.updateTable();
            }
        }
        });

        //------BOOKINGS TABLE-----
        bookingsTable = new GMTable(bookingsModel);
        bookingsTable.updateTable();
        JScrollPane bookingsPane = new JScrollPane(bookingsTable);
        JPanel bookingsPanel = new JPanel(new BorderLayout());
        bookingsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Bookings", 1, TitledBorder.CENTER, null, Res.FONT_COLOR));
        bookingsPanel.setBackground(Res.BKG_COLOR);
        bookingsPanel.add(bookingsPane,BorderLayout.CENTER);

        //-----PANEL FOR TABLES------
        JPanel tablesPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        tablesPanel.setBackground(Res.BKG_COLOR);

        c.gridheight=2;
        c.gridx=0;
        c.gridy=0;
        c.weightx =3;
        c.weighty=1;
        tablesPanel.add(tablePanel,c);

        c.gridheight=1;
        c.gridx=1;
        c.gridy=0;
        c.weightx=1;
        tablesPanel.add(partsPanel,c);

        c.gridy=1;
        tablesPanel.add(bookingsPanel,c);

        vehicleTable.updateTable();
        add(btnPanel,BorderLayout.NORTH);
        add(tablesPanel,BorderLayout.CENTER);
        this.setBackground(Res.BKG_COLOR);
        this.setVisible(true);
    }
    
    public Vehicle getVehicle(int row)
    {
        int id=(int)vehicleTable.getValueAt(row,0);
        String type=(String)vehicleTable.getValueAt(row,1);
        String reg =(String)vehicleTable.getValueAt(row,2);
        String make = (String)vehicleTable.getValueAt(row,3);
        String model = (String)vehicleTable.getValueAt(row,4);
        double engine=(double)vehicleTable.getValueAt(row,5);
        String fuel=(String)vehicleTable.getValueAt(row,6);
        String color=(String)vehicleTable.getValueAt(row,7);
        
        String motStr = (String)vehicleTable.getValueAt(row,8);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date mot = parseDate(motStr,format);
        java.sql.Date motDate = new java.sql.Date(mot.getTime());
        
        String customerStr = (String)vehicleTable.getValueAt(row,9);
        String[] details = customerStr.split(":");
        int customerId=Integer.parseInt(details[0]);
        int warrantyId = 0;//assume there is no warranty
        String warrantyStr="";
        try
        {
            warrantyStr = (String)vehicleTable.getValueAt(row,10);
        }
        catch(ClassCastException ex)//is int not string(so there is a warranty id)
        {
            warrantyId=(int)vehicleTable.getValueAt(row,10);
        }
        
        return new Vehicle(id,reg,type,warrantyId,customerId,model,make,engine,fuel,color,motDate);

    }
    private Date parseDate(String str,SimpleDateFormat format) 
    {
    Date date;
    try
    {
        date = (Date) format.parse(str);
    }
    catch(ParseException e)
    {
        date = null;
    }
        return date;
    }
    public GMTable getMainTable()
    {
        return vehicleTable;
    }
    public void addAddVehicleListener(ActionListener e) {
        addVehicle.addActionListener(e);
    }

    public void removeAddVehicleListener(ActionListener e) {
        removeVehicle.addActionListener(e);
    }
    public void updateAddVehicleListener(ActionListener e) {
        updateVehicle.addActionListener(e);
    }
    public void lastServiceAddVehicleListener(ActionListener e)
    {
        seeLastService.addActionListener(e);
    }
    public void searchAddVehicleListener(ActionListener e)
    {
        searchVehicle.addActionListener(e);
    }
    
}