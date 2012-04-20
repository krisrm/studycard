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

package controller;

import model.Card;
import model.Settings;
import util.EventType;
import util.TimedAction;
import view.CardCreateView;
import view.MainView;
import view.View;

public class CardCreateController extends Controller {
	private boolean editMode = false;
	private Integer editingIndex;
	
	@Override
	public boolean handleEvent(View source, EventType type, Object message) {
		CardCreateView myView = (CardCreateView) this.view;
		switch (type){
		case SWAP_CARD_CREATE_VIEW:
			new TimedAction(Settings.SWAP_TIME,new Runnable() {
				
				@Override
				public void run() {
					((CardCreateView) CardCreateController.this.view).resetFocus();
				}
			}).start();
			
			break;
		case CLEAR_EDIT:
			myView.clearText();
			break;
			
		case EDIT_CARD:
			MainView.fireStaticViewEvent(EventType.SWAP_CARD_CREATE_VIEW, null);
			editingIndex = (Integer) message;
			Card current = model.getStack().getCard(editingIndex);
			if (current == null)
				return true;
			
			myView.setFrontText(current.getFront());
			myView.setBackText(current.getBack());
			myView.setEditMode(true);
			editMode = true;
			
			return true;
			
		case CARD_ADD_TO_STACK:
			if (MainView.getMode() != MainView.CARD_CREATE_VIEW 
					|| myView.getTextIsEmpty())
				return false;
			
			if (editMode){
				Card edited = new Card(myView.getFrontText(),myView.getBackText());
				model.getStack().editCard(edited, editingIndex);
				editMode = false;
				myView.clearText();
				myView.setEditMode(false);
				MainView.fireStaticViewEvent(EventType.SWAP_CARD_VIEW, null);
			} else{
				model.createNewCard(myView.getFrontText(),myView.getBackText());
				myView.clearText();
			}
			
			break;
		
		}
		return false;
	}

	@Override
	public void updated(EventType type, Object arg) {
//		CardCreateView myView = (CardCreateView) this.view;
		switch (type){

		
		}
		
	}

}
