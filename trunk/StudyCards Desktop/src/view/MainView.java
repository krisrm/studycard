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

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import model.Settings;

import util.EventType;
import view.extension.JSwapPane;
import view.extension.MenuBar;
import view.extension.StatusBar;
import controller.CardCreateController;
import controller.CardListController;
import controller.Controller;
import controller.StudyController;


public class MainView extends View {

	public MainView(Controller controller) {
		super(controller);
	}

	private static JFrame frame;
	private static StatusBar statusBar;
	private static MenuBar menuBar;
	private JPanel rootPane;
	private static JSwapPane swapPane;
	
	private static MainView mainView;
	
	public static final int STUDY_VIEW = 0;
	public static final int CARD_VIEW = 1;
	public static final int CARD_CREATE_VIEW = 2;
	
	@Override
	public void createGUI() {
		mainView = this;
		
		rootPane = new JPanel(new BorderLayout(0,0));
		
		setupFrame();
		
		statusBar = new StatusBar();
		
		swapPane = new JSwapPane(CARD_VIEW);
		menuBar.setSelected(CARD_VIEW);
		swapPane.add(new StudyView(new StudyController()).getParentContainer());
		swapPane.add(new CardListView(new CardListController()).getParentContainer());
		swapPane.add(new CardCreateView(new CardCreateController()).getParentContainer());
		
		
		rootPane.add(swapPane,BorderLayout.CENTER);
		rootPane.add(statusBar, BorderLayout.PAGE_END);
		
	}

	private void setupFrame() {
		frame = new JFrame(Settings.TITLE);
		frame.setSize(Settings.get().windowSize);
		frame.setMinimumSize(Settings.DEFAULT_WINDOW_SIZE);
		
		if (Settings.get().windowPosition == null)
			frame.setLocationRelativeTo(null);
		else
			frame.setLocation(Settings.get().windowPosition);
		
		frame.setIconImage(Settings.ICON);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				fireViewEvent(MainView.this, EventType.FILE_EXIT, null);
			}
			
		});
		//frame.setUndecorated(true); //leave this for now... UI upgrade for the future?
		menuBar = new MenuBar();
		frame.setJMenuBar(menuBar);
		frame.setContentPane(rootPane);
		setParentContainer(frame);
	}

	
	public static JFrame getFrame() {
		return frame;
	}


	public void swapTo(int index) {
		swapPane.setViewed(index);
		menuBar.setSelected(index);
		
	}
	
	public static void fireStaticViewEvent(EventType type, Object message){
		mainView.fireViewEvent(mainView, type, message);
	}


	public static void setFrameTitle(String title) {
		frame.setTitle(Settings.TITLE + " - " + title);
		
	}

	public static int getMode() {
		
		return swapPane.getViewed();
	}

	public void rebuildFileMenu() {
		menuBar.rebuildFileMenu();
		
	}

	@Override
	public void setStatusBarListeners() {
		// TODO Auto-generated method stub
		
	}
	
}
