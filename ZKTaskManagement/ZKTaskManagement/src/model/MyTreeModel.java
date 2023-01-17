package model;

import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.ext.Selectable;

import bean.Task;

public class MyTreeModel extends AbstractTreeModel<Object> implements Selectable<Object> {
	private static final long serialVersionUID = 1L;
	private Task _root;
	
	public MyTreeModel(Object root) {
		super(root);
		_root = (Task) root;
	}
	
	@Override
	public boolean isLeaf(Object node) {
		return ((Task) node).getChildren().size() == 0;
	}
	
	@Override
	public Object getChild(Object parent, int index) {
		return ((Task) parent).getChildren().get(index);
	}
	
	@Override
	public int getChildCount(Object parent) {
		return ((Task) parent).getChildren().size();  
	}
	
	public boolean isMutiple()
	{
		return true;
	}
}
