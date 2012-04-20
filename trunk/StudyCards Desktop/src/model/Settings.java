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

package model;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.pushingpixels.substance.api.skin.SubstanceMistAquaLookAndFeel;

import util.EventType;
import util.FileMenuItem;
import view.MainView;
import controller.MainController;

public class Settings implements Serializable {

	private static final long serialVersionUID = -7955277313153215335L;

	private static Settings settings;

	public static final String VERSION = "1.1";
	public static final int VERSION_NUM = 2;
	public static final String FILE_PATH = System.getProperty("user.dir")
			+ File.separator + "files" + File.separator;
	public static final File SETTINGS_FILE = new File(FILE_PATH
			+ "../settings.scs");
	
	public static Image ICON;
	public static ImageIcon ARROW_BOTH;
	public static ImageIcon ARROW_LEFT;
	public static ImageIcon ARROW_RIGHT;
	public static ImageIcon ADD_ICON;
	public static ImageIcon CORRECT_ICON;
	public static ImageIcon INCORRECT_ICON;
	public File lastFile;
	
	public static final String TITLE = "Study Cards";
	public static final Dimension DEFAULT_WINDOW_SIZE = new Dimension(647, 412);
	public static final int BORDER_WIDTH = 10;

	public static final String HEADER_HTML = "<center><b><font face='verdana' color = 'white' size = '48'>StudyCards</font></b>" +
			"<br/><font face='verdana' color = 'white'><i>-Desktop-</i></font></center>";

	public static final String BODY_HTML = "<font face='verdana' color = 'white' ><center>" +
			"<p>Author: Kristofer Mitchell</p><p>Version: "+ VERSION +
			"</p></center></font>";

	public static final String HELP_FILE_URL = "http://www.on-key.ca/projects/studycards/help.html";

	public static final String FILE_EXTENSION = "stc";

	public static final Dimension BUTTON_DEFAULT = new Dimension(110,21);
	
	public static final Font CARD_CREATE_FONT = new Font("sansserif",Font.PLAIN,14);

	public static final Font QUESTION_FONT = new Font("sansserif",Font.BOLD,22);

	public static final int MAX_RECENT_ITEMS = 5;

	public static final int SWAP_TIME = 800;
	
	public Dimension windowSize;
	public Point windowPosition;
	public double percentageThreshold;
	public double correctThreshold;
	
	private File lastLoaded;
	private List<FileMenuItem> recentlyOpened;
	

	public Settings() {
		windowSize = DEFAULT_WINDOW_SIZE;
		windowPosition = null; // for center; handled in MainView
		percentageThreshold = 0.45;
		correctThreshold = 2;
	}

	public static Settings get() {
		if (settings == null)
			settings = loadSettings();
		
		return settings;
	}

	public static void saveSettings() {
		Settings s = get();
		s.windowSize = MainView.getFrame().getSize();
		s.windowPosition = MainView.getFrame().getLocation();
		
		Model model = Model.getModel();
		s.lastLoaded = model.getCurrentFile();
		s.recentlyOpened = model.getRecentlyOpened();
		
		writeObject();
	}

	public static Settings loadSettings() {

		try {
			ObjectInputStream objInStream = new ObjectInputStream(
					new FileInputStream(SETTINGS_FILE));
			Object newSettings = objInStream.readObject();

			if (newSettings != null && newSettings instanceof Settings) {
				objInStream.close();
				return (Settings) newSettings;
			}

			objInStream.close();

		} catch (FileNotFoundException e) {
			return new Settings();

		} catch (Exception e) {
			e.printStackTrace();

			return new Settings();
			
		}
		
		return null;
	}

	private static void writeObject() {
		FileOutputStream fileOutputStream = null;
		ObjectOutputStream objOutStream = null;
		try {
			fileOutputStream = new FileOutputStream(SETTINGS_FILE);
			objOutStream = new ObjectOutputStream(fileOutputStream);
			objOutStream.writeObject(settings);
			objOutStream.close();
		} catch (Exception e) {
			// FAILED TO SAVE
			System.err.println("Serialization failed");

		}
	}
	
	public static URL buildResource(String name){
		Card c = new Card();
		return c.getClass().getResource("/files/"+name);
	}

	public static void main(String args[]) {
		
		ICON = new ImageIcon(buildResource("icon.png")).getImage();
		ARROW_BOTH = new ImageIcon(buildResource("arrow_both.png"));
		ARROW_LEFT = new ImageIcon(buildResource("arrow_left.png"));
		ARROW_RIGHT = new ImageIcon(buildResource( "arrow_right.png"));
		ADD_ICON = new ImageIcon(buildResource("add.png"));
		CORRECT_ICON = new ImageIcon(buildResource("correct.png"));
		INCORRECT_ICON = new ImageIcon(buildResource("incorrect.png"));
		
		Settings.get();
		
		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					JFrame.setDefaultLookAndFeelDecorated(true);
//					UIManager.setLookAndFeel(new SubstanceGeminiLookAndFeel());
					UIManager.setLookAndFeel(new SubstanceMistAquaLookAndFeel());
				} catch (Exception e) {
					System.out.println("Substance failed to initialize");
				}

				Model m = Model.getModel();
				new MainView(new MainController());
				MainView.getFrame().setVisible(true);
				m.notifyControllers();
				
				//remember previously loaded cards...
				m.startRecentlyOpened(get().recentlyOpened);
				
				
				//load the last set of cards
				try{
					if (get().lastLoaded != null)
						m.loadCards(get().lastLoaded);
					else
						MainView.fireStaticViewEvent(EventType.FILE_NEW, null);
						
				} catch (Exception e){
					MainView.fireStaticViewEvent(EventType.FILE_NEW, null);
				}
				
				Settings.saveSettings();
			}
		};

		SwingUtilities.invokeLater(r);
		
	}
}
