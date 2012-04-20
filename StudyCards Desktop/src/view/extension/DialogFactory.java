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
