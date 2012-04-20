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

package view.extension.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import util.EventType;
import view.MainView;

public class EditMenuListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem source = (JMenuItem) e.getSource();
		
		if (source.getText().contains("Cut")){
			
		} else if (source.getText().contains("Paste")){
			
		} else if (source.getText().contains("Study Cards")){
			MainView.fireStaticViewEvent(EventType.SWAP_STUDY, null);
		} else if (source.getText().contains("View Stack")){
			MainView.fireStaticViewEvent(EventType.SWAP_CARD_VIEW, null);
		} else if (source.getText().contains("Create Cards")){
			MainView.fireStaticViewEvent(EventType.SWAP_CARD_CREATE_VIEW, null);
		}
	}

}
