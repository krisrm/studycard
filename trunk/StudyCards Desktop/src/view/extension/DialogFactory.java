package view.extension;

import javax.swing.JOptionPane;

import model.Settings;
import view.MainView;


public class DialogFactory {

	public static void showUpdateDialog(boolean isUpdated) {
		if (isUpdated){
			JOptionPane.showMessageDialog(MainView.getFrame(), 
					"You are using the latest version ("+ Settings.VERSION+") of StudyCards", 
					"StudyCards Updater",
					JOptionPane.INFORMATION_MESSAGE);
		}else{
			
			Object[] options = {"Go Download It",
                    "I'll Do It Later"};
			int choice = JOptionPane.showOptionDialog(MainView.getFrame(),
					"StudyCards is out of date! You should go to the website and download the latest version.",
					"Uhoh...", 
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE, 
					null, 
					options, 
					options[1]);
			
			if (choice == JOptionPane.YES_OPTION){
				//BareBonesBrowserLaunch.openURL(Settings.UPDATESITE);
			}
		}
	
	}

	public static void errorDialog(String msg) {
		JOptionPane.showMessageDialog(MainView.getFrame(), msg, "Error",
				JOptionPane.ERROR_MESSAGE);
		
	}

}
