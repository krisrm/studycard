package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;

import model.CardTableModel;
import model.Settings;
import model.Stack;

import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;

import util.EventType;
import view.extension.OutlineBorder;
import view.extension.StatusBar;
import view.extension.listeners.GlobalKeyListener;
import controller.Controller;

public class CardListView extends View {

	
	private static final Font FIELD_FONT = new Font("sansserif",Font.PLAIN,13);
	private static final Font LABEL_FONT = new Font("sansserif",Font.BOLD,13);
	private static final int SCORE_WIDTH = 50;

	public CardListView(Controller controller) {
		super(controller);
	}
	
	private static JPanel mainPane;
	private static JPanel panel;
	private static JTable table;
	//private CardTableModel tableModel;
	private JScrollPane scrollPane;
	private JTextField titleField;
	private CardTableModel tableModel;
	private JButton editButton;
	private JButton deleteButton;
	private JButton createButton;
	private JButton studyButton;

	@Override
	public void createGUI() {
		
		mainPane = new JPanel(new BorderLayout(0,0));
		mainPane.setBorder(new EmptyBorder(Settings.BORDER_WIDTH, Settings.BORDER_WIDTH, 
				Settings.BORDER_WIDTH, Settings.BORDER_WIDTH));
		
		panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder());
		panel.add(createTitlePane(), BorderLayout.PAGE_START);
		
		table = new JTable();
		ToolTipManager.sharedInstance().unregisterComponent(table);
		ToolTipManager.sharedInstance().unregisterComponent(table.getTableHeader());
		
		//StatusBar.addStatusListener(table, "View all cards");
		table.getTableHeader().setReorderingAllowed(false);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2){
					fireViewEvent(CardListView.this, EventType.EDIT_CARD, table.getSelectedRow());
				}
				
			}
		});
		
		tableModel = new CardTableModel();
		table.setModel(tableModel);
		
		TableColumn scoreCol = table.getColumnModel().getColumn(CardTableModel.SCORE_COL);
		scoreCol.setPreferredWidth(SCORE_WIDTH);
		scoreCol.setMaxWidth(SCORE_WIDTH);
		scoreCol.setMinWidth(SCORE_WIDTH);
		
		SubstanceDefaultTableCellRenderer stcr = new SubstanceDefaultTableCellRenderer(){
			private static final long serialVersionUID = 8432701659243824115L;
			@Override
			protected void setValue(Object value){
				if (value instanceof String){
					String str = (String) value;
					if (CardTableModel.belowThreshold(str)){
						this.setForeground(Color.red);
					}
				}
				super.setValue(value);
				
			}
			
		};
		stcr.setHorizontalAlignment(SwingConstants.RIGHT);
		scoreCol.setCellRenderer(stcr);
		
		scrollPane = new JScrollPane(table);
		scrollPane.setBorder(new OutlineBorder(Settings.BORDER_WIDTH,Settings.BORDER_WIDTH,Settings.BORDER_WIDTH*2,Settings.BORDER_WIDTH));
		panel.add(scrollPane, BorderLayout.CENTER);
		
		mainPane.add(panel, BorderLayout.CENTER);
		mainPane.add(makeButtonPane(),BorderLayout.PAGE_END);
		setParentContainer(mainPane);
	}
	
	
	private JPanel createTitlePane() {
		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEADING,15,0));

		JLabel titleLabel = new JLabel("Title:");
		titleLabel.setFont(LABEL_FONT);
		titlePanel.add(titleLabel);
		
		titleField = new JTextField();
		titleField.setFont(FIELD_FONT);
		titleField.setPreferredSize(new Dimension(200, 24));
		
		titleField.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				fireViewEvent(CardListView.this, EventType.STACK_TITLE_CHANGE, titleField.getText());
			}
		});
		titlePanel.add(titleField);
		
		return titlePanel;
	}
	public boolean isTitleFocusedAndBlank() {
		return (titleField.hasFocus() && titleField.getText().equals(""));
	}
	
	public String getTitleText(){
		return titleField.getText();
	}

	public void setTitleText(String newTitle){
		titleField.setText(newTitle);
	}

	private JPanel makeButtonPane() {
		JPanel buttonPane = new JPanel(new GridBagLayout());
		
		JPanel studyPane = new JPanel(new FlowLayout(FlowLayout.LEADING,5,0));
		studyButton = new JButton(" Study Cards");
		studyButton.setIcon(Settings.ARROW_LEFT);
		studyButton.setPreferredSize(Settings.BUTTON_DEFAULT);
		studyPane.add(studyButton);
		
		JPanel createPane = new JPanel(new FlowLayout(FlowLayout.TRAILING,5,0));
		createButton = new JButton("Create Cards ");
		createButton.setIcon(Settings.ARROW_RIGHT);
		createButton.setHorizontalTextPosition(SwingConstants.LEFT);
		createButton.setPreferredSize(Settings.BUTTON_DEFAULT);
		createPane.add(createButton);
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		
		buttonPane.add(studyPane,c);
		
		c.gridx = 1;
		buttonPane.add(makeEditControls(),c);
		
		c.gridx = 2;
		buttonPane.add(createPane,c);
		
		studyButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fireViewEvent(CardListView.this, EventType.SWAP_STUDY, null);
				
		}});
		
		createButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				fireViewEvent(CardListView.this, EventType.SWAP_CARD_CREATE_VIEW, null);
				
		}});
		
		GlobalKeyListener.get().registerRightButton(createButton, MainView.CARD_VIEW);
		GlobalKeyListener.get().registerLeftButton(studyButton, MainView.CARD_VIEW);
		
		return buttonPane;
	}


	private JPanel makeEditControls() {
		JPanel pane = new JPanel();
		
		editButton = new JButton("Edit Selected");
		deleteButton = new JButton("Delete Selected");
		
		editButton.setPreferredSize(Settings.BUTTON_DEFAULT);
		deleteButton.setPreferredSize(Settings.BUTTON_DEFAULT);
		
		editButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				fireViewEvent(CardListView.this, EventType.EDIT_CARD, table.getSelectedRow());
			}
		});
		
		deleteButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fireViewEvent(CardListView.this, EventType.DELETE_CARD, table.getSelectedRows());
			}
		});
		
		
		pane.add(editButton);
		pane.add(deleteButton);
		
		return pane;
	}


	public void setTableModel(Stack stack) {
		tableModel.clear();
		for (int i = 0; i < stack.getNumOfCards(); i++){
			tableModel.setCard(stack.getCard(i),i);
		}
	}


	@Override
	public void setStatusBarListeners() {
		StatusBar.addStatusListener(titleField,"The name of this Stack (i.e. \"Psychology 1st Midterm\").");
		StatusBar.addStatusListener(table,"Your cards - double click to edit one in the \"Create Card\" view.");
		StatusBar.addStatusListener(studyButton,"Study the cards in your Stack.");
		StatusBar.addStatusListener(createButton,"Create cards to add to your Stack.");
		StatusBar.addStatusListener(editButton,"Edit the currently selected card in the \"Create Card\" view.");
		StatusBar.addStatusListener(deleteButton,"Delete the currently selected card (no undoing!)");
		
	}


	
}
