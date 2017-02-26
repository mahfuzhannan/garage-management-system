/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;
import assets.GMButton;
import assets.Res;
import auth.AuthModel;
import auth.LoginBox;
import customer.GUI.CustomerView;
import customer.Logic.Customer;
import customer.Logic.CustomerController;
import diagrep.DiagRepModel;
import diagrep.DiagRepView;
import diagrep.DiagRepController;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Observable;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import parts.PartMainView;
import schemain.SchemainController;
import schemain.SchemainModel;
import schemain.SchemainView;
import schemain.mot.MotModel;
import schemain.service.ServiceModel;
import settings.SettingsView;
import vehicle.VehicleView;
import vehicle.VehicleController;

/**
 *
 * @author mafz
 */
public class View extends JFrame implements java.util.Observer, WindowListener {
    private JPanel navPanel, mainPanel;
    private GMButton customerBtn, vehicleBtn, partBtn, schemainBtn, diagrepBtn, settingsBtn, logoutBtn;
    private JFrame thisFrame = this;
	
    
    public final int PANEL_WIDTH = 1280, PANEL_HEIGHT = 720;

    public View() throws HeadlessException {
        init();
        initLogin();
    }
    
    private void init() {
        this.setTitle("GMSIS");
        // set layout
        BorderLayout borderLayout = new BorderLayout(20, 20);
        this.setLayout(borderLayout);
        this.setSize(PANEL_WIDTH, PANEL_HEIGHT);
        this.setMinimumSize(new Dimension(PANEL_WIDTH-100, PANEL_HEIGHT-200));
        this.getContentPane().setBackground(Res.BKG_COLOR);
        
        // set border for the frame
        EmptyBorder emptyBorder = new EmptyBorder(10, 10, 10, 10);
        
        // create navigation panel
        navPanel = new JPanel(new GridLayout(0, 6, 20, 20));
        navPanel.setSize(PANEL_WIDTH, 60);
        navPanel.setOpaque(false);
        navPanel.setBorder(emptyBorder);
        
        // create navigation buttons
        customerBtn = new GMButton("<html>Customers</html>", 
                new ImageIcon(getClass().getResource("/assets/customer64.png")));
        customerBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                mainPanel.removeAll();
                closeButtons();
                customerBtn.setOpen(true);
                
                // TODO add controller, view and model
                CustomerController c = new CustomerController(new Customer());
                CustomerView v = new CustomerView(c);
                
                mainPanel.add(v);
                
                revalidate();
                repaint();
            }
        });
        
        vehicleBtn = new GMButton("<html>Vehicles</html>", 
                new ImageIcon(getClass().getResource("/assets/vehicles64.png")));
        vehicleBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                mainPanel.removeAll();
                closeButtons();
                vehicleBtn.setOpen(true);

                // TODO add controller, view and model
                VehicleView view = new VehicleView();
                VehicleController controller = new VehicleController(view);
                mainPanel.add(view);

                revalidate();
                repaint();
            }
        });
        
        partBtn = new GMButton("<html>Parts</html>", 
                new ImageIcon(getClass().getResource("/assets/parts64.png")));
	partBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                mainPanel.removeAll();
                closeButtons();
                partBtn.setOpen(true);

                // TODO add controller, view and model
                PartMainView view = new PartMainView();
                mainPanel.add(view);

                revalidate();
                repaint();
            }
        });
        
        schemainBtn = new GMButton("<html>Scheduled<br/>Maintenance</html>", 
                new ImageIcon(getClass().getResource("/assets/schemain64.png")));
        schemainBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                mainPanel.removeAll();
                closeButtons();
                schemainBtn.setOpen(true);

                // TODO add controller, view and model
                SchemainView view = new SchemainView(getView());
                SchemainModel model = new SchemainModel();
                ServiceModel serviceModel = new ServiceModel();
                MotModel motModel = new MotModel();
                SchemainController controller = new SchemainController(view, model, serviceModel, motModel);
                mainPanel.add(view);

                revalidate();
                repaint();
            }
        });
        
        diagrepBtn = new GMButton("<html>Diagnostic &<br/>Repair</html>", 
                new ImageIcon(getClass().getResource("/assets/diagrep64.png")));
        diagrepBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                mainPanel.removeAll();

                DiagRepView view = new DiagRepView(getView());
                DiagRepModel model = new DiagRepModel();
                DiagRepController controller = new DiagRepController(view,model);
                mainPanel.add(view);

                closeButtons();
                diagrepBtn.setOpen(true);
                revalidate();
                repaint();
            }
        });

        // TODO change the icon.
        settingsBtn = new GMButton("<html>Settings</html>", 
        new ImageIcon(getClass().getResource("/assets/settings64.png")));
        settingsBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                mainPanel.removeAll();
                closeButtons();
                settingsBtn.setOpen(true);

                // TODO add controller, view and model
                SettingsView view = new SettingsView(getView());
                mainPanel.add(view);

                revalidate();
                repaint();
            }
        });
        
        // add buttons to navigation panel
        navPanel.add(customerBtn);
        navPanel.add(vehicleBtn);
        navPanel.add(partBtn);
        navPanel.add(schemainBtn);
        navPanel.add(diagrepBtn);
        navPanel.add(settingsBtn);
        //navPanel.add(logoutBtn);
        
        // create main panel
        mainPanel = new JPanel(); 
        mainPanel.setLayout(new GridLayout());
        //mainPanel.setSize(WIDTH, PANEL_HEIGHT);
        mainPanel.setOpaque(false);
        mainPanel.setBorder(emptyBorder);
        
        // add panels to main JFrame
        this.add(navPanel, BorderLayout.PAGE_START);
        this.add(mainPanel, BorderLayout.CENTER);
        
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	this.addWindowListener(this);
        this.setVisible(true);
        
        this.revalidate();
        this.repaint();
    }
    
    private void closeButtons() {
        customerBtn.setOpen(false);
        vehicleBtn.setOpen(false);
        partBtn.setOpen(false);
        schemainBtn.setOpen(false);
        diagrepBtn.setOpen(false);
        settingsBtn.setOpen(false);
    }
    
    private void initLogin() {
        AuthModel model = AuthModel.getInstance();
		model.addObserver(this);
		model.logout();
		mainPanel.removeAll();
		mainPanel.revalidate();
		mainPanel.repaint();
		GMButton login = new GMButton("Login");
		login.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new LoginBox(thisFrame);
			}
		});
		mainPanel.add(login);
		new LoginBox(this);
                
                repaint();
    }
    
    public View getView() {
        return this;
    }
    
    public static void main(String[] args) {
        View view = new View();
    }

	@Override
	public void update(Observable o, Object arg) {
		AuthModel model = AuthModel.getInstance();
		setTitle("GMSIS - " + model.getUsername());
		boolean[] privs = model.getPrivileges();
		mainPanel.removeAll();
		mainPanel.revalidate();
		mainPanel.repaint();
		customerBtn.setEnabled(privs[0]);
		vehicleBtn.setEnabled(privs[1]);
		diagrepBtn.setEnabled(privs[2]);
		partBtn.setEnabled(privs[3]);
		schemainBtn.setEnabled(privs[4]);
	}

	@Override
	public void windowOpened(WindowEvent e) {}
	@Override
	public void windowClosing(WindowEvent e) {
		int closing = 0;
		String[] options = new String[] {"Close Application", "Switch User", "Cancel"};
		closing = JOptionPane.showOptionDialog(thisFrame,
				"Do you want to close or switch user?",
				"Exiting?", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options,
				options[0]);
		if (closing == 0) System.exit(0);
		else if (closing == 1) initLogin();}
	@Override
	public void windowClosed(WindowEvent e) {}
	@Override
	public void windowIconified(WindowEvent e) {}
	@Override
	public void windowDeiconified(WindowEvent e) {}
	@Override
	public void windowActivated(WindowEvent e) {}
	@Override
	public void windowDeactivated(WindowEvent e) {}

    @Override
    public void repaint() {
        super.repaint(); //To change body of generated methods, choose Tools | Templates.
        getContentPane().setBackground(Res.BKG_COLOR);
    }
        
        
}
