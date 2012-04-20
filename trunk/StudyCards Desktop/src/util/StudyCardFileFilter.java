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

import java.io.File;

import javax.swing.filechooser.FileFilter;

import model.Settings;

public class StudyCardFileFilter extends FileFilter {

	@Override
	public boolean accept(File file) {
		String extension = Utilities.getExtension(file);
		
		//FIXME Unnamed files will get through
		if (extension == null)
			return true;
		
		return extension.equals(Settings.FILE_EXTENSION);
	}

	@Override
	public String getDescription() {
		return "StudyCards Desktop files (." + Settings.FILE_EXTENSION +")";
	}

}
