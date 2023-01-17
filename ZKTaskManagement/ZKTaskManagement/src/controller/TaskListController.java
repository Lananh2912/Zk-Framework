package controller;

import java.util.ArrayList;
import java.util.LinkedList;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.TreeNode;

import bean.Task;
import bean.TaskList;
import model.TaskModel;
import model.TaskServiceImpl;
import service.TaskService;

public class TaskListController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	private DefaultTreeModel<Task> treeModel;
	private TreeModel<TreeNode<TaskList>> taskListModel;
	@Wire
	private Tree taskListTree;

	private ArrayList<Task> result;

	private TaskService taskService = new TaskServiceImpl("jdbc:mysql://localhost:3306/task_management", "root",
			"123456789");
	
	public TaskListController() {
		
		TaskList taskListRoot = taskService.getTaskListRoot();
		TaskTreeNode rootNode = constructTaskTreeNode(taskListRoot);
		
		taskListModel = new DefaultTreeModel<TaskList>(rootNode);
		((DefaultTreeModel<TaskList>)taskListModel).addOpenPath(new int[]{0});
	}
	
	private TaskTreeNode constructTaskTreeNode(TaskList taskList) {
		TaskTreeNode taskListNode = new TaskTreeNode(taskList);
		LinkedList<TaskTreeNode> queue = new LinkedList<TaskTreeNode>(); // BFS
		queue.add(taskListNode);
		while(!queue.isEmpty()) {
			TaskTreeNode node = queue.remove();
			for(TaskList childCategory : node.getData().getChildren()) {
				TaskTreeNode child = new TaskTreeNode(childCategory);
				node.add(child);
				queue.add(child);
			}
		}
		TaskTreeNode rootNode = new TaskTreeNode(null); // won't show
		rootNode.add(taskListNode);
		return rootNode;
	}
	
	public TreeModel<TreeNode<TaskList>> getTaskListModel() {
		return taskListModel;
	}

//	public void doAfterCompose(Component comp) throws Exception {
//		super.doAfterCompose(comp);
//
//		result = taskService.loadTaskList();
//	}
//
//	public ArrayList<Task> getResult() {
//		return result;
//	}

//	private TreeModel<TreeNode<TaskList>> taskListModel;

//	public TaskListController() {
//		TaskList taskListRoot = taskService.getTaskListRoot();
//		TaskTreeNode rootNode = constructTaskTreeNode(taskListRoot);
//		
//		taskListModel = new DefaultTreeModel<TaskList>(rootNode);
//		((DefaultTreeModel<TaskList>)taskListModel).addOpenPath(new int[] {0});
//	};
//	
//	private TaskTreeNode constructTaskTreeNode(TaskList taskList) {
//		TaskTreeNode taskListNode = new TaskTreeNode(taskList, taskService.countByFilter(taskList.getName()));
//		LinkedList<TaskTreeNode> queue = new LinkedList<TaskTreeNode>();
//		queue.add(taskListNode);
//		while(!queue.isEmpty()) {
//			TaskTreeNode node = queue.remove();
//			for(TaskList childTaskList : node.getData().getChildren()) {
//				TaskTreeNode child = new TaskTreeNode(childTaskList, taskService.countByFilter(childTaskList.getName()));
//				node.add(child);
//				queue.add(child);
//			}
//		}
//		TaskTreeNode rootNode = new TaskTreeNode(null, -1);
//		rootNode.add(taskListNode);
//		return rootNode;
//	}
//	
//	public TreeModel<TreeNode<TaskList>> getTaskListModel(){
//		return taskListModel;
//	}
}
