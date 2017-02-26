/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import assets.Res;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import settings.UsersController;

/**
 *
 * @author mafz
 */
public class Database {
    /**
     * Create the database tables.
     */
    private static final String[] tables = {"CREATE TABLE IF NOT EXISTS \"Parts Installed\" (\"Parts Installed ID\"  INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, \"Date Installed\" date NOT NULL, \"VehicleVehicle ID\" integer(10) NOT NULL, \"WarrantyWarranty ID\" integer(10), \"Parts StockPart ID\" integer(10) NOT NULL, FOREIGN KEY(\"Parts StockPart ID\") REFERENCES \"Parts Stock\"(\"Part ID\"), FOREIGN KEY(\"WarrantyWarranty ID\") REFERENCES Warranty(\"Warranty ID\"), FOREIGN KEY(\"VehicleVehicle ID\") REFERENCES Vehicle(\"Vehicle ID\"));\n" ,
        "CREATE TABLE IF NOT EXISTS Supplier (\"Supplier ID\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, \"Supplier Name\" varchar(255) NOT NULL UNIQUE, Tel varchar(255) NOT NULL);\n" ,
        "CREATE TABLE IF NOT EXISTS \"Parts Ordered\" (\"Order ID\"  INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, Quantity integer(10) NOT NULL, \"Date Ordered\" date NOT NULL, \"Date Expected\" date NOT NULL, \"Supplier ID\" integer(10) NOT NULL, \"Part ID\" integer(10) NOT NULL, FOREIGN KEY(\"Supplier ID\") REFERENCES Supplier(\"Supplier ID\"), FOREIGN KEY(\"Part ID\") REFERENCES \"Parts Stock\"(\"Part ID\"));\n" ,
        "CREATE TABLE IF NOT EXISTS \"Parts Stock\" (\"Part ID\"  INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, \"Part Name\" varchar(255) NOT NULL, \"Part Description\" text, Stock integer(10) NOT NULL, Price REAL NOT NULL);\n" ,
        "CREATE TABLE IF NOT EXISTS Mechanic (\"Mechanic ID\"  INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, \"Mechanic Name\" varchar(255) NOT NULL, \"Hourly Rate\" integer(10) NOT NULL, Bay integer(10) NOT NULL);\n" ,
        "CREATE TABLE IF NOT EXISTS Mot (\"Mot ID\"  INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, Pass VARCHAR(255) , Reason text, \"Scheduled MaintenanceScheduled Maintenance ID\" integer(10) NOT NULL , FOREIGN KEY(\"Scheduled MaintenanceScheduled Maintenance ID\") REFERENCES \"Scheduled Maintenance\"(\"Scheduled Maintenance ID\"));\n" ,
        "CREATE TABLE IF NOT EXISTS Service (\"Service ID\"  INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, Type varchar(255) NOT NULL, \"Next Service\" VARCHAR(255) NOT NULL, \"Scheduled MaintenanceScheduled Maintenance ID\" integer(10) NOT NULL, FOREIGN KEY(\"Scheduled MaintenanceScheduled Maintenance ID\") REFERENCES \"Scheduled Maintenance\"(\"Scheduled Maintenance ID\"));\n" ,
        "CREATE TABLE IF NOT EXISTS \"Scheduled Maintenance\" (\"Scheduled Maintenance ID\"  INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, Type varchar(255) NOT NULL, \"BookingBooking ID\" integer(10) NOT NULL, FOREIGN KEY(\"BookingBooking ID\") REFERENCES Booking(\"Booking ID\"));\n" ,
        "CREATE TABLE IF NOT EXISTS Booking (\"Booking ID\"  INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, \"Start Date\" date NOT NULL,\"Start Time\" time NOT NULL, \"End Date\" date NOT NULL, \"End Time\" time NOT NULL, Bay integer(10) NOT NULL, \"Total Cost\" real(10) NOT NULL, Type varchar(255) NOT NULL, Mileage real(10) NOT NULL, isPaid bit NOT NULL, \"MechanicMechanic ID\" integer(10) NOT NULL, \"VehicleVehicle ID\" integer(10) NOT NULL, \"Customer AccountsCustomer ID\" integer(10) NOT NULL, FOREIGN KEY(\"MechanicMechanic ID\") REFERENCES \"Mechanic\"(\"Mechanic ID\"), FOREIGN KEY(\"Customer AccountsCustomer ID\") REFERENCES \"Customer Accounts\"(\"Customer ID\"), FOREIGN KEY(\"VehicleVehicle ID\") REFERENCES Vehicle(\"Vehicle ID\"));\n",
        "CREATE TABLE IF NOT EXISTS Warranty (\"Warranty ID\"  INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, \"Company Name\" varchar(255) NOT NULL, Expiry date NOT NULL, Line1 varchar(255) NOT NULL, City varchar(255) NOT NULL, Postcode varchar(255) NOT NULL);\n" ,
        "CREATE TABLE IF NOT EXISTS Vehicle (\"Vehicle ID\"  INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, Type varchar(255) NOT NULL, Reg varchar(255) UNIQUE NOT NULL, Make varchar(255) NOT NULL, Model varchar(255) NOT NULL, \"Engine Size\" real(10) NOT NULL, \"Fuel Type\" varchar(255) NOT NULL, Color varchar(255) NOT NULL, \"Mot Renewal Date\" date NOT NULL, \"Customer AccountsCustomer ID\" integer(10), \"WarrantyWarranty ID\" integer(10), FOREIGN KEY(\"WarrantyWarranty ID\") REFERENCES Warranty(\"Warranty ID\"), FOREIGN KEY(\"Customer AccountsCustomer ID\") REFERENCES \"Customer Accounts\"(\"Customer ID\"));\n" ,
        "CREATE TABLE IF NOT EXISTS \"Customer Accounts\" (\"Customer ID\"  INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, \"First Names\" varchar(255) NOT NULL, \"Last Name\" varchar(255) NOT NULL,\"Customer Type\" varchar(255) NOT NULL,\"Address \" varchar(255) NOT NULL,  Tel varchar(13) NOT NULL,Email varchar(255) NOT NULL);",
        "CREATE TABLE IF NOT EXISTS users(userid INTEGER PRIMARY KEY AUTOINCREMENT,username VARCHAR(50) NOT NULL UNIQUE,password TEXT NOT NULL,salt TEXT NOT NULL,customerprivileges BOOLEAN NOT NULL,vehicleprivileges BOOLEAN NOT NULL,darprivileges BOOLEAN NOT NULL,partprivileges BOOLEAN NOT NULL, schedmaintprivileges BOOLEAN NOT NULL,usersprivileges BOOLEAN NOT NULL, color varchar(255));"
    };
    
    /**
     * Remove the database tables.
     */
    private static final String[] drop = {"DROP TABLE IF EXISTS \"Parts Installed\"", 
        "DROP TABLE IF EXISTS Supplier", "DROP TABLE IF EXISTS \"Parts Ordered\"", "DROP TABLE IF EXISTS \"Parts Stock\"",
        "DROP TABLE IF EXISTS Mechanic", "DROP TABLE IF EXISTS \"Diagnosis and Repair\"", "DROP TABLE IF EXISTS Mot",
        "DROP TABLE IF EXISTS Service", "DROP TABLE IF EXISTS \"Scheduled Maintenance\"", "DROP TABLE IF EXISTS Booking",
        "DROP TABLE IF EXISTS Warranty", "DROP TABLE IF EXISTS Vehicle", "DROP TABLE IF EXISTS Address", 
        "DROP TABLE IF EXISTS \"Customer Accounts\"", "DROP TABLE IF EXISTS users"
    };
    
    /**
     * 
     */
    public static final String testData =
            // Customer Details
            "INSERT INTO \"Customer Accounts\" VALUES (null, 'Michael John', 'Smith', 'private', '32 Jackson Road,place,Essex, I13 2RX', 0015258889, 'theMichael@a.yahoo.co.uk');\n" +
            "INSERT INTO \"Customer Accounts\" VALUES (null, 'Steve', 'Stevenson', 'business', '34 Corporate Road,Somehwere,London, E11 7GY', 0204246689, 'steve_Stevenson@GoldmanSachs.com');\n" +
            "INSERT INTO \"Customer Accounts\" VALUES (null, 'Fred', 'Flintstone', 'private', '222 Rocky Way,Somehwere,Bedrock,B17 3TY', 07723456789, 'Yabba@CartoonNetwork.com');\n" +
            "INSERT INTO \"Customer Accounts\" VALUES (null, 'Bob', 'Builder', 'business', '34 Construction Road,Somehwere,Harlem, H11 6TY', 0013246689, 'bob01@hotmail.com');\n" +
            "INSERT INTO \"Customer Accounts\" VALUES (null, 'Homer', 'Simpson', 'private', '742 Evergreen Terrace,Somehwere,Springfield, S13 ERX', 0755675589, 'Homer$impson@a.gmail.com');\n" +
            "INSERT INTO \"Customer Accounts\" VALUES (null, 'Peter', 'Griffin', 'business', '31 Spooner Street,Quahog,Rhode Island', 11247586789, 'pETER@Aol.com');\n" +
            "INSERT INTO \"Customer Accounts\" VALUES (null, 'Elmer', 'Fudd', 'private', '23 The Hood,Compton,LA,LA17 3XY', 0907456789, 'bigElmer09@CartoonNetwork.com');\n" +
            "INSERT INTO \"Customer Accounts\" VALUES (null, 'Steve', 'Stevenson', 'business', '34 Corporate Road,Somehwere,London, E11 7GY', 0204246689, 'steve_Stevenson@GoldmanSachs.com');\n" +
            "INSERT INTO \"Customer Accounts\" VALUES (null, 'Jack', 'Sparrow', 'private', '20 The Black Perl,Tortuga,The Bronx', 07247586789, 'CaptainJack01@Aol.com');\n" +
            
            // Warranty
            "INSERT INTO Warranty (\"Company Name\", Expiry, Line1, City, Postcode) VALUES ('Aviva', '2016-12-12', '2 Random Street', 'Manchester', 'ME2 4QP');\n" +
            "INSERT INTO Warranty (\"Company Name\", Expiry, Line1, City, Postcode) VALUES ('SaveMyCar', '2018-12-12', '125 Save Road', 'London', 'EC1 9TH');\n" +
            "INSERT INTO Warranty (\"Company Name\", Expiry, Line1, City, Postcode) VALUES ('Warranty', '2014-10-08', '13 Expired Street', 'Glasgow', 'N12 3ER');\n" +

            // Vehciles
            "INSERT INTO Vehicle VALUES (null, 'car', 'ND74 JUD', 'Volkswagen', 'Polo', '1.4', 'petrol', 'black', '2015-11-01', 1, 1);\n" +
            "INSERT INTO Vehicle VALUES (null, 'car', 'EJ06 JVL', 'BMW', 'M4', '4.5', 'petrol', 'yellow', '2015-12-27', 2, 2);\n" +
            "INSERT INTO Vehicle VALUES (null, 'car', 'JE54 DED', 'Volkswagen', 'Golf', '1.6', 'petrol', 'blue', '2015-09-13', 3, 2);\n" +
            "INSERT INTO Vehicle VALUES (null, 'car', 'SH41 LZZ', 'Porsche', '911', '4.0', 'petrol', 'black', '2015-07-03', 4, 0);\n" +
            "INSERT INTO Vehicle VALUES (null, 'car', 'YD51 GTH', 'Audi', 'A6', '2.0', 'petrol', 'black', '2016-01-12', 5, 1);" +
            "INSERT INTO Vehicle VALUES (null, 'car', 'NF55 TUP', 'Audi', 'Q7', '2.0', 'diesel', 'navy', '2016-01-27', 6, 1);" +
            "INSERT INTO Vehicle VALUES (null, 'car', 'LP14 CUD', 'Ford', 'Focus', '2.0', 'petrol', 'green', '2015-11-08', 7, 0);" +
            "INSERT INTO Vehicle VALUES (null, 'car', 'LO12 LSI', 'Audi', 'A3', '1.4', 'petrol', 'silver', '2015-10-17', 8, 3);" +
            "INSERT INTO Vehicle VALUES (null, 'car', 'TW24 POP', 'Fiat', 'Punto', '1.1', 'petrol', 'silver', '2015-12-30', 9, 3);" +
            
            // Mechanics
            "INSERT INTO Mechanic VALUES (null, 'Aragorn', 10, 1);\n" +
            "INSERT INTO Mechanic VALUES (null, 'Gimley', 9, 2);\n" +
            "INSERT INTO Mechanic VALUES (null, 'Gollum', 12, 3);\n" +
            "INSERT INTO Mechanic VALUES (null, 'Gandalf', 15, 4);\n" +
            "INSERT INTO Mechanic VALUES (null, 'Sam', 5, 5);\n" +
            "INSERT INTO Mechanic VALUES (null, 'Frodo', 6, 6);\n" +
            
            // Suppliers
            "INSERT INTO Supplier VALUES(null, 'Daves Bits', '01454235782');\n" +
            "INSERT INTO Supplier VALUES(null, 'Pedros Parts', '0216546954');\n" +
            "INSERT INTO Supplier VALUES(null, 'Engine Inc.', '0205484888');\n" +
            "INSERT INTO Supplier VALUES(null, 'Headlights Ltd.', '01456456465');\n" +
            "INSERT INTO Supplier VALUES(null, 'The Chop Shop', '01454235782');\n" +
            "INSERT INTO Supplier VALUES(null, 'Halfords', '01454235782');\n" +
            
            // Parts Stock
            "INSERT INTO \"Parts Stock\" VALUES(null, 'Engine', 'Makes the car go vroom', 4, 400);\n" +
            "INSERT INTO \"Parts Stock\" VALUES(null, 'Headlights', 'Illuminating', 2, 33.99);\n" +
            "INSERT INTO \"Parts Stock\" VALUES(null, 'Steering Wheel', 'Drives me around the bend', 0, 20);\n" +
            "INSERT INTO \"Parts Stock\" VALUES(null, 'Wheel', 'Spin me right round', 4, 12.99);\n" +
            "INSERT INTO \"Parts Stock\" VALUES(null, 'Flux Capacitor', 'Stay under 88mph', 1, 40000.01);\n" +
            "INSERT INTO \"Parts Stock\" VALUES(null, 'Stereo System', 'Witty description', 9, 60);\n" +
            "INSERT INTO \"Parts Stock\" VALUES(null, 'Preused Gearbox', 'For a smooth transmission', 100, 1.50);\n" +
            "INSERT INTO \"Parts Stock\" VALUES(null, 'Drive Shaft', 'heh shaft...', 2, 100);\n" +
            "INSERT INTO \"Parts Stock\" VALUES(null, 'Air Bag', 'Inflate for best results', 10, 10.20);\n" +
            "INSERT INTO \"Parts Stock\" VALUES(null, 'Driver', 'Required for smooth operation', 1, 1);\n" +
            
            // Parts Ordered
            "INSERT INTO \"Parts Ordered\" VALUES(null, 5, '2015-03-27', '2015-03-29', 5, 7);\n" +
            "INSERT INTO \"Parts Ordered\" VALUES(null, 8, '2015-03-26', '2015-04-01', 2, 3);\n" +
            "INSERT INTO \"Parts Ordered\" VALUES(null, 3, '2015-03-25', '2015-04-02', 4, 6);\n" +
            "INSERT INTO \"Parts Ordered\" VALUES(null, 2, '2015-03-27', '2015-03-28', 2, 2);\n" +
            
            // Parts Installed and Warranties
            "INSERT INTO Warranty VALUES(null, 'WarrantiesRUs', '2015-12-25', '28 Business Street', 'London', 'E1 111');\n" +
            "INSERT INTO \"Parts Installed\" VALUES(null, '2014-12-25', 1, 4, 2);\n" + 
            "INSERT INTO Warranty VALUES(null, 'WarrantiesRUs', '2013-12-25', '31 Business Street', 'London', 'E1 111');\n" +
            "INSERT INTO \"Parts Installed\" VALUES(null, '2014-12-25', 1, 5, 3);\n" + 
            "INSERT INTO Warranty VALUES(null, 'WarrantiesRUs', '2015-11-12', '39 Business Street', 'London', 'E1 111');\n" +
            "INSERT INTO \"Parts Installed\" VALUES(null, '2014-11-12', 2, 6, 4);\n" + 
            "INSERT INTO Warranty VALUES(null, 'WarrantiesRUs', '2013-02-02', '57 Business Street', 'London', 'E1 111');\n" +
            "INSERT INTO \"Parts Installed\" VALUES(null, '2012-02-02', 3, 7, 6);\n" + 
            "INSERT INTO Warranty VALUES(null, 'WarrantiesRUs', '2016-12-25', '79 Business Street', 'London', 'E1 111');\n" +
            "INSERT INTO \"Parts Installed\" VALUES(null, '2015-12-25', 4, 8, 5);\n" + 
            
            // Bookings to test Customer Booking Reminders (Scheduled Miantenace) will always have ended 11 months ago
            "INSERT INTO Booking VALUES (null, date('now','-11 months'),'09:00:00',date('now','-11 months'),'10:00:00','3','150.0','SCHEMAIN','20000.0','0','3','1','1');\n" +
            "INSERT INTO \"Scheduled Maintenance\" VALUES (null, 'SERVICE', '1');\n" +
            "INSERT INTO Service VALUES (null, 'MILEAGE','5000 miles','1');\n" +
            "INSERT INTO Booking VALUES (null, date('now','-11 months'),'09:00:00',date('now','-11 months'),'10:00:00','5','30.0','SCHEMAIN','40000.0','0','5','2','2');\n" +
            "INSERT INTO \"Scheduled Maintenance\" VALUES (null, 'MOT', '2');\n" +
            "INSERT INTO Mot (\"Scheduled MaintenanceScheduled Maintenance ID\") VALUES ('2');\n" + 
            
            // Bookings to test Customer Booking Reminders (Diagnosis and Repair) will always start tomorrow
            "INSERT INTO Booking VALUES (null, date('now','+1 day'),'09:00:00',date('now','+5 days'),'09:00:00','6','12.0','DIAGREP','500000.0','0','6','3','3');" +
            
            // Service Bookings for Today
            "INSERT INTO Booking VALUES (null, date('now'),'09:00:00',date('now'),'10:00:00','1','150.0','SCHEMAIN','20000.0','0','1','3','3');\n" +
            "INSERT INTO \"Scheduled Maintenance\" VALUES (null, 'SERVICE', '4');\n" +
            "INSERT INTO Service (Type, \"Next Service\",\"Scheduled MaintenanceScheduled Maintenance ID\") VALUES ('TIME','1 year','3');" +
            "INSERT INTO Booking VALUES (null, date('now'),'09:30:00',date('now'),'10:30:00','2','150.0','SCHEMAIN','200000.0','0','2','4','4');\n" +
            "INSERT INTO \"Scheduled Maintenance\" (Type, \"BookingBooking ID\") VALUES ('SERVICE', '5');\n" +
            "INSERT INTO Service (Type, \"Next Service\",\"Scheduled MaintenanceScheduled Maintenance ID\") VALUES ('TIME','1 year','4');\n" +
            "INSERT INTO Booking VALUES (null, date('now', '+1 day'),'11:00:00',date('now', '+1 day'),'12:00:00','3','150.0','SCHEMAIN','25000.0','0','3','5','5');\n" +
            "INSERT INTO \"Scheduled Maintenance\" (Type, \"BookingBooking ID\") VALUES ('SERVICE', '6');\n" +
            "INSERT INTO Service (Type, \"Next Service\",\"Scheduled MaintenanceScheduled Maintenance ID\") VALUES ('MILEAGE','20000 miles','5');\n" +
            
            // MoT Bookings for Today
            "INSERT INTO Booking VALUES (null, date('now'),'09:00:00',date('now'),'10:00:00','3','30.0','SCHEMAIN','65000.0','0','3','6','6');\n" +
            "INSERT INTO \"Scheduled Maintenance\" (Type, \"BookingBooking ID\") VALUES ('MOT', '7');\n" +
            "INSERT INTO Mot (\"Scheduled MaintenanceScheduled Maintenance ID\") VALUES ('6');\n" +
            "INSERT INTO Booking VALUES (null, date('now'),'09:30:00',date('now'),'10:30:00','5','30.0','SCHEMAIN','10000.0','0','5','7','7');\n" +
            "INSERT INTO \"Scheduled Maintenance\" (Type, \"BookingBooking ID\") VALUES ('MOT', '8');\n" +
            "INSERT INTO Mot (\"Scheduled MaintenanceScheduled Maintenance ID\") VALUES ('7');\n" +
            "INSERT INTO Booking VALUES (null, date('now', '+1 day'),'09:00:00',date('now', '+1 day'),'10:00:00','4','30.0','SCHEMAIN','130000.0','0','4','8','8');\n" +
            "INSERT INTO \"Scheduled Maintenance\" (Type, \"BookingBooking ID\") VALUES ('MOT', '9');\n" +
            "INSERT INTO Mot (\"Scheduled MaintenanceScheduled Maintenance ID\") VALUES ('8');" +
            
            "";
    private static Connection c;
    private static Statement s;
    private static ArrayList<String> holidays;
    
    /**
     * Method to create tables
     * @throws SQLException 
     */
    public static void createDb() throws SQLException {
        for(String q : tables)
            executeUpdate(q);
        
        // add default user everytime database is created
        new UsersController(null).addDefault();
    }
    
    /**
     * Method to drop tables
     * @throws SQLException 
     */
    public static void dropDb() throws SQLException {
        for(String q : drop)
            executeUpdate(q);
    }
    
    public static void addData() throws SQLException {
        executeUpdate(testData);
    }
    
    /**
     * Method to get Statement connection
     * @return Statement
     */
    private static Statement getStatement() {
        if (s == null) {
            try {
                //c.close();
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:database.db");
                s = c.createStatement();
                return s;
            } catch (ClassNotFoundException | SQLException e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
                return null;
            }
        } else {
                return s;
        }   
    }
    
    /**
     * Executes a database query
     * @param query - String value representing query to be executed
     * @throws SQLException 
     */
    public static void executeUpdate(String query) throws SQLException {
        getStatement().executeUpdate(query);
    }
    
    /**
     * Executes and SQL query returning a ResultSet
     * @param query - String value representing query to be executed
     * @return - ResultSet that can be used to handle the data received
     * @throws SQLException 
     */
    public static ResultSet executeQuery(String query) throws SQLException {
        ResultSet rs = getStatement().executeQuery(query);
        return rs;
    }
    
    private static void close() throws SQLException {
        c.close();
    }
    
    /**
     * Method that returns a DefaultTableModel from a given ResultSet
     * JTable table = new JTable()
     * table.setModel(Database.getTable(Database.executeQuery("Select * from Vehicles")));
     * http://stackoverflow.com/questions/10620448/most-simple-code-to-populate-jtable-from-resultset
     * @param rs - ResultSet from SELECT query
     * @return - DefaultTableModel is returned which can be assigned to a JTable
     * via the setTable(DefaultTableModel model) method
     * @throws SQLException 
     */
    public static DefaultTableModel getTable(ResultSet rs) throws SQLException {
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int numberOfColumns = metaData.getColumnCount();
            Vector columnNames = new Vector();

            // Get the column names
            for (int column = 0; column < numberOfColumns; column++) {
                columnNames.addElement(metaData.getColumnLabel(column + 1));
            }

            // Get all rows.
            Vector rows = new Vector();

            while (rs.next()) {
                Vector newRow = new Vector();

                for (int i = 1; i <= numberOfColumns; i++) {
                    newRow.addElement(rs.getObject(i));
                }

                rows.addElement(newRow);
            }
            
            return new DefaultTableModel(rows, columnNames);
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
    
    /**
     * Sets the holidays for the garage;
     * stackoverflow.com/questions/13716314/java-get-json-object-array
     * @throws MalformedURLException
     * @throws IOException 
     */
    public static void setHolidays() throws MalformedURLException, IOException {
        holidays = new ArrayList<>();
        InputStream is = new URL("https://www.gov.uk/bank-holidays.json").openStream();
        try {
            // read data
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            
            // build string
            StringBuilder sb = new StringBuilder();
            
            // store line while reading
            String cp = new String();

            // read line and store into sb
            while((cp=rd.readLine())!=null){
                sb.append(cp);
            }
            
            // split data by date
            String[] split = sb.toString().split("\"date\":\"");
            
            for(int i=0; i<split.length; i++) {
                // get the date as string
                String date = split[i].substring(0, 10);
                // store the date
                holidays.add(date);
            }
            
        } finally {
          is.close();
        }  
    }

    public static ArrayList<String> getHolidays() {
        return holidays;
    }
    
    public static void loadTheme(String username) {
        try {
            ResultSet rs = Database.executeQuery("SELECT color FROM users WHERE username = '"+Res.username+"'");
            Res.setColor(rs.getString("color"));

        } catch (SQLException ex) {
            //JOptionPane.showMessageDialog(null, "Display preferences could not be loaded.");
        }
    }
    
    public static void main(String[] args) {
        try {
            Database.dropDb();
            System.out.println("Tables Dropped");
            Database.createDb();
            System.out.println("Tables Created");
            Database.executeUpdate(testData);
            //Database.executeQuery(null);
        } catch (SQLException ex) {
            System.out.println("Error: " +ex.getMessage());
        }
    }
}
