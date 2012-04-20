/*
 * WindowResizeEvent.java
 * 
 * http://coding.derkeiler.com/Archive/Java/comp.lang.java.gui/2005-05/msg00287.html
 */
package util;

import java.awt.Dimension;
import java.util.EventObject;

/**
 *From http://coding.derkeiler.com/Archive/Java/comp.lang.java.gui/2005-05/msg00287.html
 *
 *Used in the swap pane
 *
 *@see MainView
 *@see JSwapPane
 */
public class WindowResizeEvent extends EventObject {
	/*
	 * From
	 * http://coding.derkeiler.com/Archive/Java/comp.lang.java.gui/2005-05/msg00287
	 * .html
	 */
	private static final long serialVersionUID = -7273289346336519447L;
	private Dimension oldSize;
	private Dimension newSize;

	/**
	 * Resizes a window
	 * @param source - the window object
	 * @param oldSize - the old size of the window
	 * @param newSize - the new size of the window
	 */
	public WindowResizeEvent(Object source, Dimension oldSize, Dimension newSize) {
		super(source);
		this.oldSize = oldSize;
		this.newSize = newSize;
	}

	public Dimension getOldSize() {
		return oldSize;
	}

	public Dimension getNewSize() {
		return newSize;
	}
}