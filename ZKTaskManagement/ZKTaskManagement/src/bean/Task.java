package bean;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class Task {
	private Integer id;
	private String name;
	private String description;
	private String status;
	private Integer parent_id;
	private Task parent;
	private List<Task> children = new ArrayList<>();
	private int level;
	private String priority;
	private String comment;
	private Date date;
	private List<Comment> child = new ArrayList<>();
	private String depcode;
	private String url;
	
	public Task() {
		
	}
	
	public Task(Integer id, String name, String status, String description, String priority) {
		this.id = id;
		this.name = name;
		this.status= status;
		this.description = description;
		this.priority = priority;		
		child = new ArrayList<Comment>();
	}
	
	public Task(Integer id, String name, String status, String description, String priority, Date date, String depcode, String url) {
		this.id = id;
		this.name = name;
		this.status= status;
		this.description = description;
		this.priority = priority;		
		this.date = date;
		this.depcode = depcode;
		this.url = url;
		child = new ArrayList<Comment>();
	}
	
	public Task(Integer id, String name, String status, String description, Task parent) {
		this.id = id;
		this.name = name;
		this.status= status;
		this.description = description;
		this.parent = parent;
		if(parent != null) {
			parent.children.add(this);
		}
	}
	
	public Task(String name, String comment, Date date) {
		this.name = name;
		this.comment = comment;
		this.date = date;
	}
	
	public String getDepcode() {
		return depcode;
	}

	public void setDepcode(String depcode) {
		this.depcode = depcode;
	}

	public Task(String name, int level) {
		this.name = name;
		this.level = level;
		children = new ArrayList<Task>();
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	//	public Task(Task parent, int level) {
//		this.parent = parent;
//		this.level = level;
//		children = new ArrayList<Task>();
//	}
	public void addChild(Task node) {
		children.add(node);
	}
	
	public void addCmtChild(Comment cmt) {
		child.add(cmt);
	}
	
	public void appendChild(Task child) {
		if(children == null) 
			children = new ArrayList<Task>();
		children.add(child);
	}
	
	public void appendCmtChild(Comment cmt) {
		if(child == null) 
			child = new ArrayList<Comment>();
		child.add(cmt);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<Task> getChildren() {
		return children;
	}

	public void setChildren(List<Task> children) {
		this.children = children;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getParent_id() {
		return parent_id;
	}

	public void setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
	}
	
	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}
}
