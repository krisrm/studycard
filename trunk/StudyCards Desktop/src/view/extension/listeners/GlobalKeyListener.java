package view.extension.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import view.MainView;

public class GlobalKeyListener {
	
	
	private final class TimedAction extends AbstractAction {

		private static final long serialVersionUID = 5257078869188866431L;
		private Timer timer;
		private Runnable start;
		private Runnable end;
		
		private TimedAction(Runnable start, Runnable end) {
			this.start = start;
			this.end = end;
		}

		public void actionPerformed(ActionEvent event) {
			if (timer != null && timer.isRunning())
				return;
			
			start.run();
			
			timer = new Timer(100, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					end.run();
					timer.stop();
					
				}
				
			});
			timer.setRepeats(false);
			timer.start();
		}
	}

	private static int id = 0;
	public static int RIGHT = 1;
	public static int LEFT = 0;
	
	private static GlobalKeyListener instance;
	private boolean leftRegistered = false;
	private boolean rightRegistered = false;
	
	private HashMap<Integer, JButton[]> buttonModeMap;
	
	private GlobalKeyListener(){
		buttonModeMap = new HashMap<Integer,JButton[]>();
	}
	
	public static GlobalKeyListener get(){
		if (instance == null)
			instance = new GlobalKeyListener();
		return instance;
	}
	
	
	public void registerButton(JButton button, int key, final int arraynum, final int in_mode){
		final String ACTION = newAction();
			button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
					KeyStroke.getKeyStroke(key, 0), ACTION);
			Runnable start = new Runnable() {
				
				@Override
				public void run() {
					JButton[] toClick = buttonModeMap.get(MainView.getMode());
					if (toClick == null || toClick[arraynum] == null)
						return;
					toClick[arraynum].setSelected(true);
					toClick[arraynum].doClick();
				}
			};

			Runnable end = new Runnable() {
				@Override
				public void run() {
					for (JButton[] buttonArr : buttonModeMap.values()){
						if (buttonArr == null)
							continue;
						
						if (buttonArr[arraynum] != null)
							buttonArr[arraynum].setSelected(false);	
					}
					
				}
			};
			
			button.getActionMap().put(ACTION, new TimedAction(start,end));
	}
	
	
	public void registerRightButton(final JButton button, final int in_mode){
		
		if (buttonModeMap.containsKey(in_mode)){
			buttonModeMap.put(in_mode, new JButton[]{buttonModeMap.get(in_mode)[LEFT], button});
		} else {
			buttonModeMap.put(in_mode, new JButton[] {null,button});
		}
		
		if (! rightRegistered ){
			registerButton(button,KeyEvent.VK_RIGHT,RIGHT,in_mode);
			rightRegistered = true;
		}
	}
	public void registerLeftButton(final JButton button, final int in_mode){
		
		if (buttonModeMap.containsKey(in_mode)){
			buttonModeMap.put(in_mode, new JButton[]{button, buttonModeMap.get(in_mode)[RIGHT]});
		} else {
			buttonModeMap.put(in_mode, new JButton[] {button,null});
		}
	
		if (! leftRegistered ){
			registerButton(button,KeyEvent.VK_LEFT,LEFT,in_mode);
			leftRegistered = true;
		}
		
	}
	
	public void createGlobalButtonShortcut(final JButton button,
			int key, int mask, JPanel listenPane) {
		
		final String ACTION = newAction();
		
		listenPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(key, mask), ACTION);
		
		Runnable start = new Runnable() {
			
			@Override
			public void run() {
				button.setSelected(true);
				button.doClick();
			}
		};

		Runnable end = new Runnable() {
			@Override
			public void run() {
				button.setSelected(false);	
			}
		};
		
		listenPane.getActionMap().put(ACTION, new TimedAction(start, end));
				
	}

	private String newAction() {
		final String ACTION = "Action_" + id;
		id++;
		return ACTION;
	}
}
