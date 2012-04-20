package util;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	private static final long serialVersionUID = -8920206701840350433L;
	private boolean frozen;
	private BufferedImage currentImage;

	public ImagePanel() {
		super();
		frozen = false;
	}

	public void setFrozen(boolean state) {
		frozen = state;
		if (frozen) {
			Rectangle tempBounds = getBounds().getBounds();
			currentImage = new BufferedImage(tempBounds.width,
					tempBounds.height, BufferedImage.TYPE_INT_ARGB);
			super.paint(currentImage.getGraphics());
		}
	}

	public boolean getFrozen() {
		return frozen;
	}
	
	public void clearImage(){
		Runtime r = Runtime.getRuntime();
		r.gc();
	}

	@Override
	public void paint(Graphics g) {
		if (frozen) {
			g.drawImage(currentImage, 0, 0, this);
		} else {
			super.paint(g);
		}
	}
}
