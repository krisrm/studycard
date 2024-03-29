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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import model.Settings;

public class AboutDialog
{
  private static final Dimension SIZE = new Dimension(320, 180);
  private JDialog dialogBox;
  private JPanel dialogPanel;
  private JTextPane topTextBox;
  
  public AboutDialog(Component center)
  {
    dialogBox = new JDialog();
    dialogBox.setTitle("StudyCards - About");
    dialogBox.setIconImage(Settings.ICON);
    dialogBox.setSize(SIZE);
    dialogBox.setResizable(false);
    dialogBox.setLocationRelativeTo(center);
    
    dialogPanel = new JPanel(new BorderLayout());
    
    topTextBox = new JTextPane();
    
    topTextBox.setContentType("text/html");
    
    topTextBox.setEditable(false);
    
    dialogPanel.add(topTextBox, "Center");
    
    //topTextBox.setText("<center><b><font face='verdana' color = 'white' size = '48'>StudyCards</font></b><br/><font face='verdana' color = 'white'><i>-Desktop-</i></font></center><font face='verdana' color = 'white' ><center><p>Author: Kristofer Mitchell</p><p>Version: 1.0a</p></center></font>");
    topTextBox.setText(Settings.HEADER_HTML+Settings.BODY_HTML);
    topTextBox.setBackground(Color.BLACK);
    
    dialogBox.setContentPane(dialogPanel);
    dialogBox.setDefaultCloseOperation(2);
  } 
  
  public void setVisible(boolean visible)
  {
    dialogBox.setVisible(visible);
  } 
} 