package bean;

import java.util.Date;

public class Comment {
	private Integer id;
	private String username;
	private String comment;
	private String taskname;
	private Date date;
	
	public Comment() {
		
	}

	public Comment(Integer id, String username, String comment, String taskname, Date date) {
		this.id = id;
		this.username = username;
		this.comment = comment;
		this.taskname = taskname;
		this.date = date;
	}
	
	

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTaskname() {
		return taskname;
	}

	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}
