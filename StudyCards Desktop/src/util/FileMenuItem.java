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

package util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.Serializable;

import view.MainView;

/*FIXME - minor bug where if a file is removed from the disk, 
 * the recent files don't get built on first run*/

public class FileMenuItem implements Serializable{
	
	private static final long serialVersionUID = -2396932828592854941L;
	private File file;
	private String shortName;
	

	public FileMenuItem(File newFile){
		file = newFile;
		shortName = Utilities.getWithoutExtension(file);
	}

	public String getShortFileName() {
		return shortName;
	}

	public ActionListener getActionListener() {
		// tell the model we want to open this file.
		
		return new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.fireStaticViewEvent(EventType.FILE_OPEN, file);
				
			}
		};
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileMenuItem other = (FileMenuItem) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		return true;
	}
}
