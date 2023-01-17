package controller;

import java.util.LinkedList;

import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.TreeNode;

import bean.TaskList;

public class TaskTreeNode extends DefaultTreeNode<TaskList> {
	private static final long serialVersionUID = 1L;
	int count;
	
	public TaskTreeNode(TaskList taskList) {
		super(taskList, new LinkedList<TreeNode<TaskList>>());
		
	}
	
	public String getDescription() {
		return getData().getDescription();
	}
	
	public int getCount() {
		return count;
	}
	
	public boolean isLeaf() {
		return getData() != null && getData().getChildren().isEmpty();
	}
}
