package view.extension.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import util.EventType;
import view.MainView;

public class EditMenuListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem source = (JMenuItem) e.getSource();
		
		if (source.getText().contains("Cut")){
			
		} else if (source.getText().contains("Paste")){
			
		} else if (source.getText().contains("Study Cards")){
			MainView.fireStaticViewEvent(EventType.SWAP_STUDY, null);
		} else if (source.getText().contains("View Stack")){
			MainView.fireStaticViewEvent(EventType.SWAP_CARD_VIEW, null);
		} else if (source.getText().contains("Create Cards")){
			MainView.fireStaticViewEvent(EventType.SWAP_CARD_CREATE_VIEW, null);
		}
	}

}
