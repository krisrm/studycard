/*
 * WindowResizeListener.java
 * 
 * Copyright (c) 2010 Team Alpha 
 */
package util;

import java.util.EventListener;

/**
 * From http://coding.derkeiler.com/Archive/Java/comp.lang.java.gui/2005-05/msg00287.html
 * @see WindowResizeMonitor
 */
public interface WindowResizeListener extends EventListener {
	/*
	 * From
	 * http://coding.derkeiler.com/Archive/Java/comp.lang.java.gui/2005-05/msg00287
	 * .html
	 */
	void windowResized(WindowResizeEvent e);
}