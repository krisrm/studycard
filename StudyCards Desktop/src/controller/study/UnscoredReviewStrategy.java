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
