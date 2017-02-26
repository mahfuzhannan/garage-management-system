package common;

import assets.Res;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
 
/**
 * A Splash Screen displaying the Garage Management System Loading
 * Adapted from - http://stackoverflow.com/questions/20716819/splashscreen-in-java
 * @author Mahfuz
 */
public class SplashScreen extends JWindow {
    
    private JProgressBar bar;
    private boolean popup = false;
    private boolean loading = true;
    private JLabel info;

    public SplashScreen() {
        
    }
    
    public void showSplash() {
        JPanel content = (JPanel) getContentPane();
        content.setBackground(Res.BKG_COLOR);

        // Set the window's bounds, centering the window
        int width = 480;
        int height = 270;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        setBounds(x, y, width, height);

        // Set splash screen logo
        JLabel label = new JLabel(new ImageIcon(getClass().getResource("/assets/logo.png")));
        // Set progress bar
        label.setLayout(new BorderLayout());
        bar = new JProgressBar();
        bar.setMaximum(100);
        bar.setValue(0);
        label.add(bar, BorderLayout.SOUTH);
        
        info = new JLabel("");
        info.setForeground(Res.FONT_COLOR);
        label.add(info, BorderLayout.NORTH);
        
        content.add(label, BorderLayout.CENTER);
        setVisible(true);

        animateBar();
    }
    
    private void animateBar() {
        loadData();
        while(bar.getValue() != 100) {
            int val = bar.getValue() + 1;
            int sleep = 20;
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException ex) {
                // do nothing
            }
            bar.setValue(val);
        }
        while(popup || loading){
            // do nothing
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                // do nothing
            }
        }
        
        setVisible(false);
        View view = new View();
        view.setVisible(true);
    }
    
    private void loadData() {
        new Thread() {
            @Override
            public void run() {
                loading = true;
                setInfo("Loading Holidays...");
                try {
                    Database.setHolidays();
                } catch (IOException ex) {
                    popup = true;
                    JOptionPane.showMessageDialog(null, "Holidays were not loaded, ensure that an internet connection is established");
                    popup = false;
                }

                setInfo("Loading Database...");
                try {
                    Database.createDb();

                } catch (SQLException ex) {
                    popup = true;
                    JOptionPane.showMessageDialog(null, "Database was not loaded.");
                    popup = false;
                }
                
                /*setInfo("Fixing up preferences...");
                try {
                    ResultSet rs = Database.executeQuery("SELECT color FROM users WHERE username = '"+Database.getUsername()+"'");
                    Res.setColor(rs.getString("color"));

                } catch (SQLException ex) {
                    popup = true;
                    JOptionPane.showMessageDialog(null, "Display preferences could not be loaded.");
                    popup = false;
                }*/
                loading = false;
            }
        }.start();
    }
    
    public void setInfo(String info) {
        this.info.setText(info);
        revalidate();
        repaint();
    }
    
    public static void main (String args[]) {
        SplashScreen test = new SplashScreen();
        test.showSplash();
    }
}