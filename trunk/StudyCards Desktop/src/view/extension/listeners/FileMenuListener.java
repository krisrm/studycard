package view.extension.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import util.EventType;
import view.MainView;

public class FileMenuListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem source = (JMenuItem) e.getSource();
		
		if (source.getText().contains("Open")){
			MainView.fireStaticViewEvent(EventType.FILE_OPEN, null);
		} else if (source.getText().contains("New")){
			MainView.fireStaticViewEvent(EventType.FILE_NEW, null);
		} else if (source.getText().contains("Save As")){
			MainView.fireStaticViewEvent(EventType.FILE_SAVE_AS, null);
		} else if (source.getText().contains("Save")){
			MainView.fireStaticViewEvent(EventType.FILE_SAVE, null);
		} else if (source.getText().contains("Exit")){
			MainView.fireStaticViewEvent(EventType.FILE_EXIT, null);
		}

	}

}
