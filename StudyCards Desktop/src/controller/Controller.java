package controller;


import java.util.Observable;
import java.util.Observer;

import util.EventType;
import util.ModelMessage;
import view.View;

import model.Model;


public abstract class Controller implements Observer{
	protected static Model model;
	protected View view;
	
	public Controller(){
		model = Model.getModel();
		model.addController(this);
		
	}
	
	@Override
	public void update(Observable observable, Object message) {
		
		ModelMessage mm = (ModelMessage) message;
		if (mm == null){
			updated(EventType.UNTYPED, null);
			return;
		}
		
		updated(mm.getType(),mm.getArgument());
	}	

	public void setView(View v){
		view = v;
	}
	
	public abstract void updated(EventType type, Object arg);
	public abstract boolean handleEvent(View source, EventType type, Object message);
	
}
