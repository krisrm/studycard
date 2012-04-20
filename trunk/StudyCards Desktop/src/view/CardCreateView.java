package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import model.Settings;
import util.EventType;
import view.extension.OutlineBorder;
import view.extension.StatusBar;
import view.extension.TransferFocus;
import view.extension.listeners.GlobalKeyListener;
import controller.Controller;

public class CardCreateView extends View {

	private JScrollPane frontScrollPane;
	private JScrollPane backScrollPane;

	public CardCreateView(Controller controller) {
		super(controller);
	}

	private static JPanel mainPane;
	private static JTextPane frontText;
	private static JTextPane backText;
	private JButton cardCreateButton;
	private JButton switchButton;
	private JButton cardListButton;

	@Override
	public void createGUI() {
		mainPane = new JPanel(new BorderLayout());
		mainPane.setBorder(new EmptyBorder(Settings.BORDER_WIDTH, Settings.BORDER_WIDTH, 
				Settings.BORDER_WIDTH, Settings.BORDER_WIDTH));
		
		JPanel editPane = new JPanel(new GridBagLayout());
		editPane.setBorder(new OutlineBorder("default"));
		
		GridBagConstraints c;
		c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5,15,2,5);
		
		editPane.add(new JLabel("Question:"),c);
		c.gridx = 1;
		editPane.add(new JLabel("Answer:"),c);
		
		
		frontText = new JTextPane();
		frontScrollPane = createTextBoxStyled(frontText);
		frontScrollPane.setPreferredSize(new Dimension(100,100));
		
		c.weightx = .5;
		c.weighty = .5;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(0,5,5,5);
		
		
		editPane.add(frontScrollPane,c);

		c.weightx = .5;
		c.weighty = .5;
		c.fill = GridBagConstraints.BOTH;
		c.gridy = 1;
		c.gridx = 1;
		c.insets = new Insets(0,5,5,5);
		backText = new JTextPane();
		
		backText.setFocusable(true);
		frontText.setFocusable(true);
		
		TransferFocus.patch(backText);
		TransferFocus.patch(frontText);
		
		backScrollPane = createTextBoxStyled(backText);
		backScrollPane.setPreferredSize(new Dimension(100,100));
		editPane.add(backScrollPane,c);
		
		mainPane.add(editPane, BorderLayout.CENTER);
		mainPane.add(makeButtonPane(),BorderLayout.PAGE_END);
		this.setParentContainer(mainPane);
		
	}

	private JButton createSwapButton() {
		switchButton = new JButton("Swap Question and Answer");
		switchButton.setIcon(Settings.ARROW_BOTH);
		
		switchButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String front = frontText.getText();
				frontText.setText(backText.getText());
				backText.setText(front);
				frontText.requestFocus();
			}
		});
		return switchButton;
	}
	
	private JScrollPane createTextBoxStyled(JTextPane textP){
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

	private JPanel makeButtonPane() {
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		
		JPanel cardButtonPane = new JPanel(new FlowLayout(FlowLayout.LEADING,5,0));
		cardListButton = new JButton(" View Cards");
		cardListButton.setIcon(Settings.ARROW_LEFT);
		cardButtonPane.add(cardListButton);
		
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 0.5;
		c.fill = GridBagConstraints.HORIZONTAL;
		buttonPanel.add(cardButtonPane,c);
		
		JPanel cardControlButtonPane = new JPanel(new FlowLayout(FlowLayout.TRAILING,5,0));
		cardCreateButton = new JButton("Add (Shift+Enter)");
		cardCreateButton.setIcon(Settings.ADD_ICON);
		
		cardControlButtonPane.add(createSwapButton());
		//cardControlButtonPane.add(new JSpacer(5, 1));
		cardControlButtonPane.add(cardCreateButton);
		
		
		c.gridx = 1;
		buttonPanel.add(cardControlButtonPane, c);
		
		cardCreateButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fireViewEvent(CardCreateView.this, EventType.CARD_ADD_TO_STACK, null);
				frontText.requestFocus();
			}
		});
		
		GlobalKeyListener.get().createGlobalButtonShortcut(cardCreateButton,KeyEvent.VK_ENTER,InputEvent.SHIFT_DOWN_MASK, mainPane);
		
		cardListButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fireViewEvent(CardCreateView.this, EventType.SWAP_CARD_VIEW, null);
				
			}
		});
		
		GlobalKeyListener.get().registerLeftButton(cardListButton, MainView.CARD_CREATE_VIEW);
		
		return buttonPanel;
	}

	@Override
	public void setStatusBarListeners() {
		StatusBar.addStatusListener(frontText, "The text you wish to appear on the front of the card - possibly a question.");
		StatusBar.addStatusListener(backText, "The text you wish to appear on the back of the card - the \"answer\" to your question.");
		StatusBar.addStatusListener(cardCreateButton, "Save this card to the Stack you're working on.");
		StatusBar.addStatusListener(switchButton, "Swap the text from the front and the back of the card.");
		StatusBar.addStatusListener(cardListButton, "View the list of cards in your Stack.");
		
		
	}

	public boolean getTextIsEmpty() {
		return frontText.getText().equals("") && backText.getText().equals("");
	}

	public String getFrontText() {
		return frontText.getText();
	}
	
	public String getBackText() {
		return backText.getText();
	}

	public void clearText() {
		frontText.setText("");
		backText.setText("");
		
	}

	public void setFrontText(String front) {
		frontText.setText(front);
		
	}
	public void setBackText(String back) {
		backText.setText(back);
		
	}

	public void setEditMode(boolean editing) {
		if (editing){
			cardCreateButton.setText("Save (Shift+Enter)");
		}else{
			cardCreateButton.setText("Add (Shift+Enter)");
		}
	}

	public void resetFocus() {
		frontText.requestFocus();
		
	}


}
