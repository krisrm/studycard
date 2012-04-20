/*
 * WindowResizeMonitor.java
 * 
 * Copyright (c) 2010 Team Alpha 
 */
package util;

import java.awt.Dimension;
import java.awt.Window;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * FROM http://coding.derkeiler.com/Archive/Java/comp.lang.java.gui/2005-05/msg00287.html
 * Used in the JSwapPane
 * @see WindowResizeEvent
 */
public class WindowResizeMonitor implements Runnable {
	/*
	 * From
	 * http://coding.derkeiler.com/Archive/Java/comp.lang.java.gui/2005-05/msg00287
	 * .html
	 */
	private static final HashMap<Window, WindowResizeMonitor> WINDOW_MAP = new HashMap<Window, WindowResizeMonitor>();

	private List<WindowResizeListener> listeners = new ArrayList<WindowResizeListener>();
	private boolean run = false;
	private Window window;

	private WindowResizeMonitor(Window window) {
		this.window = window;
	}

	public static void register(Window window, WindowResizeListener listener) {
		WindowResizeMonitor monitor = WINDOW_MAP.get(window);

		if (monitor == null) {
			monitor = new WindowResizeMonitor(window);
			WINDOW_MAP.put(window, monitor);
		}
		monitor.add(listener);
	}

	public static void unregister(Window window, WindowResizeListener listener) {
		WindowResizeMonitor monitor = WINDOW_MAP.get(window);

		if (monitor != null) {
			monitor.remove(listener);
		}
	}

	private synchronized void add(WindowResizeListener listener) {
		listeners.add(listener);

		if (!run) {
			run = true;
			new Thread(this).start();
		}
	}

	private synchronized void remove(WindowResizeListener listener) {
		listeners.remove(listener);

		if (run && listeners.isEmpty()) {
			run = false;
		}
	}

	public void run() {
		Dimension oldSize = window.getSize();

		try {
			while (run) {
				Thread.sleep(60);

				Dimension curSize = window.getSize();
				if (!oldSize.equals(curSize)) {
					fireWindowResizeEvent(new WindowResizeEvent(window,
							oldSize, curSize));

					oldSize = curSize;
				}
			}
		} catch (InterruptedException e) {
		}
	}

	private void fireWindowResizeEvent(WindowResizeEvent event) {
		Iterator<WindowResizeListener> it = listeners.iterator();

		while (it.hasNext()) {
			WindowResizeListener l = it.next();
			l.windowResized(event);
		}
	}
}
