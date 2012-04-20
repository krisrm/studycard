package view.extension;

import static java.lang.Math.abs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JViewport;
import javax.swing.Timer;

import util.ImagePanel;
import util.ViewList;
import util.WindowResizeEvent;
import util.WindowResizeListener;
import util.WindowResizeMonitor;
import view.MainView;

public class JSwapPane extends JViewport implements ComponentListener,
	ActionListener, WindowResizeListener{
	
	private static final long serialVersionUID = 7383407175164483254L;
	private static final double ACCELERATION = 4;
	private ImagePanel swapPanel;
	private GridLayout layout;
	private int current;
	private int old;
	private Timer timer;
	private double xvel;
	private double lastX;
	private ViewList<Component> components;
	private static final int rate = 15;
	
	
	public JSwapPane(){
		this(0);
	}
	
	public JSwapPane(int defaultIndex){
		super();
		
		xvel = 0;
		timer = new Timer(rate, this);
		
		swapPanel = new ImagePanel();
		layout = new GridLayout(0, 1);
		swapPanel.setLayout(layout);
		setView(swapPanel);
		
		MainView.getFrame().addComponentListener(this);
		WindowResizeMonitor.register(MainView.getFrame(), this);
		
		components = new ViewList<Component>(swapPanel);
		current = defaultIndex;

	}
	
	/**
	 * This method adds components to the swap pane. They are added to the right
	 * of the last component.
	 */
	@Override
	public Component add(Component component) {
		components.add(component);
		
		setViewColumns();
		adjust();
		return component;
	}

	private void setViewColumns() {
		//layout.setColumns(swapPanel.getComponentCount());
		layout.setColumns(components.size());
	}
	
	public void removeView(int index) {
		swapPanel.remove(components.get(index));
		components.remove(index);
		setViewColumns();
		
	}
	
	public void removeAllViews() {
		components.clear();
		current = 0;
		old = 0;
		setViewColumns();
		adjust();
		
	}

	/**
	 * This method moves to the next component in the list of components.
	 */
	public void next() {
		setViewed(getViewed() + 1);
	}
	
	/**
	 * This method moves to the previous component in the list of components.
	 */
	public void previous() {
		setViewed(getViewed() - 1);
	}
	

	public void jumpNext() {
		old = current;
		current ++;
		current = current % components.size();
		adjust();
		
	}
	
	public void jumpPrev() {
		old = current;
		current --;
		current = (current < 0) ? components.size() : current;
		adjust();
		
	}

	/**
	 * This method sets the currently viewed component.
	 * 
	 * @param index
	 */
	public void setViewed(int index) {
		old = current;
		current = index;
		current = current % components.size();

		xvel = ACCELERATION;
		swapPanel.setFrozen(true);
		if (timer.isRunning()){
			timer.restart();
		} else{
			timer.start();
		}
	}
	
	/**
	 * This method returns the index of the currently viewed component.
	 * 
	 * @return
	 */
	public int getViewed() {
		return current;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		int x = getViewPosition().x;
		lastX = x;
		int start = old * getWidth();
		int finish = current * getWidth();
		int direction = -1;
		if (x < finish) {
			direction = 1;
		}
		if (abs(x - start) > abs(x - finish)) {
			xvel -= ACCELERATION * direction;
		} else {
			xvel += ACCELERATION * direction;
		}
		x += xvel;
		setViewPosition(new Point(x, 0));
		if ((direction == -1 && lastX - x < 0)
				|| (direction == 1 && lastX - x > 0)) {
			xvel = 0;
			timer.stop();
			adjust();
			
		}
		
	}

	private void adjust() {
//		if (!SwingUtilities.isEventDispatchThread()){
//			SwingUtilities.invokeLater(new Runnable(){
//				@Override
//				public void run() {
//					adjust();
//				}
//				
//			});
//			return;
//		}
		
		swapPanel.setFrozen(false);
		Dimension size = this.getSize();
		size.width *= (swapPanel.getComponentCount());
		swapPanel.setPreferredSize(size);
		setViewPosition(new Point(this.getSize().width * current, 0));
		swapPanel.clearImage();
		revalidate();
		this.repaint();
		
	}
	
	@Override
	public void componentHidden(ComponentEvent arg0) {}

	@Override
	public void componentMoved(ComponentEvent arg0) {
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		adjust();
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
	}

	@Override
	public void windowResized(WindowResizeEvent e) {
		adjust();
	}
	
}
