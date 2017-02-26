/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemain.mot;

import assets.GMButton;
import assets.Res;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Mahfuz
 */
public class MotCompleteDialog extends JDialog {
    private GMButton submit, cancel;
    private JRadioButton pass, fail;
    private ButtonGroup options;
    private JTextArea reason;
    private final Mot mot;

    public MotCompleteDialog(Frame owner, boolean modal, Mot mot) {
        super(owner, modal);
        this.mot = mot;
        init();
    }
    
    private void init() {
        this.getContentPane().setBackground(Res.BKG_COLOR);
        this.setLayout(new GridLayout(1, 0));
        this.setTitle("Mot Completion");
        
        // Main Panel to hold buttons and textarea
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Res.BKG_COLOR);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // set buttons
        submit = new GMButton("Submit", null);
        cancel = new GMButton("Cancel", null);
        
        // set radio buttons
        pass = new JRadioButton("Pass");
        pass.setBackground(Res.BKG_COLOR);
        pass.setForeground(Res.FONT_COLOR);
        fail = new JRadioButton("fail");
        fail.setBackground(Res.BKG_COLOR);
        fail.setForeground(Res.FONT_COLOR);
        
        // group radio buttons
        options = new ButtonGroup();
        options.add(pass);
        options.add(fail);
        
        // set reason for fail text area
        reason = new JTextArea(5, 35);
        reason.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.SHADOW, Res.SHADOW));
        reason.setForeground(Res.BKG_COLOR);
        reason.setLineWrap(true);
        
        // add buttons to panel (makes layout easier)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Res.BKG_COLOR);
        buttonPanel.setLayout(new GridLayout(1, 2, 5, 5));
        buttonPanel.add(submit);
        buttonPanel.add(cancel);
        
        // add radio buttons to seperate panel
        JPanel optionPanel = new JPanel(new GridLayout(2, 0));
        optionPanel.setBackground(Res.BKG_COLOR);
        optionPanel.add(pass);
        optionPanel.add(fail);
        
        // add text area to another panel
        JPanel reasonPanel = new JPanel(new GridBagLayout());
        reasonPanel.setBackground(Res.BKG_COLOR);
        reasonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty=1;
        gbc.gridx = 0;
        gbc.gridy=0;
        reasonPanel.add(optionPanel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 4;
        reasonPanel.add(reason, gbc);

        // label to act as title for dialogue
        JLabel label = new JLabel("Mot Completed");
        label.setForeground(Res.FONT_COLOR);
        
        mainPanel.add(label, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(reasonPanel, BorderLayout.CENTER);
        
        this.add(mainPanel);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);  
        this.pack();      
        this.setLocationRelativeTo(super.rootPane);
        
        
        // Set values
        Boolean isPass = mot.getPass();
        if(isPass) {
            pass.setSelected(true);
        }
        else if(!isPass) {
            fail.setSelected(true);
            reason.setText(mot.getReason());
        }
        
        
        
        /*
        ----------------------- SET EVENT LISTENERS --------------------------
        */            
        
        cancel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        pass.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mot.setPass(true);
                reason.setEnabled(false);
                reason.setBackground(Res.BKG_COLOR);
                reason.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.GREEN, Res.GREEN));
            }
        });
        
        fail.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                    reason.setEnabled(true);
                    reason.setBackground(Res.FONT_COLOR);
                    mot.setPass(false);
                    mot.setReason(null);
                if(reason.getText().equals("")) {
                    reason.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.RED, Res.RED));
                    reason.setToolTipText("Reason must be entered if MOT has failed!");
                }
            }
        });
        
        reason.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {            }

            @Override
            public void keyPressed(KeyEvent e) {            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(!reason.getText().equals("")) {
                    reason.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.GREEN, Res.GREEN));
                    mot.setReason(reason.getText());
                }
                else {
                    reason.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.RED, Res.RED));
                    reason.setToolTipText("Reason must be entered if MOT has failed!");
                }
            }
        });
        
        submit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(pass.isSelected() || (fail.isSelected()) && !reason.getText().equals("")) {
                        new MotModel().motCompleted(mot);
                        JOptionPane.showMessageDialog(rootPane, "Mot updated\n", "", JOptionPane.INFORMATION_MESSAGE);                    
                        dispose();
                    } 
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(rootPane, "Mot not updated\n"+ex.getMessage(), "Error", JOptionPane.ERROR);
                }
            }
        });
    }

    public JTextArea getReason() {
        return reason;
    }

    public ButtonGroup getOptions() {
        return options;
    }

    public JRadioButton getPass() {
        return pass;
    }

    public JRadioButton getFail() {
        return fail;
    }
}
