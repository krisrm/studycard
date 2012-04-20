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

import javax.swing.JPanel;

import model.Stack;
import view.StudyView;
import controller.StudyController;

public class UnscoredReviewStrategy extends AbstractReviewStrategy {

	private Stack stack;
	private int qnumber;

	public UnscoredReviewStrategy(StudyView sView) {
		super(sView);
	}

	@Override
	public void actionPerformed(StudyAction action) {
		myPanel.removeAll();
		stack = model.getStack();

		switch (action) {
		case FIRST:
			qnumber = 0;
			timeElapsed = 0;
			timer.start();
			makeQuestion();
			view.setTime(0);
			break;

		case SHOW_ANSWER:
			makeAnswer();
			view.switchButtons();
			break;

		case CORRECT:
		case INCORRECT:
			qnumber++;

			if (qnumber >= stack.getNumOfCards()) {
				// we're done
				timer.stop();
				myPanel.removeAll();
				view.finishedStudyingResults(view.buildReport(qnumber,
						timeElapsed));
				qnumber = 0;
				StudyController.setChooseModeEnabled(true);
				return;
			}

			makeQuestion();
			view.switchButtons();

			break;
		case FINISH:
			timer.stop();
		}

	}

	private void makeQuestion() {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.insets = new Insets(5, 5, 5, 5);
		myPanel.add(view.makeQuestion(stack.getCard(qnumber).getFront()), c);
	}

	private void makeAnswer() {

		GridBagConstraints c = new GridBagConstraints();

		c.weightx = 0.5;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 5, 5, 5);

		JPanel pane = new JPanel(new BorderLayout());

		pane.add(view.makeQuestion(stack.getCard(qnumber).getFront()),
				BorderLayout.CENTER);
		myPanel.add(pane, c);

		c.gridx = 1;
		pane = new JPanel(new BorderLayout());

		pane.add(view.makeQuestion(stack.getCard(qnumber).getBack()),
				BorderLayout.CENTER);
		myPanel.add(pane, c);

	}

}
