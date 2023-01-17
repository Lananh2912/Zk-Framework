package bean;

import java.util.LinkedList;
import java.util.List;

public class TaskList {
	private Integer id;
	private String name;
	private String description;
	private List<TaskList> children = new LinkedList<TaskList>();
	
	public TaskList(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void addChild(TaskList child) {
		if(child != null) {
			children.add(child);
		}
	}
	
	public void removeChild(TaskList child) {
		if(child != null) {
			children.remove(child);
		}
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<TaskList> getChildren() {
		return children;
	}

	public void setChildren(List<TaskList> children) {
		this.children = children;
	}
	
}
