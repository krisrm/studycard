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

package view;

import java.awt.Container;
import java.util.ArrayList;
import java.util.Iterator;

import util.EventType;
import controller.Controller;

public abstract class View {
	private Controller controller;
	private Container parentContainer;
	private static ArrayList<View> allViews = new ArrayList<View>();

	public View(Controller controller) {
		allViews.add(this);
		this.controller = controller;
		createGUI();
		setStatusBarListeners();
		controller.setView(this);
	}

	public void setParentContainer(Container c) {
		this.parentContainer = c;
	}

	public Container getParentContainer() {
		return parentContainer;
	}

	public boolean fireViewEvent(View source, EventType type, Object message) {
		if (!controller.handleEvent(source, type, message)) {
			
			// if our controller can't handle it, ask around
			boolean handled = false;
			Iterator<View> i = allViews.iterator();

			while ((!handled) && i.hasNext()) {
				View v = i.next();

				if (!v.equals(this))
					handled = v.fireShallowViewEvent(source, type, message);
				
			}

			if (handled)
				return true;

		} else {
			return true;
		}
		return false;
	}

	private boolean fireShallowViewEvent(View source, EventType type,
			Object message) {

		return controller.handleEvent(source, type, message);
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public Controller getController() {
		return controller;
	}

	public abstract void createGUI();
	public abstract void setStatusBarListeners();
}
