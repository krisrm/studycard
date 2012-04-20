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

package view.extension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import model.Model;
import model.Settings;
import util.BareBonesBrowserLaunch;
import util.EventType;
import util.FileMenuItem;
import view.MainView;
import view.extension.listeners.EditMenuListener;
import view.extension.listeners.FileMenuListener;

public class MenuBar extends JMenuBar implements ActionListener{
	
	private static final long serialVersionUID = -400043268740807671L;

	private static JMenu fileMenu;
	private static JMenu helpMenu;
	private static JMenu viewMenu;

	private JRadioButtonMenuItem stackRadio;

	private JRadioButtonMenuItem studyRadio;

	private JRadioButtonMenuItem createRadio;

	private AboutDialog dialog;

	private JMenuItem aboutMenuItem;

	private JMenuItem updateMenuItem;

	private JMenuItem helpMenuItem;

	public MenuBar(){
		super();
		fileMenu = new JMenu("File");
		viewMenu = new JMenu("View");
		helpMenu = new JMenu("Help");
		
		createFileMenu();
		createViewMenu();
		createHelpMenu();
		
		add(fileMenu);
		add(viewMenu);
		add(helpMenu);
	}
	
	
	private JMenuItem makeMenuItem(String title, ActionListener actionListener) {
		
		return makeMenuItem(title, actionListener, -1);
	}
	private JMenuItem makeMenuItem(String title,
			ActionListener actionListener, int accelerator_key){
		return makeMenuItem(title, actionListener, accelerator_key, InputEvent.CTRL_DOWN_MASK);
	}

	private JMenuItem makeMenuItem(String title,
			ActionListener actionListener, int accelerator_key, int alternate_key) {
		
		JMenuItem tempItem = new JMenuItem(title);
		tempItem.addActionListener(actionListener);
		
		if (accelerator_key != -1)
			tempItem.setAccelerator(KeyStroke.getKeyStroke(accelerator_key, 
					alternate_key));
		
		return tempItem;
	}
	
	public void rebuildFileMenu(){
		fileMenu.removeAll();
		createFileMenu();
	}

	private void createFileMenu() {
		fileMenu.add(addStatusListener(makeMenuItem("New", 
				new FileMenuListener(), KeyEvent.VK_N),
				"Create a new Stack of cards to study."));
		fileMenu.add(addStatusListener(makeMenuItem("Open", 
				new FileMenuListener(), KeyEvent.VK_O),
				"Open a previously saved Stack of cards."));
		fileMenu.addSeparator();
		
		fileMenu.add(addStatusListener(makeMenuItem("Save", 
				new FileMenuListener(), KeyEvent.VK_S),
				"Save the Stack of cards that you're currently working on."));
		fileMenu.add(addStatusListener(makeMenuItem("Save As", 
				new FileMenuListener()),
				"Save the Stack of cards that you're currently working on as a new file."));
		fileMenu.addSeparator();
		
		fileMenu.add(addStatusListener(makeMenuItem("Exit", 
				new FileMenuListener(), KeyEvent.VK_Q),
				"Please don't leave me :("));
		
		addRecentlyOpened();
		
	}

	private void addRecentlyOpened() {
		List<FileMenuItem> r = Model.getModel().getRecentlyOpened();
		
		if (r == null || r.size() == 0)
			return;
		
		fileMenu.addSeparator();
		int n = KeyEvent.VK_1;
		for (FileMenuItem item : r) {
			fileMenu.add(makeMenuItem(item.getShortFileName(),
					item.getActionListener(), n, InputEvent.ALT_DOWN_MASK));
			n++;
		}
		
	}


	private void createHelpMenu() {
		aboutMenuItem = addStatusListener(makeMenuItem("About StudyCards",this),
				"Find out about this version of StudyCards.");
		updateMenuItem = addStatusListener(makeMenuItem("Check for Updates", this), 
				"Make sure you're running the latest version of StudyCards.");
		helpMenuItem = addStatusListener(makeMenuItem("Help Contents...",this),
				"Wait, you need help? You should click here! (you may or may not need internet access... just kidding, you will)");

		helpMenu.add(aboutMenuItem);
		helpMenu.add(updateMenuItem);
		helpMenu.addSeparator();
		helpMenu.add(helpMenuItem);
	}

	private void createViewMenu() {
		//ccp maybe not necessary...
		/*editMenu.add(makeMenuItem("Cut",
				new EditMenuListener(), KeyEvent.VK_X));
		editMenu.add(makeMenuItem("Copy",
				new EditMenuListener(), KeyEvent.VK_C));
		editMenu.add(makeMenuItem("Paste",
				new EditMenuListener(), KeyEvent.VK_V));
		
		editMenu.addSeparator();*/
		
		ButtonGroup viewRadioGroup = new ButtonGroup();
		
		studyRadio = new JRadioButtonMenuItem("Study Cards");
		studyRadio.addActionListener(new EditMenuListener());
		viewRadioGroup.add(studyRadio);
		viewMenu.add(studyRadio);
		StatusBar.addStatusListener(studyRadio, 
				"Study the cards in your Stack.");
		
		stackRadio = new JRadioButtonMenuItem("View Stack");
		stackRadio.addActionListener(new EditMenuListener());
		viewRadioGroup.add(stackRadio);
		viewMenu.add(stackRadio);
		StatusBar.addStatusListener(stackRadio, 
				"View the list of cards in your Stack.");
		
		createRadio = new JRadioButtonMenuItem("Create Cards");
		createRadio.addActionListener(new EditMenuListener());
		viewRadioGroup.add(createRadio);
		viewMenu.add(createRadio);
		StatusBar.addStatusListener(createRadio, 
				"Create cards to add to your Stack.");
	}
	
	public void setSelected(int index){
		
		switch (index){
		case MainView.STUDY_VIEW:
			studyRadio.setSelected(true);
			break;
		case MainView.CARD_VIEW:
			stackRadio.setSelected(true);
			break;
		case MainView.CARD_CREATE_VIEW:
			createRadio.setSelected(true);
			break;
		}
		
	}

	private JMenuItem addStatusListener(JMenuItem c, String status){
		StatusBar.addStatusListener(c, status);
		return c;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem source = (JMenuItem) e.getSource();
		
		if (source.equals(helpMenuItem)){
			BareBonesBrowserLaunch.openURL(Settings.HELP_FILE_URL);
		} else if (source.equals(aboutMenuItem)){
			if (dialog == null)
				dialog = new AboutDialog(MainView.getFrame().getContentPane());
			dialog.setVisible(true);
		} else if (source.equals(updateMenuItem)){
			MainView.fireStaticViewEvent(EventType.CHECK_UPDATES,null);
		}

	}

	
}
