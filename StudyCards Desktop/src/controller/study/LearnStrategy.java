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

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JPanel;

import model.Card;
import model.Settings;
import view.StudyView;
import controller.StudyController;

public class LearnStrategy extends AbstractReviewStrategy {

	private ArrayList<LearnableReviewCard> reviewStack;
	private int numCorrect;
	private int numIncorrect;
	private LearnableReviewCard card;
	private int qnumber;
	
	
	
	
	
	class LearnableReviewCard{
		private int timesCorrect = 0;
		private int attempts	 = 0;
		private Card card;
		
		public LearnableReviewCard(Card c){
			card = c;
		}	
		public Card getCard(){ 
			return card;
		}
		public void addCorrect(){
			card.addCorrect();
			timesCorrect ++;
		}
		public void addAttempt(){
			card.addAttempt();
			attempts ++;
		}
		public boolean canBeRemoved(){
			return (timesCorrect >= Settings.get().correctThreshold) 
				&& (attempts > 0) 
				&& ((float)timesCorrect/attempts) > Settings.get().percentageThreshold;
		}
		
	}
	
	public LearnStrategy(StudyView sView) {
		super(sView);
		
	}

	@Override
	public void actionPerformed(StudyAction action) {
		myPanel.removeAll();
		
		
		switch (action){
		case FIRST:
			qnumber = 0;
			numCorrect = 0;
			numIncorrect = 0;
			
			reviewStack = new ArrayList<LearnableReviewCard>();
			for (Card c: model.getStack().getAllCards()){
				reviewStack.add(new LearnableReviewCard(c));
			}
			Collections.shuffle(reviewStack);
		
			card = reviewStack.get(0);
		
			makeQuestion();
		
			timeElapsed = 0;
			timer.start();
			view.setTime(0);
			model.setChangedSinceSave();
			break;
		case CORRECT:
			numCorrect++;
			card.addCorrect();
			numIncorrect--;
			
		case INCORRECT:
			numIncorrect++;
			qnumber++;
			card.addAttempt();
			
			//take the card out of the stack
			reviewStack.remove(card);
			if (!card.canBeRemoved()){
				//if it's not ready to be removed, put it back on the end
				reviewStack.add(card);
			} 
			
			if (reviewStack.isEmpty()){
				//we're done when we're out of cards in the stack
				qnumber = 0;
				timer.stop();
				myPanel.removeAll();
				view.finishedStudyingResults(view.buildReport(numCorrect,numIncorrect,timeElapsed,percentageCorrect()));
				StudyController.setChooseModeEnabled(true);
				return;
			}
			
			
			card = reviewStack.get(0);
			makeQuestion();
			view.switchButtons();
			
			break;
		case SHOW_ANSWER:
			makeAnswer();
			view.switchButtons();
			break;
		case FINISH:
			timer.stop();
		}
		
	}
	
	private void makeQuestion(){
		view.setStatText(String.format("(%.0f%% Correct)", percentageCorrect()));

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.insets = new Insets(5,5,5,5);
		myPanel.add(view.makeQuestion(card.getCard().getFront()),c);
	}
	
	private float percentageCorrect() {
		int answered = numCorrect+numIncorrect;
		return (answered == 0) ? 0 : ((float)numCorrect/answered)*100;
	}

	private void makeAnswer(){
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.weightx = 0.5;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5,5,5,5);
		
		JPanel pane = new JPanel(new BorderLayout());
	
		pane.add(view.makeQuestion(card.getCard().getFront()), BorderLayout.CENTER);
		myPanel.add(pane,c);

		c.gridx = 1;
		pane = new JPanel(new BorderLayout());
		
		pane.add(view.makeQuestion(card.getCard().getBack()), BorderLayout.CENTER);
		myPanel.add(pane,c);
	
	}


}
