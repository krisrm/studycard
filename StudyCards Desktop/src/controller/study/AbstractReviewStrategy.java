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

package controller.study;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import model.Model;
import view.StudyView;

public abstract class AbstractReviewStrategy implements ActionListener {
	
	protected StudyView view;
	protected JPanel myPanel;
	protected Model model;
	
	protected Timer timer;
	protected long timeElapsed;

	public enum StudyAction {CORRECT,INCORRECT,SHOW_ANSWER, FIRST, FINISH};
	
	public abstract void actionPerformed(StudyAction action);

	public AbstractReviewStrategy(StudyView sView) {
		view = sView;
		myPanel = new JPanel();
		model = Model.getModel();
		
		myPanel.setLayout(new GridBagLayout());
		timer = new Timer(1000,this);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (timeElapsed >= 86399){
			timer.stop();
			return;
		} else {
			timeElapsed++;
			view.setTime(timeElapsed);
		}
	}
	
	public JPanel getView() {
		
		return myPanel;
	}
}
