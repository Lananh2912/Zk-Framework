package controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

import bean.Comment;
import bean.Task;
import bean.User;
import model.MyTreeModel;
import model.TaskServiceImpl;
import service.TaskService;

public class TaskVM {
	private TaskService taskService = new TaskServiceImpl("jdbc:mysql://localhost:3306/task_management", "root",
			"123456789");
	
	@Wire("#mytree")
	private Tree mytree;
	@Wire("#mytree2")
	private Tree mytree2;
	@Wire("#mytree3")
	private Tree mytree3;
	
	private List<Object> tasks = new ArrayList<Object>();
	private List<Task> doingTasks = new ArrayList<Task>();
	private List<Task> doneTasks = new ArrayList<Task>();
	
	TreeModel _model;
	TreeModel _model1;
	TreeModel _model2;
	private List<Task> allTask;
	
	public TreeModel getModel1() {
		if(_model == null) {
			MyTreeModel a = new MyTreeModel(getRoot());
			a.setMultiple(true);
			_model = a;
		}
		return _model;
	}
	
	public TreeModel getModel2() {
		if(_model1 == null) {
			MyTreeModel a = new MyTreeModel(getRoot1());
			a.setMultiple(true);
			a.addOpenPath(new int[] {0});
			_model1 = a;
		}
		return _model1;
	}
	
	public TreeModel getModel3() {
		if(_model2 == null) {
			MyTreeModel a = new MyTreeModel(getRoot2());
			a.setMultiple(true);
			a.addOpenPath(new int[] {0});
			_model2 = a;
		}
		return _model2;
	}
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		mytree.setFocus(true);
		Session sess = Sessions.getCurrent();
		User user = (User) sess.getAttribute("sessionCurr");
		if((user.getDepcode() != null && "tvp".contentEquals(user.getDepcode()))) {
			user.setDepcode(null);
		}
		
		List<Task> todoTask = taskService.getTodoTask(user.getDepcode());
		List<Task> doingTask = taskService.getDoingTask(user.getDepcode());
		List<Task> doneTask = taskService.getDoneTask(user.getDepcode());
		
		Task m1 = new Task("To do", 1);
		
		for(Task task1 : todoTask) {
			List<Comment> cmtList = taskService.selectComment(task1.getName());
//			System.out.print(task1.getDate());
			for(Comment cmt : cmtList) {
				task1.addCmtChild(cmt);
			}
			m1.addChild(task1);
		}
//		for(Task task1 : todoTask) {
//			Task m2 = new Task(task1.getName(), 2);
//			for(Task cmt : cmtList) {
//				if (task1.getName() == cmt.getName()) {
//					m2.addChild(cmt);	
//				}
//			}	
//			m1.addChild(m2);
//		}
		tasks.add(m1);
		Task m2 = new Task("Doing", 1);
		for(Task task2 : doingTask) {
			m2.addChild(task2);
		}
		doingTasks.add(m2);
		Task m3 = new Task("Done", 1);
		for(Task task3 : doneTask) {
			m3.addChild(task3);
		}
		doneTasks.add(m3);
		
		
	}
	
	public Task getRoot() {
		Task superRoot = new Task("Task", 0);
//		Task root = new Task("Task", 0);
		Task m1 = (Task) tasks.get(0);
//		Comment m2 = (Comment) tasks.get(1);
//		Task m3 = tasks.get(2);
//		root.addChild(m1);
//		root.addCmtChild(m2);
		superRoot.addChild(m1);
//		root.addChild(m2);
//		root.addChild(m3);
		return superRoot;
	}
	
	public Task getRoot1() {
		Task superRoot = new Task("Task", 0);
//		Task root = new Task("Task", 0);
		Task m1 = doingTasks.get(0);
//		Task m2 = tasks.get(1);
//		Task m3 = tasks.get(2);
		superRoot.addChild(m1);
//		root.addChild(m1);
//		root.addChild(m2);
//		root.addChild(m3);
		return superRoot;
	}
	
	public Task getRoot2() {
		Task superRoot = new Task("Task", 0);
//		Task root = new Task("Task", 0);
		Task m1 = doneTasks.get(0);
//		Task m2 = tasks.get(1);
//		Task m3 = tasks.get(2);
		superRoot.addChild(m1);
//		root.addChild(m1);
//		root.addChild(m2);
//		root.addChild(m3);
		return superRoot;
	}
	
	@Command
	public void onFulfill() {
		doCollapseExpandAll(mytree, true);
	}
	
	@Command
	public void onClickComment(@BindingParam("id") Integer id) {
		
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		Window window = (Window) Executions.createComponents("insertComment.zul", null, map);
		window.doModal();
	}
	
	@Command
	public void onDropItem(@BindingParam("dragged")Component dragged,  @BindingParam("target")Component target) {
		
		Treerow trDragging = (Treerow) dragged;
		Treeitem tiFatherDragging = (Treeitem) trDragging.getParent();
		
		Treeitem item = (Treeitem) ((Treerow) target).getParent();

		Task dragTask = tiFatherDragging.getValue();
//		System.out.print(dragTask.getStatus());
		
		Task dropTask = item.getValue();
//		System.out.print(dropTask.getName());
		if((dropTask.getName() == "To do") || (dropTask.getName() == "Doing") || (dropTask.getName() == "Done")) {	
			dragTask.setStatus(dropTask.getName());
			if("Done".equals(dragTask.getStatus())) {
				dragTask.setPriority("");
			}
			taskService.updateTask(dragTask);
			
			Treechildren tc = item.getTreechildren();
			
			if (tc == null)
				tc = new Treechildren();
			
			tc.appendChild(tiFatherDragging);
			tc.setParent(item);
		}
	}
	
//	@Command
//	public void onDoubleClick() {
//		Window window = (Window) Executions.createComponents("addTask.zul", null, null);
//		window.doModal();
//	}
	
	public static void doCollapseExpandAll(Component component,
			boolean aufklappen) {
		if (component instanceof Treeitem) {
			Treeitem treeitem = (Treeitem) component;
			treeitem.setOpen(aufklappen);
		}
		Collection<?> com = component.getChildren();
		if (com != null) {
			for (Iterator<?> iterator = com.iterator(); iterator.hasNext();) {
				doCollapseExpandAll((Component) iterator.next(), aufklappen);

			}
		}
	}
	
}
