package controller;

import javax.swing.JOptionPane;

import util.EventType;
import util.TimedAction;
import view.MainView;
import view.StudyView;
import view.View;
import controller.study.AbstractReviewStrategy;
import controller.study.AbstractReviewStrategy.StudyAction;

public class StudyController extends Controller {
	public class ShowChooser implements Runnable {

		public ShowChooser(StudyController studyController) {
		}

		@Override
		public void run() {
			StudyView sView = (StudyView) view;
			sView.showModeChooser(StudyController.isChooseModeEnabled());
		}

	}

	private static boolean chooseMode = true;
	private static AbstractReviewStrategy strategy;

	public boolean handleEvent(View source, EventType type, Object message) {
		StudyView sView = (StudyView) this.view;
		switch (type) {


		case SWAP_CARD_VIEW:
			if ((!chooseMode) && (dialogSure(source))) {
				return true;
			}
			chooseMode = true;
			new TimedAction(800, new StudyController.ShowChooser(this)).start();
			if (strategy != null)
				strategy.actionPerformed(StudyAction.FINISH);
			return false;
		case SWAP_STUDY:
			chooseMode = true;
			new TimedAction(800, new StudyController.ShowChooser(this)).start();
			return false;

		case STUDY_MODE_PICKED:
			chooseMode = false;
			sView.showModeChooser(chooseMode);
			
			strategy = (AbstractReviewStrategy) message;
			strategy.actionPerformed(AbstractReviewStrategy.StudyAction.FIRST);
			sView.setView(strategy.getView());
			break;

		case STUDY_SHOW_ANSWER:
			strategy.actionPerformed(AbstractReviewStrategy.StudyAction.SHOW_ANSWER);
			sView.setView(strategy.getView());

			break;

		case STUDY_GOT_ANSWER:
			if (((Integer) message).intValue() == 1) {
				strategy.actionPerformed(AbstractReviewStrategy.StudyAction.CORRECT);
			} else {
				strategy.actionPerformed(AbstractReviewStrategy.StudyAction.INCORRECT);
			}
			sView.setView(strategy.getView());
		}

		return false;
	}

	private boolean dialogSure(View source) {
		if (source == this.view) {
			int n = JOptionPane
					.showConfirmDialog(
							MainView.getFrame(),
							"You're in the middle of studying, are you sure you wanted to do that?",
							"Woah, you sure?", 0);
			return n != 0;
		}
		return false;
	}

	public void updated(EventType type, Object arg) {
	}

	public static void setChooseModeEnabled(boolean chooseMode) {
		StudyController.chooseMode = chooseMode;
	}

	public static boolean isChooseModeEnabled() {
		return chooseMode;
	}
}