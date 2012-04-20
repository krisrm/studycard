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

package view.extension;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;


public class StatusBar extends JPanel {
	private static final long serialVersionUID = 1873928414803442745L;

	private static JLabel statusMessage;
	public StatusBar(){
		this(new FlowLayout(FlowLayout.LEADING,5,2));
		statusMessage = new JLabel(" ");
		statusMessage.setFont(new Font("SansSerif",Font.ITALIC,12));
		//statusMessage.setForeground(new Color(250,250,0));
		this.setBackground(Color.LIGHT_GRAY);
		this.setBorder(new OutlineBorder(1,0,0,0));
		this.add(statusMessage);
	}


	public StatusBar(FlowLayout flowLayout) {
		super(flowLayout);
	}

	public static void setStatus(String newStatus){
		statusMessage.setText(newStatus + " ");
	}
	
	public static void clearStatus(){
		statusMessage.setText(" ");
	}
	
	public static void addStatusListener(Component hoverOver, final String toDisplay){
		hoverOver.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				setStatus(toDisplay);
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				clearStatus();
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}	
		});
	}
	
}
