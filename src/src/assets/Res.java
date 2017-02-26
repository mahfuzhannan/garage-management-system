/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package assets;

import java.awt.Color;
import settings.ColorPanel;

/**
 * Resource file that contains data regarding the GUI design
 * @author mh309
 */
public class Res {
    public static Color BTN_COLOR = Color.decode("#424242");
    public static Color BTN_FONT_COLOR = Color.decode("#fafafa");
    public static Color SHADOW = Color.decode("#616161");

    public static Color FONT_COLOR = BTN_FONT_COLOR;
    public static Color BKG_COLOR = Color.decode("#212121");
    
    public static Color RED = Color.decode("#f44336");
    public static Color GREEN = Color.decode("#4caf50");
    
    public static int WIDTH = 1280, HEIGHT = 720;
    
    public static String username = "";
    
    public static void setColor(String str) {
        ColorPanel color = new ColorPanel(null,null);
        
        // default cold
        ColorPanel.ColorRadio cr = color.getGrey(); 
        
        // check saved color
        if(str.equals("bluegrey"))
            cr=color.getBluegrey();
        if(str.equals("brown"))
            cr=color.getBrown();
        if(str.equals("orange"))
            cr=color.getOrange();
        if(str.equals("yellow"))
            cr=color.getYellow();
        if(str.equals("lightgreen"))
            cr=color.getLightgreen();
        if(str.equals("green"))
            cr=color.getGreen();
        if(str.equals("teal"))
            cr=color.getTeal();
        if(str.equals("cyan"))
            cr=color.getCyan();
        if(str.equals("blue"))
            cr=color.getBlue();
        if(str.equals("purple"))
            cr=color.getPurple();
        if(str.equals("lightpurple"))
            cr=color.getLightpurple();
        if(str.equals("red"))
            cr=color.getRed();
        
        Res.BKG_COLOR = cr.bkg;
        Res.BTN_COLOR = cr.btn;
        Res.SHADOW = cr.shadow;
        Res.FONT_COLOR = cr.font;
    }
}
