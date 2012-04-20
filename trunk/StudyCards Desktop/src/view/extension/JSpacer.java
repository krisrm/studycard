package view.extension;

import java.awt.Dimension;

import javax.swing.JComponent;

public class JSpacer extends JComponent {
	private static final long serialVersionUID = -1479947256190402834L;

	public JSpacer(int width, int height){
		this.setPreferredSize(new Dimension(width, height));
	}
}
