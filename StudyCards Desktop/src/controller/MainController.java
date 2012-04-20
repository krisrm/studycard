package controller;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import model.Model;
import model.Settings;
import model.Stack;
import util.EventType;
import util.StudyCardFileFilter;
import util.Utilities;
import view.MainView;
import view.View;

/*
 * DONE - study strategies and views
 * TODO - "about", and help pages
 * TODO - major code cleanup (Settings?)
 * TODO - javadoc
 * DONE - status bar linkage
 * 
 * -timed study sessions/clock
 * -smart review algorithm that keys on weaknesses 
 * -session graphs (achievement tracking?)
 * -swappy panes for study cards
 * -tie images/sounds to cards
 */



public class MainController extends Controller {

	@Override
	public boolean handleEvent(View source, EventType type, Object message) {
		
		MainView view = (MainView) this.view;
		
		switch (type){
		case SWAP_CARD_VIEW:
			view.getParentContainer().requestFocusInWindow();
			view.swapTo(MainView.CARD_VIEW);
			MainView.fireStaticViewEvent(EventType.CLEAR_EDIT, null);
			break;
		case SWAP_CARD_CREATE_VIEW:
			view.getParentContainer().requestFocusInWindow();
			view.swapTo(MainView.CARD_CREATE_VIEW);
			break;
			
		case SWAP_STUDY:
			view.getParentContainer().requestFocusInWindow();
			if (model.getStack().getNumOfCards() == 0){
				JOptionPane.showMessageDialog(MainView.getFrame(),
					    "Hold on, you don't have any cards to study!",
					    "Ummmm...",
					    JOptionPane.WARNING_MESSAGE);
				return false;
			}
			
			view.swapTo(MainView.STUDY_VIEW);
			MainView.fireStaticViewEvent(EventType.CLEAR_EDIT, null);
			break;
		
		case FILE_NEW:
			dialogNewFile();
			break;
		case FILE_OPEN:
			dialogOpenFile(message);
			break;
		case FILE_SAVE:
			File current = model.getCurrentFile();
			if (current != null){
				model.saveCards(current);
				return false;
			} 
		case FILE_SAVE_AS:
			File saveFile = makeSaveFileChooser();
			model.saveCards(saveFile);
			break;
		case FILE_EXIT:
			dialogExit();
			break;
			
		/* Removed online update checking! */
//		case CHECK_UPDATES:
//			
//			Thread t = new Thread(){
//			
//				@Override
//				public void run() {
//					String webResponse;
//		
//					try {
//						webResponse= HTTPUtil.executePost(Settings.UPDATESVC, "version="+Settings.VERSION_NUM);
//					} catch (IOException e) {
//						webResponse = "-1";
//						e.printStackTrace();
//					}
//					webResponse = webResponse.replaceAll("\\s", "");
//					final Integer updSiteVersion = Integer.parseInt(webResponse);
//					
//					try {
//						SwingUtilities.invokeAndWait(new Runnable(){
//
//							@Override
//							public void run() {
//								if (updSiteVersion > Settings.VERSION_NUM){
//									//we need to update
//									DialogFactory.showUpdateDialog(false);
//								} else if (updSiteVersion == -1){
//									//we had troubles connecting
//									DialogFactory.errorDialog("Uhh, I had some troubles connecting. You should make sure your internet is working and try again.");
//								} else {
//									//we're up to date
//									DialogFactory.showUpdateDialog(true);
//								}
//								
//							}
//							
//						});
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					} catch (InvocationTargetException e) {
//						e.printStackTrace();
//					}
//				}
//				
//			};
//			t.start();
//			
//			break;
		}
		
		
		return false;
	}

	private void dialogOpenFile(Object message) {
		int n = JOptionPane.YES_OPTION;
		if (model.hasChangedSinceSave()){
			Object[] options = {"Yeah, load that file!", "Yeah, but Save first",
					"No! Take me back"};
			n = JOptionPane.showOptionDialog(MainView.getFrame(),
					"Ok, listen, you haven't saved your stuff for awhile. Are you sure you want to do that?",
					"Uhhhh",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE,
					null,
					options,
					options[2]);
			if (n == JOptionPane.CANCEL_OPTION)
				return;
			
			if (n == JOptionPane.NO_OPTION){
				if (!saveFile())
					 return;
				n = JOptionPane.YES_OPTION;
			}
		}
		
		if (n == JOptionPane.YES_OPTION) {
			File openFile = (message instanceof File) ? (File) message
					: makeOpenFileChooser();
			try {
				model.loadCards(openFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void dialogNewFile() {
		int n = JOptionPane.YES_OPTION;
		if (model.hasChangedSinceSave()){
			Object[] options = {"Yeah, make a new Stack", "Yeah, but Save first",
					"No! Take me back"};
			n = JOptionPane.showOptionDialog(MainView.getFrame(),
					"Ok, listen, you haven't saved your stuff for awhile. Are you sure you want to do that?",
					"On a scale from one to sure...",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE,
					null,
					options,
					options[2]);
			if (n == JOptionPane.CANCEL_OPTION)
				return;
			
			if (n == JOptionPane.NO_OPTION){
				if (!saveFile())
					 return;
				n = JOptionPane.YES_OPTION;
			}
		}
		
		if (n == JOptionPane.YES_OPTION)
			model.makeNewStack();
	}

	private boolean saveFile() {
		File current = model.getCurrentFile();
		
		if (current == null){
			current = makeSaveFileChooser();
		}
		
		if (current == null)
			return false;
		
		model.saveCards(current);
		return true;
	}
	

	private void dialogExit() {
		Settings.saveSettings();
		int n = JOptionPane.YES_OPTION;
		if (model.hasChangedSinceSave()){
			Object[] options = {"Exit!!",
					"Save and Exit",
			"Cancel"};
			n = JOptionPane.showOptionDialog(MainView.getFrame(),
					"Hey, you know \""+ model.getStack().getTitle() +"\"? Well, it's got some unsaved changes. What are you going do about it?",
					"Proceed with great caution!!",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE,
					null,
					options,
					options[2]);
			if (n == JOptionPane.NO_OPTION){
				 if (!saveFile())
					 return;
				System.exit(0);
			}
		}
		if (n == JOptionPane.YES_OPTION)
			System.exit(0);
	}

	public static File makeOpenFileChooser() {
		JFileChooser fc = new JFileChooser();
		File prev = Settings.get().lastFile;
		if (prev != null)
			fc.setSelectedFile(prev);
		fc.setFileFilter(new StudyCardFileFilter());
		int returnVal = fc.showOpenDialog(MainView.getFrame());
		
        if (returnVal == JFileChooser.APPROVE_OPTION) {
        	Settings.get().lastFile = fc.getSelectedFile();
            return fc.getSelectedFile();
            
        }
        
		return null;
	}
	
	public static File makeSaveFileChooser() {
		JFileChooser fc = new JFileChooser();
		
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(new StudyCardFileFilter());
		fc.setSelectedFile(new File(Model.getModel().getStack().getTitle() + "." + Settings.FILE_EXTENSION));
		int returnVal = fc.showSaveDialog(MainView.getFrame());
		
        if (returnVal == JFileChooser.APPROVE_OPTION) {
        	File saveFile = Utilities.addExtension(fc.getSelectedFile());
        	
            return saveFile;
            
        }
        
		return null;
	}
	
	@Override
	public void updated(EventType type, Object arg) {
		
		switch (type){
		case MODEL_TITLE_CHANGE:
			Stack cardstack = model.getStack();
			
			String newTitle = cardstack.getTitle();
			String modelFileTitle = model.getFileTitle();
			String changed = (model.hasChangedSinceSave()) ? "*" : "";
			
			//if the file title and the stack name are the same, don't bother with the title being different
			if (modelFileTitle.equals("") 
					|| Utilities.getWithoutExtension(model.getCurrentFile()).equals(newTitle)){
				newTitle+= changed;
				MainView.setFrameTitle(newTitle);
				return;
			}

			
			MainView.setFrameTitle(newTitle + " ("+modelFileTitle+ ")" + changed);
			((MainView) view).rebuildFileMenu();
			break;
		case UNTYPED:
			
			break;
		}

	}

}
