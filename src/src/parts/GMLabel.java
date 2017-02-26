package parts;

import assets.Res;
import javax.swing.JLabel;

/**
 * A pre-styled label.
 * @author George
 */
public class GMLabel extends JLabel {

	/**
	 * Create an empty label.
	 */
	public GMLabel() {
		super.setForeground(Res.FONT_COLOR);
	}
	
	/**
	 * Create a new label with the specified text.
	 * @param text	Text to be displayed
	 */
	public GMLabel(String text) {
		super(text);
		super.setForeground(Res.FONT_COLOR);
	}
}
