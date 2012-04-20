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
