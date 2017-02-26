/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assets;

import java.awt.Color;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.border.BevelBorder;

/**
 * JComboBox that has been customised to match any theme changes made
 * @author mafz
 */
public class GMComboBox extends JComboBox{

    public GMComboBox() {
        super();
        init();
    }

    public GMComboBox(ComboBoxModel aModel) {
        super(aModel);
        init();
    }

    public GMComboBox(Object[] items) {
        super(items);
        init();
    }

    public GMComboBox(Vector items) {
        super(items);
        init();
    }
    
    
    private void init() {
        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Res.SHADOW, Res.SHADOW));
        this.setForeground(Res.BKG_COLOR);
    }
    
}
