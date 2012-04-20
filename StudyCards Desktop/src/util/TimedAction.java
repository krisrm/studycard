package util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class TimedAction implements ActionListener{
	public TimedAction(int delay, Runnable action) {
		this.delay = delay;
		this.action = action;
	}
	
	private Timer timer;
	private int delay;
	private Runnable action;
	
	public void start() {
		timer = new Timer(delay, this);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		action.run();
		timer.stop();
	}
	
	
	
}
