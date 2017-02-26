/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package settings;

import assets.GMButton;
import assets.Res;
import common.Database;
import common.View;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 *
 * @author mafz
 */
public class ColorPanel extends JPanel {
    private ColorRadio grey, bluegrey, brown, orange, yellow, green, lightgreen, teal, cyan, blue, purple, lightpurple, pink, red, selected;
    private ButtonGroup group;
    private final View view;
    private final SettingsView settingsView;
    
    public ColorPanel(final View view, SettingsView panel) {
        this.view = view;
        this.settingsView = panel;
        setLayout(new GridBagLayout());
        setBackground(Res.BKG_COLOR);
        
        grey = new ColorRadio("#212121", "#424242", "#616161", "#FAFAFA", "grey");
        bluegrey = new ColorRadio("#263238", "#37474f", "#455a64", "#eceff1", "bluegrey");
        brown = new ColorRadio("#3e2723", "#4e342e", "#5d4037", "#efebe9", "brown");
        orange = new ColorRadio("#bf360c", "#d84315", "#e64a19", "#fbe9e7", "orange");
        yellow = new ColorRadio("#f57f17", "#f9a825", "#fbc02d", "#fffde7", "yellow");
        lightgreen = new ColorRadio("#33691e", "#558b2f", "#689f38", "#f1f8e9", "lightgreen");
        green = new ColorRadio("#1b5e20", "#2e7d32", "#388e3c", "#e8f5e9", "green");
        teal = new ColorRadio("#004d40", "#00695c", "#00796b", "#e0f2f1", "teal");
        cyan = new ColorRadio("#006064", "#00838f", "#0097a7", "#e0f7fa", "cyan");
        blue = new ColorRadio("#1a237e", "#283593", "#303f9f", "#3f51b5", "blue");
        purple = new ColorRadio("#311b92", "#4527a0", "#512da8", "#ede7f6", "purple");
        lightpurple = new ColorRadio("#4a148c", "#6a1b9a", "#7b1fa2", "#f3e5f5", "lightpurple");
        pink = new ColorRadio("#880e4f", "#ad1457", "#c2185b", "#fce4ec", "pink");
        red = new ColorRadio("#b71c1c", "#c62828", "#d32f2f", "#ffebee", "red");
        
        selected = grey;
        
        group = new ButtonGroup();
        group.add(grey);
        group.add(bluegrey);
        group.add(brown);
        group.add(orange);
        group.add(yellow);
        group.add(lightgreen);
        group.add(green);
        group.add(teal);
        group.add(cyan);
        group.add(blue);
        group.add(purple);
        group.add(lightpurple);
        group.add(pink);
        group.add(red);
        
        JPanel colors = new JPanel();
        colors.setOpaque(false);
        colors.add(grey);
        colors.add(bluegrey);
        colors.add(brown);
        colors.add(orange);
        colors.add(yellow);
        colors.add(lightgreen);
        colors.add(green);
        colors.add(teal);
        colors.add(cyan);
        colors.add(blue);
        colors.add(purple);
        colors.add(lightpurple);
        colors.add(pink);
        colors.add(red);
        
        GMButton save = new GMButton("SAVE");
        save.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Database.executeUpdate("UPDATE users SET color = '"+selected.name+"' WHERE username = '"+Res.username+"'");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(view, "Could not save preferences", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        
        colors.add(save);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1;
        
        gbc.gridx = 0;
        gbc.gridy = 0;        
        this.add(colors, gbc);
        
        //gbc.gridy = 1;
        //this.add(save, gbc);
    }
    
    public class ColorRadio extends JRadioButton {
        public Color bkg, btn, shadow, font;
        public String name;

        public ColorRadio(String bkg900, String btn800, String shadow700, String font50, String name) {
            super();
            bkg = Color.decode(bkg900);
            btn = Color.decode(btn800);
            shadow = Color.decode(shadow700);
            font = Color.decode(font50);
            this.name = name;
            init();
        }
        
        private void init() {
            this.setBackground(bkg);
            
            this.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    if(isSelected()) {
                        Res.BKG_COLOR = bkg;
                        Res.BTN_COLOR = btn;
                        Res.SHADOW = shadow;
                        Res.FONT_COLOR = font;
                    }
                    selected = ((ColorRadio)e.getItem());
                    setBackground(Res.BKG_COLOR);
                    view.repaint();
                    settingsView.paintPanels();
                }
            });
        }   
    }

    public ColorRadio getGrey() {
        return grey;
    }

    public ColorRadio getBluegrey() {
        return bluegrey;
    }

    public ColorRadio getBrown() {
        return brown;
    }

    public ColorRadio getOrange() {
        return orange;
    }

    public ColorRadio getYellow() {
        return yellow;
    }

    public ColorRadio getLightgreen() {
        return lightgreen;
    }

    public ColorRadio getGreen() {
        return green;
    }

    public ColorRadio getTeal() {
        return teal;
    }

    public ColorRadio getCyan() {
        return cyan;
    }

    public ColorRadio getBlue() {
        return blue;
    }

    public ColorRadio getPurple() {
        return purple;
    }

    public ColorRadio getLightpurple() {
        return lightpurple;
    }

    public ColorRadio getRed() {
        return red;
    }
    
}
