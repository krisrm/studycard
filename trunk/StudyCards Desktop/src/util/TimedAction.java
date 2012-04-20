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

import javax.swing.Timer;

public class TimedAction implements ActionListener{
	public TimedAction(int delay, Runnable action) {
		this.delay = delay;
		this.action = action;
	}
	
	private Timer timer;
	private int delay;
	private Runnable action;
	
	public void start() {
		timer = new Timer(delay, this);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		action.run();
		timer.stop();
	}
	
	
	
}
