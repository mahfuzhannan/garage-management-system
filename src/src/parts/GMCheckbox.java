package parts;

import assets.Res;
import javax.swing.JCheckBox;

/**
 * A Pre-styled checkbox
 * @author George
 */
public class GMCheckbox extends JCheckBox {
	
	/**
	 * Creates a new CheckBox which has GM styling already applied.
	 * @param text	The text that the checkbox will display.
	 */
	public GMCheckbox(String text) {
		super(text);
		setBackground(Res.BKG_COLOR);
		setForeground(Res.FONT_COLOR);
	}
}
