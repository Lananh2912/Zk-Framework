package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.zul.DefaultTreeNode;

import bean.Comment;
import bean.Task;
import bean.TaskList;

public interface TaskService {
	
	public List<Task> search(String keyword, String depcode, String fromDate, String toDate);
	
	public boolean insertTask(Task task);
	
	public boolean updateTask(Task task);
	
	public boolean deleteTask(int id);
	
	public Task selectTask(int id);
	
	public Task selectTaskByName(String name);
	
	public TaskList getTaskListRoot();
	
	public ArrayList<Task> loadTaskList();
	
	public List<Task> getTodoTask(String depcode);
	
	public List<Task> getDoingTask(String depcode);
	
	public List<Task> getDoneTask(String depcode);
	
	public List<Task> getCancelTask(String depcode);
	
	public boolean insertComment(Comment comment); 
	
	public List<Comment> selectComment(String taskname);
	
}
