package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.zkoss.zk.ui.util.ForEach;
import org.zkoss.zul.DefaultTreeNode;

import bean.Task;

public class TaskModel {
	private String jdbcURL;
	private String jdbcUsername;
	private String jdbcPassword;
	private Connection conn;
	
	public TaskModel(String jdbcURL, String jdbcUsername, String jdbcPassword) {
		this.jdbcURL = jdbcURL;
		this.jdbcUsername = jdbcUsername;
		this.jdbcPassword = jdbcPassword;
	}
	
	public List getTodoTask(){
		List<Task> todoTask = new LinkedList<Task>();
		PreparedStatement pst = null;
		try {
			conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
			String sqlCommand = "Select * from task where status = 'To do';";
			pst = conn.prepareStatement(sqlCommand);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Integer id = rs.getInt(1);
				String name = rs.getString(2);
				String status = rs.getString(3);
				String description = rs.getString(4);
				String priority = rs.getString(4);
				Task task = new Task(id, name, status, description, priority);
				todoTask.add(task);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnect(conn);
			DBConnection.closeStatement(pst);
		}
		return null;
	}
	
	private static DefaultTreeNode<Task> newNode(Task task, DefaultTreeNode<Task>... children) {
		if (children == null || children.length == 0) {
            return new DefaultTreeNode<>(task);
        }
        return new DefaultTreeNode<>(task, children);
	}
}
