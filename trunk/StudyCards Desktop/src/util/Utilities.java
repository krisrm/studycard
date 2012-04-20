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

import model.Settings;

public class Utilities {
	private static int[] time = new int[3];
	public static String getExtension(File file) {
		String extension = file.getName();
		
		if (! extension.contains("."))
			return null;
		
		extension = extension.substring(extension.lastIndexOf(".")+1);
		return extension;
	}

	public static String getWithoutExtension(File namedFile) {
		String fileName = namedFile.getName();
		if (fileName == null)
			return null;
		if (! fileName.contains("."))
			return fileName;
		
		fileName = fileName.substring(0,fileName.lastIndexOf("."));
		return fileName;
	}

	public static File addExtension(File file) {
		
		String name = file.getName();
		if (name.indexOf('.') == -1) {
			name += "." + Settings.FILE_EXTENSION;
			file = new File(file.getParentFile(), name);
		}
		
		return file;
	}
	
	public static String formatTime(long timeElapsed){
		time = getTimeArray(timeElapsed);
		return String.format("%02d:%02d:%02d", time[2],time[1],time[0]);
		
	}
	
	public static int[] getTimeArray(long timeElapsed){
		time[0] = (int) timeElapsed % 60;
		time[1] = (int) (timeElapsed/60) % 60;
		time[2] = (int) (timeElapsed/3600) % 24;
		
		return time;
	}

}
