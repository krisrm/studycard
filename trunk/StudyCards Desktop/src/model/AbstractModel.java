package model;
import java.util.Observable;

import javax.swing.SwingUtilities;

import util.ModelMessage;

import controller.Controller;

public class AbstractModel extends Observable {

	public AbstractModel() {
		super();
	}

	public void addController(Controller controller) {
		this.addObserver(controller);
		
	}

	public void notifyControllers() {
		notifyControllers(null);
	}

	public void notifyControllers(final ModelMessage arg) {

		if (!SwingUtilities.isEventDispatchThread()) {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					notifyControllers(arg);
				}
			};
			SwingUtilities.invokeLater(r);
			return;
		}
		setChanged();
		this.notifyObservers(arg);
	}

}