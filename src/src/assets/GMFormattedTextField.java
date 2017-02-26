/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assets;

import java.text.Format;
import javax.swing.JFormattedTextField;
import javax.swing.border.BevelBorder;

/**
 * JFormattedTextField that has been customised to match any theme changes made
 * @author mafz
 */
public class GMFormattedTextField extends JFormattedTextField{

    public GMFormattedTextField() {
        super();
        init();
    }
    
    public GMFormattedTextField(AbstractFormatterFactory factory) {
        super(factory);
        init();
    }

    public GMFormattedTextField(Format format) {
        super(format);
        init();
    }

    public GMFormattedTextField(AbstractFormatter formatter) {
        super(formatter);
        init();
    }
    
    private void init() {
        this.setBorder(new BevelBorder(BevelBorder.LOWERED, Res.SHADOW, Res.SHADOW));
        this.setForeground(Res.BKG_COLOR);
    }
}
