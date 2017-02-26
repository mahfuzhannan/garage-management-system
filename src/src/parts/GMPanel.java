package parts;

import assets.Res;
import java.awt.LayoutManager;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * A pre-styled panel.
 * @author George
 */
public class GMPanel extends JPanel {

	/**
	 * Create an empty panel.
	 */
	public GMPanel() {
		init();
	}

	/**
	 * Create a new panel with specified {@link LayoutManager}.
	 * @param layout LayoutManager to be used.
	 */
	public GMPanel(LayoutManager layout) {
		super(layout);
		init();
	}

	/**
	 * Create a new panel with specified {@link LayoutManager} and some padding.
	 * @param layout LayoutManager to be used.
	 * @param borderWidth Size of padding in pixels.
	 */
	public GMPanel(LayoutManager layout, int borderWidth) {
		super(layout);
		init();
		setBorder(new EmptyBorder(borderWidth, borderWidth, borderWidth, borderWidth));
	}

	/**
	 * Creates a new panel with the specified padding.
	 * @param borderWidth Size of padding in pixels.
	 */
	public GMPanel(int borderWidth) {
		init();
		setBorder(new EmptyBorder(borderWidth, borderWidth, borderWidth, borderWidth));
	}

	private void init() {
		super.setBackground(Res.BKG_COLOR);
	}
	
}
