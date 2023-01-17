package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.zkoss.zul.DefaultTreeNode;

import bean.Comment;
import bean.Task;
import bean.TaskList;
import service.TaskService;

public class TaskServiceImpl implements TaskService {
	private String jdbcURL;
	private String jdbcUsername;
	private String jdbcPassword;
	private Connection conn;

	private List<String> statusListDefault;
	private TaskList rootTaskList;
	private Map<String, List<Task>> queryResults;
	private List<Task> allTask;

	public TaskServiceImpl(String jdbcURL, String jdbcUsername, String jdbcPassword) {
		this.jdbcURL = jdbcURL;
		this.jdbcUsername = jdbcUsername;
		this.jdbcPassword = jdbcPassword;
	}



	public TaskList getTaskListRoot() {
		return rootTaskList;
	}

	public ArrayList<Task> loadTaskList() {
		PreparedStatement pst = null;
		try {

			ArrayList<Task> allTask = new ArrayList<Task>();
			HashMap<Integer, Task> taskMap = new HashMap<>();

			conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
			String sqlCommand = "Select * from task ";
			pst = conn.prepareStatement(sqlCommand);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Integer id = rs.getInt(1);
				String name = rs.getString(2);
				String status = rs.getString(3);
				String description = rs.getString(4);
				Integer parent_id = rs.getInt(5);
				Task parent = taskMap.get(parent_id);
				Task task = new Task(id, name, status, description, parent);
				taskMap.put(id, task);
				if (parent == null) {
					allTask.add(task);
				}
				return allTask;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnect(conn);
			DBConnection.closeStatement(pst);
		}
		return null;
	}

	public List<Task> search(String keyword, String depcode, String fromDate, String toDate) {
		List<Task> result = new LinkedList<Task>();
		PreparedStatement pst = null;
		try {
			conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
			String sqlCommand = "Select * from task where status <> 'Cancel' and date >= ? and date <= ? ";

			if ((keyword != null) && (!"".equals(keyword)) && (depcode != null)) {
				sqlCommand += "AND " + "name LIKE ? " + " OR " + " status LIKE ? " + " AND (depcode = ? OR depcode = '') " + "ORDER BY status desc, date desc";
			}
			else if ((keyword != null) && (!"".equals(keyword))) {
				sqlCommand += "AND " + "name LIKE ? " + " OR " + " status LIKE ? "  + "ORDER BY status desc, date desc";
			}
			else if (depcode != null) {
				sqlCommand += " AND (depcode = ? OR depcode = '') " + "ORDER BY status desc, date desc";
			}
			else {
				sqlCommand += "ORDER BY status desc, date desc";
			}
			pst = conn.prepareStatement(sqlCommand);
			pst.setString(1, fromDate);
			pst.setString(2, toDate);
			if (((keyword != null && !"".equals(keyword)) && depcode != null)) {
				pst.setString(3, "%" + keyword.toLowerCase() + "%");
				pst.setString(4, "%" + keyword.toLowerCase() + "%");
				pst.setString(5, depcode);
			} else if (depcode != null) {
				pst.setString(3, depcode);
			} else if ((keyword != null) && (!"".equals(keyword)) && (depcode == null)){
				pst.setString(3, "%" + keyword.toLowerCase() + "%");
				pst.setString(4, "%" + keyword.toLowerCase() + "%");
			}
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Integer id = rs.getInt("id");
				String name = rs.getString("name");
				String status = rs.getString("status");
				String description = rs.getString("description");
				String priority = rs.getString("priority");
				Date date = rs.getDate("date");
				String depCode = rs.getString("depcode");
				String url = rs.getString("url");
				Task task = new Task(id, name, status, description, priority, date, depCode, url);
				result.add(task);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnect(conn);
			DBConnection.closeStatement(pst);
		}
		return result;
	}

	public boolean insertTask(Task task) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "INSERT INTO task(name, status, description, priority, date, depcode) values(?, ?, ?, ?, ?, ?)";
		PreparedStatement pst = null;
		int result = 0;
		try {
			pst = conn.prepareStatement(sqlCommand);
			pst.setString(1, task.getName());
			pst.setString(2, task.getStatus());
			pst.setString(3, task.getDescription());
			pst.setString(4, task.getPriority());
			pst.setDate(5, java.sql.Date.valueOf(java.time.LocalDate.now()));
			pst.setString(6, task.getDepcode());
			result = pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.closeConnect(conn);
			DBConnection.closePreparedStatement(pst);
		}

		if (result == 1) {
			return true;
		}
		return false;
	}

	public boolean updateTask(Task task) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sql = "UPDATE task SET name = ?, status = ?, description = ?, priority = ?, depcode = ? , url = ?";
		sql += " WHERE id = ?";
		PreparedStatement pst = null;
		int rowUpdated = 0;
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, task.getName());
			pst.setString(2, task.getStatus());
			pst.setString(3, task.getDescription());
			pst.setString(4, task.getPriority());
			pst.setString(5, task.getDepcode());
			pst.setString(6, task.getUrl());
			pst.setInt(7, task.getId());
			
			rowUpdated = pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.closeConnect(conn);
			DBConnection.closePreparedStatement(pst);
		}
		if (rowUpdated == 0) {
			return true;
		}
		return false;
	}

	public boolean deleteTask(int id) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sql = "DELETE FROM task WHERE id = ?";
		int rowDeleted = 0;
		try {
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setInt(1, id);
			rowDeleted = pst.executeUpdate();
			DBConnection.closeConnect(conn);
			DBConnection.closePreparedStatement(pst);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (rowDeleted == 1) {
			return true;
		}
		return false;
	}

	public Task selectTask(int id) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sql = "SELECT * FROM task WHERE id = ?";
		Task task = new Task();
		try {
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setInt(1, id);
			ResultSet result = pst.executeQuery();
			if (result.next()) {
				String name = result.getString("name");
				String status = result.getString("status");
				String description = result.getString("description");
				String priority = result.getString("priority");
				Date date = result.getDate("date");
				String depCode = result.getString("depcode");
				String url = result.getString("url");
				task = new Task(id, name, status, description, priority, date, depCode, url);
				DBConnection.closeConnect(conn);
				DBConnection.closePreparedStatement(pst);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return task;
	}
	
	
	
	public List<Task> getTodoTask(String depcode){
		List<Task> result = new LinkedList<Task>();
		PreparedStatement pst = null;
		try {
			conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
			String sqlCommand = "Select * from task where status = 'To do' ";

			if (depcode != null) {
				sqlCommand += " AND (depcode = ? OR depcode = '') " + "ORDER BY  date desc";
			}
			else {
				sqlCommand += "ORDER BY date desc";
			}
			pst = conn.prepareStatement(sqlCommand);
			if (depcode != null) {
				pst.setString(1, depcode);
			}
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Integer id = rs.getInt("id");
				String name = rs.getString("name");
				String status = rs.getString("status");
				String description = rs.getString("description");
				String priority = rs.getString("priority");

				Task task = new Task(id, name, status, description, priority);
				result.add(task);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnect(conn);
			DBConnection.closeStatement(pst);
		}
		return result;
	}
	
	public List<Task> getDoingTask(String depcode){
		List<Task> result = new LinkedList<Task>();
		PreparedStatement pst = null;
		try {
			conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
			String sqlCommand = "Select * from task where status = 'Doing' ";
			if (depcode != null) {
				sqlCommand += " AND (depcode = ? OR depcode = '')" + "ORDER BY  date desc";
			}
			else {
				sqlCommand += "ORDER BY date desc";
			}
			pst = conn.prepareStatement(sqlCommand);
			if (depcode != null) {
				pst.setString(1, depcode);
			}
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Integer id = rs.getInt("id");
				String name = rs.getString("name");
				String status = rs.getString("status");
				String description = rs.getString("description");
				String priority = rs.getString("priority");
				Task task = new Task(id, name, status, description, priority);
				result.add(task);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnect(conn);
			DBConnection.closeStatement(pst);
		}
		return result;
	}
	
	public List<Task> getDoneTask(String depcode){
		List<Task> result = new LinkedList<Task>();
		PreparedStatement pst = null;
		try {
			conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
			String sqlCommand = "Select * from task where status = 'Done' ";
			if (depcode != null) {
				sqlCommand += " AND (depcode = ? OR depcode = '')" + "ORDER BY  date desc";
			}
			else {
				sqlCommand += "ORDER BY date desc";
			}
			pst = conn.prepareStatement(sqlCommand);
			if (depcode != null) {
				pst.setString(1, depcode);
			}
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Integer id = rs.getInt("id");
				String name = rs.getString("name");
				String status = rs.getString("status");
				String description = rs.getString("description");
				String priority = rs.getString("priority");
				Task task = new Task(id, name, status, description, priority);
				result.add(task);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnect(conn);
			DBConnection.closeStatement(pst);
		}
		return result;
	}
	
	public List<Task> getCancelTask(String depcode){
		List<Task> result = new LinkedList<Task>();
		PreparedStatement pst = null;
		try {
			conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
			String sqlCommand = "Select * from task where status = 'Cancel' ";
			if (depcode != null) {
				sqlCommand += " AND (depcode = ? OR depcode = '') " + "ORDER BY date desc";
			}
			else {
				sqlCommand += "ORDER BY date desc";
			}
			pst = conn.prepareStatement(sqlCommand);
			if (depcode != null) {
				pst.setString(1, depcode);
			}
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Integer id = rs.getInt("id");
				String name = rs.getString("name");
				String status = rs.getString("status");
				String description = rs.getString("description");
				String priority = rs.getString("priority");
				Date date = rs.getDate("date");
				String depCode = rs.getString("depcode");
				String url = rs.getString("url");
				Task task = new Task(id, name, status, description, priority, date, depCode, url);
				result.add(task);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnect(conn);
			DBConnection.closeStatement(pst);
		}
		return result;
	}
	
	public boolean insertComment(Comment cmt) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "INSERT INTO usercomment(username, comment, taskname, date) values(?, ?, ?, ?)";
		PreparedStatement pst = null;
		int result = 0;
		try {
			pst = conn.prepareStatement(sqlCommand);
			pst.setString(1, cmt.getUsername());
			pst.setString(2, cmt.getComment());
			pst.setString(3, cmt.getTaskname());
			pst.setDate(4, java.sql.Date.valueOf(java.time.LocalDate.now()));
			result = pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.closeConnect(conn);
			DBConnection.closePreparedStatement(pst);
		}

		if (result == 1) {
			return true;
		}
		return false;
	}
	
	public List<Comment> selectComment(String taskname) {
		List<Comment> result = new LinkedList<Comment>();
		PreparedStatement pst = null;
		try {
			conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
			String sqlCommand = "Select * from usercomment where taskname = ? order by date desc, id desc";

			pst = conn.prepareStatement(sqlCommand);
			pst.setString(1, taskname);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Integer id = rs.getInt("id");
				String username = rs.getString("username");
				String comment = rs.getString("comment");
				Date date = rs.getDate("date");
				Comment cmt = new Comment(id, username, comment, taskname, date);
				result.add(cmt);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnect(conn);
			DBConnection.closeStatement(pst);
		}
		return result;
	}
	
	public Task selectTaskByName(String name) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sql = "SELECT * FROM task WHERE name = ?";
		Task task = new Task();
		try {
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, name);
			ResultSet result = pst.executeQuery();
			if (result.next()) {
				Integer id = result.getInt("id");
				String status = result.getString("status");
				String description = result.getString("description");
				String priority = result.getString("priority");
				Date date = result.getDate("date");
				String depCode = result.getString("depcode");
				String url = result.getString("url");
				task = new Task(id, name, status, description, priority, date, depCode, url);
				DBConnection.closeConnect(conn);
				DBConnection.closePreparedStatement(pst);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return task;
	}

}
