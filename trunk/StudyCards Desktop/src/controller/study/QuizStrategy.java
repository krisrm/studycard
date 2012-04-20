package controller.study;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPanel;

import model.Card;
import model.Stack;
import view.StudyView;
import controller.StudyController;

public class QuizStrategy extends AbstractReviewStrategy {

	private Stack stack;
	private int qnumber;

	private int numCorrect;
	private int numIncorrect;
	private Card card;

	public QuizStrategy(StudyView sView) {
		super(sView);

	}

	@Override
	public void actionPerformed(StudyAction action) {
		myPanel.removeAll();
		stack = model.getStack();
		card = stack.getCard(qnumber);

		switch (action) {
		case FIRST:
			qnumber = 0;
			numCorrect = 0;
			numIncorrect = 0;
			makeQuestion();
			timeElapsed = 0;
			timer.start();
			view.setTime(0);
			model.setChangedSinceSave();
			break;
		case CORRECT:
			numCorrect++;
			card.addCorrect();
			numIncorrect--;// obfuscated... but it's to avoid nasty control
							// schemes
		case INCORRECT:
			numIncorrect++;
			qnumber++;
			card.addAttempt();

			if (qnumber >= stack.getNumOfCards()) {
				// we're done
				qnumber = 0;
				timer.stop();
				myPanel.removeAll();
				view.finishedStudyingResults(view.buildReport(numCorrect,
						numIncorrect, timeElapsed, percentageCorrect()));
				StudyController.setChooseModeEnabled(true);
				return;
			}

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

	private void makeQuestion() {
		view.setStatText(String.format("Question %d of %d (%.0f%% Correct)",
				qnumber + 1, stack.getNumOfCards(), percentageCorrect()));

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.insets = new Insets(5, 5, 5, 5);
		myPanel.add(view.makeQuestion(stack.getCard(qnumber).getFront()), c);
	}

	private float percentageCorrect() {
		int answered = numCorrect + numIncorrect;
		return (answered == 0) ? 0 : ((float) numCorrect / answered) * 100;
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
