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

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JPanel;

public class ViewList<T extends Component> extends ArrayList<T>{

	private static final long serialVersionUID = 6979275345507426558L;

	private ImagePanel swapPane;
	
	public ViewList(ImagePanel swapPane){
		super();
		this.swapPane = swapPane;
	}
	
	@Override
	public boolean add(T object){
		swapPane.add(object);
		
		return super.add(object);
	}
	
	@Override
	public T remove(int index){
		
		swapPane.remove(this.get(index));
		
		return super.remove(index);
	}
	
	@Override
	public void clear(){
		swapPane.removeAll();
		swapPane.add(new JPanel());
		
		super.clear();
	}
	
	@Override
	public int size(){
		
		return swapPane.getComponentCount();
	}
	
}
