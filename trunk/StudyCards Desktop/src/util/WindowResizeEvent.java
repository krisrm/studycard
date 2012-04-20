/* Copyright 2012 Kristofer Mitchell

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.*/


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