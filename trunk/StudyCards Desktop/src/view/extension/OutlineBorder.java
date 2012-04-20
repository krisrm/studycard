package view.extension;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.border.Border;

import model.Settings;


public class OutlineBorder implements Border {

	private final BasicStroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT,
			BasicStroke.JOIN_BEVEL, 1 );

	private Insets insets;

	public OutlineBorder(int top, int left,
			int bottom, int right) {
		insets = new Insets(top, left, bottom, right);
	}

	public OutlineBorder() {
		this(0,0,0,0);
	}
	
	public OutlineBorder(String s) {
		this(Settings.BORDER_WIDTH, Settings.BORDER_WIDTH, Settings.BORDER_WIDTH, Settings.BORDER_WIDTH);
	}

	@Override
	public Insets getBorderInsets(Component c) {
		return insets;
	}

	@Override
	public boolean isBorderOpaque() {
		return false;
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y,
			int width, int height) {
		Graphics2D g2 = (Graphics2D) g;
		x += insets.left-1;
		y += insets.top-1;
		width -= insets.right+insets.left-1;
		height -= insets.bottom+insets.top-1;
		g2.setStroke(stroke);
		g2.setColor(new Color(106,106,106));
		g2.drawRect(x, y, width, height);
		g2.setColor(new Color(156,156,156));
		g2.drawLine(x+1, y+height+1, x+width+1, y+height+1);
		g2.drawLine(x+width+1, y+1, x+width+1, y+height+1);

	}


}
