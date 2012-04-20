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

package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import model.Settings;

import util.BlankCaret;
import util.EventType;
import util.Utilities;
import view.extension.OutlineBorder;
import view.extension.StatusBar;
import view.extension.listeners.GlobalKeyListener;
import controller.Controller;
import controller.study.LearnStrategy;
import controller.study.QuizStrategy;
import controller.study.AbstractReviewStrategy;
import controller.study.UnscoredReviewStrategy;

public class StudyView extends View{

	private static final Font REPORT_FONT_SMALL = new Font("sansserif",Font.PLAIN,16);
	private static final Font REPORT_FONT_MEDIUM =  new Font("sansserif",Font.BOLD,18);
	private static final Font MODE_CHOOSE_FONT = new Font("system",Font.BOLD,20);
	private static final Dimension ANSWER_BUTTON_SIZE = new Dimension(84,21);
	public static final int MODE_1 = 0;
	public static final int MODE_2 = 1;
	public static final int MODE_3 = 2;
	
	private JPanel contentPane;
	private JPanel quizPane;
	private JPanel modeChoosePane;
	private JPanel finalPane;

	public StudyView(Controller controller) {
		super(controller);
	}

	private static JPanel mainPane;
	private JPanel studyControls;
	private JPanel cardPane;
	private JLabel statText;
	private JLabel timeText;
	private JButton correctButton;
	private JButton incorrectButton;
	private JButton showAnswerButton;
	private JButton cardButton;
	
	@Override
	public void createGUI() {
		mainPane = new JPanel(new BorderLayout());
		mainPane.setBorder(new EmptyBorder(Settings.BORDER_WIDTH, Settings.BORDER_WIDTH, 
				Settings.BORDER_WIDTH, Settings.BORDER_WIDTH));
		
		contentPane = new JPanel(new CardLayout());
		contentPane.setBorder(new OutlineBorder("default"));
		
		modeChoosePane = createModeChooser();
		quizPane = createQuizPane();
		finalPane = new JPanel(new BorderLayout());
		
		contentPane.add(modeChoosePane, "Mode");
		contentPane.add(finalPane,"Final");
		contentPane.add(quizPane, "Quiz");
		
		//showModeChooser(true);
		
		mainPane.add(contentPane,BorderLayout.CENTER);
		mainPane.add(makeButtonPane(),BorderLayout.PAGE_END);
		this.setParentContainer(mainPane);

	}

	private JPanel createQuizPane() {
		JPanel quiz = new JPanel(new BorderLayout());
		quiz.setBorder(new EmptyBorder(5,5,15,5));
		
		cardPane = new JPanel(new BorderLayout());
		
		quiz.add(makeStudyStats(),BorderLayout.NORTH);
		quiz.add(cardPane, BorderLayout.CENTER);
		quiz.add(makeStudyControls(), BorderLayout.SOUTH);
		
		return quiz;
	}

	private Component makeStudyStats() {
		JPanel parent = new JPanel(new GridLayout(0,2));
		
		JPanel alignPane = new JPanel(new FlowLayout(FlowLayout.LEADING,7,0));
		timeText = new JLabel("");
		timeText.setFont(Settings.CARD_CREATE_FONT);
		alignPane.add(timeText);
		parent.add(alignPane);
		
		alignPane = new JPanel(new FlowLayout(FlowLayout.TRAILING,7,0));
		statText = new JLabel("");
		statText.setFont(Settings.CARD_CREATE_FONT);
		alignPane.add(statText);
		parent.add(alignPane);
		
		return parent;
	}

	private JPanel makeStudyControls() {
		JPanel alignPane = new JPanel(new FlowLayout(FlowLayout.LEADING,5,0));
		studyControls = new JPanel(new CardLayout());
		
		JPanel questionControls = new JPanel();
		questionControls.setLayout(new BoxLayout(questionControls, BoxLayout.X_AXIS));
		
		showAnswerButton = new JButton("Show Answer");
		showAnswerButton.setPreferredSize(new Dimension(145,21));
		showAnswerButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fireViewEvent(StudyView.this, EventType.STUDY_SHOW_ANSWER, null);
				
			}
		});
		
		questionControls.add(showAnswerButton);
		questionControls.add(Box.createHorizontalGlue());
		
		JPanel answerControls = new JPanel();
		answerControls.setLayout(new BoxLayout(answerControls, BoxLayout.X_AXIS));
		
		correctButton = new JButton("Correct");
		correctButton.setPreferredSize(ANSWER_BUTTON_SIZE);
		correctButton.setBackground(Color.green);
		correctButton.setIcon(Settings.CORRECT_ICON);
		correctButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fireViewEvent(StudyView.this, EventType.STUDY_GOT_ANSWER, 1);
				
			}
		});
		incorrectButton = new JButton("Incorrect");
		incorrectButton.setBackground(Color.red);
		incorrectButton.setIcon(Settings.INCORRECT_ICON);
		incorrectButton.setPreferredSize(ANSWER_BUTTON_SIZE);
		incorrectButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fireViewEvent(StudyView.this, EventType.STUDY_GOT_ANSWER, 0);
				
			}
		});
		
		answerControls.add(Box.createHorizontalGlue());
		answerControls.add(correctButton);
		answerControls.add(Box.createRigidArea(new Dimension(5,0)));
		answerControls.add(incorrectButton);
		
		studyControls.add(questionControls, "Question");
		studyControls.add(answerControls, "Answer");
		
		alignPane.add(studyControls);
		
		return alignPane;
	}

	private JPanel createModeChooser() {
		JPanel selectionPane = new JPanel(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;
		c.weighty = 0;
		c.insets = new Insets(5, 15, 5, 5);
		JLabel studyLabel = new JLabel("Choose study mode:");
		studyLabel.setFont(new Font("sansserif",Font.BOLD,13));
		selectionPane.add(studyLabel, c);
		
		c.insets = new Insets(0, 10, 10, 10);
		c.weightx = 1;
		c.weighty = 1;

		c.gridy = 1;
		selectionPane.add(addButtonStatus("Quickly learn or re-learn the cards in your stack. Cards are presented in order, until you have learned them.", 
				createModeButton("Learn Cards",new LearnStrategy(this))), c);
		c.gridy = 2;
		selectionPane.add(addButtonStatus("Take a Quiz on your entire stack. See how well you'd do if your questions were given to you on an exam.", 
				createModeButton("Take Quiz",new QuizStrategy(this))), c);
		c.gridy = 3;
		selectionPane.add(addButtonStatus("Do a quick review. Scores aren't saved from this mode, so you can go as fast as you want.", 
				createModeButton("Unscored Review",new UnscoredReviewStrategy(this))), c);

		return selectionPane;
	}
	
	private JButton addButtonStatus(String status, JButton button){
		StatusBar.addStatusListener(button, status);
		return button;
	}

	private JButton createModeButton(String title, final AbstractReviewStrategy review) {
		JButton modeBtn = new JButton(title);
		modeBtn.setFont(MODE_CHOOSE_FONT);
		modeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fireViewEvent(StudyView.this, EventType.STUDY_MODE_PICKED,
						review);

			}
		});
		
		return modeBtn;
	}

	public void showModeChooser(boolean show) {
		CardLayout cl = (CardLayout)(contentPane.getLayout());
		//show the "Show Answer" button
		CardLayout cl2 = (CardLayout)(studyControls.getLayout());
		cl2.first(studyControls);
		if (show){
			cl.first(contentPane);
		}
		else{
			cl.last(contentPane);
		}
	}

	private JPanel makeButtonPane() {
		JPanel buttonPanel = new JPanel(new GridBagLayout());

		JPanel cardButtonPane = new JPanel(new FlowLayout(FlowLayout.TRAILING,
				5, 0));
		cardButton = new JButton("View Cards ");
		cardButton.setIcon(Settings.ARROW_RIGHT);
		cardButton.setHorizontalTextPosition(SwingConstants.LEFT);
		cardButtonPane.add(cardButton);

		GridBagConstraints c = new GridBagConstraints();

		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		buttonPanel.add(cardButtonPane, c);

		cardButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				fireViewEvent(StudyView.this, EventType.SWAP_CARD_VIEW, null);

			}
		});
		GlobalKeyListener.get().registerRightButton(cardButton, MainView.STUDY_VIEW);

		return buttonPanel;
	}
	
	public void switchButtons(){
		CardLayout cl = (CardLayout) studyControls.getLayout();
		cl.next(studyControls);	
		
	}
	
	public JComponent makeQuestion(String question){
		JTextPane textP = new JTextPane();
		
		textP.setText(question);
		textP.setEditable(false);
		textP.setCaret(new BlankCaret());
        
		textP.setBackground(new Color(200,200,200));
		
		
		MutableAttributeSet standard = new SimpleAttributeSet();
		
		StyleConstants.setAlignment(standard, StyleConstants.ALIGN_CENTER);
		StyleConstants.setFontFamily(standard, "sansserif");
		StyleConstants.setFontSize(standard, 18);
		StyleConstants.setSpaceAbove(standard, 15);
		
		textP.getStyledDocument().setParagraphAttributes(0, 0, standard, true);
		
		JScrollPane scrollPane = new JScrollPane(textP);
		scrollPane.setBorder(new OutlineBorder(2,2,2,2));
		return scrollPane;
	}


	public void setView(JPanel view) {
		cardPane.removeAll();
		cardPane.add(view,BorderLayout.CENTER);
		cardPane.repaint();
		
	}
	
	public void setStatText(String text){
		statText.setText(text);
	}
	
	public void setTime(long timeElapsed){

		timeText.setText(Utilities.formatTime(timeElapsed));
		
	}

	public void finishedStudyingResults(JPanel view){
		CardLayout cl = (CardLayout)(contentPane.getLayout());
		cl.show(contentPane, "Final");
		
		finalPane.removeAll();
		finalPane.add(view, BorderLayout.CENTER);
		finalPane.repaint();
	}
	
	
	public JPanel buildReport(int numCorrect, int numIncorrect, long timeElapsed, float percentCorrect) {
		Font REPORT_FONT = new Font("sansserif",Font.BOLD,22);

		JPanel parent = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		JLabel label = new JLabel("Study Results");
		label.setFont(REPORT_FONT);
		label.setBorder(new EmptyBorder(20,0,0,0));
		c.weightx = 1;
		c.weighty = 0.5;
		parent.add(label,c);
		JPanel reportCard = new JPanel(new GridLayout(0,2));
		reportCard.setBorder(new EmptyBorder(45,25,25,25));
		
		c.gridy = 1;
		c.weighty = 0.45;
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		parent.add(reportCard,c);
		
		reportCard.add(makeLabel("Questions Answered:",FlowLayout.TRAILING));
		reportCard.add(makeLabel(String.valueOf(numCorrect+numIncorrect),FlowLayout.LEADING));
		
		if (numIncorrect != -1) {
			reportCard.add(makeLabel("Questions Correct:", 
					FlowLayout.TRAILING));
			reportCard.add(makeLabel(String.valueOf(numCorrect),
					FlowLayout.LEADING));
		}
		
		reportCard.add(makeLabel("Time Elapsed:",FlowLayout.TRAILING));
		reportCard.add(makeLabel(Utilities.formatTime(timeElapsed),FlowLayout.LEADING));
		
		if (percentCorrect != -1){
			c.gridy = 2;
			c.fill = GridBagConstraints.NONE;
			c.weighty = .9;
			label = new JLabel(String.format("Score: %.0f%%", percentCorrect));
			label.setBorder(new EmptyBorder(0,0,20,0));
			label.setFont(REPORT_FONT_MEDIUM);
			parent.add(label,c);
		}
		
		return parent;
	}
	
	private JComponent makeLabel(String text, int align) {
		JPanel alignPanel = new JPanel(new FlowLayout(align,0,5));

		alignPanel.setBorder(new EmptyBorder(0,80,0,80));
		JLabel label = new JLabel(text);
		label.setFont(REPORT_FONT_SMALL);
		
		alignPanel.add(label);
		return alignPanel;
	}

	public JPanel buildReport(int answered, long timeElapsed) {
		return buildReport(answered+1, -1, timeElapsed, -1);
	}

	@Override
	public void setStatusBarListeners() {
		StatusBar.addStatusListener(cardButton,"View the list of cards in your Stack.");
		StatusBar.addStatusListener(correctButton, "Click if you answered the question correctly.");
		StatusBar.addStatusListener(incorrectButton, "Click if you did not answer the question correctly.");
		StatusBar.addStatusListener(showAnswerButton, "Click to show the answer to the question.");
		
	}

	
	
}
