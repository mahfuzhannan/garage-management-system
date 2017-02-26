/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assets;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicBorders;

/**
 * JButton that has been customised to match any theme changes made
 * @author mafz
 */
public class GMButton extends JButton{
    private boolean isOpen = false;

    public GMButton(String text) {
        super(text);
    }

    public GMButton(String text, Icon icon) {
        super(text, icon);
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    @Override
    protected void init(String text, Icon icon) {
        super.init(text, icon); //To change body of generated methods, choose Tools | Templates.
        this.setBackground(Res.BTN_COLOR);
        this.setForeground(Res.BTN_FONT_COLOR);
        this.setFocusable(false);
        this.setFocusPainted(false);
        this.setBorderPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
        if(this.getModel().isPressed() || isOpen) {
            this.setContentAreaFilled(false);
            this.setBackground(Res.SHADOW);
        }
        else if(this.getModel().isRollover()) {
            this.setContentAreaFilled(true);
            this.setBackground(Res.SHADOW);
        } 
        else {
            this.setContentAreaFilled(true);
            this.setBackground(Res.BTN_COLOR);
        }
    }
    
    
}
