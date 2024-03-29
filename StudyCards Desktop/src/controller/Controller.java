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
