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
