package controller;

import util.EventType;
import view.CardListView;
import view.View;

public class CardListController extends Controller {
	
	@Override
	public boolean handleEvent(View source, EventType type, Object message) {
		//CardListView myView = (CardListView) this.view;
		
		switch(type){
		case DELETE_CARD:
			model.getStack().deleteCards((int[]) message);
			
			return true;
		
		case STACK_TITLE_CHANGE:
			String newTitle = (String) message;
			
			if (newTitle.equals(model.getStack().getTitle()))
				return true;
			
			model.getStack().setTitle(newTitle);
			model.setChangedSinceSave();
			return true;
		}
		
		return false;
	}

	@Override
	public void updated(EventType type, Object arg) {
		
		CardListView myView = (CardListView) this.view;
		
		switch (type){
		case MODEL_STACK_CHANGE:
			
			myView.setTableModel(model.getStack());
			
			break;
		case MODEL_TITLE_CHANGE:
			String modelTitle = model.getStack().getTitle();
			if (!myView.getTitleText().equals(modelTitle) && ! myView.isTitleFocusedAndBlank()){
				myView.setTitleText(modelTitle);
			}
			break;
		}
		
	}

}
