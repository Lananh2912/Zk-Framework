package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import bean.Task;
import bean.User;

public class UserModel {
	private String jdbcURL;
	private String jdbcUsername;
	private String jdbcPassword;
	private Connection conn;
	
	public UserModel(String jdbcURL, String jdbcUsername, String jdbcPassword) {
		this.jdbcURL = jdbcURL;
		this.jdbcUsername = jdbcUsername;
		this.jdbcPassword = jdbcPassword;
	}
	
	public List<User> search(String keyword) {
		List<User> result = new LinkedList<User>();
		PreparedStatement pst = null;
		try {
			conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
			String sqlCommand = "Select * from tbluser where permission <> '1' ";

			if ((keyword != null && !"".equals(keyword))) {
				sqlCommand += "AND " + "username LIKE ? " + "OR depcode like ? " + "ORDER BY permission";
			} else {
				sqlCommand += "ORDER BY permission";
			}
			pst = conn.prepareStatement(sqlCommand);
			if ((keyword != null && !"".equals(keyword))) {
				pst.setString(1, "%" + keyword.toLowerCase() + "%");
				pst.setString(2, "%" + keyword.toLowerCase() + "%");
			}
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Integer id = rs.getInt("id");
				String username = rs.getString("username");
				String password = rs.getString("password");
				String fullname = rs.getString("fullname");
				String permission = rs.getString("permission");
				String depcode = rs.getString("depcode");
				User user = new User(id, username, password, fullname, permission, depcode);
				result.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnect(conn);
			DBConnection.closeStatement(pst);
		}
		return result;
	}
	
	public boolean insertUser(User user) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sqlCommand = "INSERT INTO tbluser(username, password, fullname, permission, depcode) values(?, ?, ?, ?, ?)";
		PreparedStatement pst = null;
		int result = 0;
		try {
			pst = conn.prepareStatement(sqlCommand);
			pst.setString(1, user.getUsername());
			pst.setString(2, user.getPassword());
			pst.setString(3, user.getFullname());
			pst.setString(4, user.getPermission());
			pst.setString(5, user.getDepcode());
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
	
	public boolean updateUser(User user) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sql = "UPDATE tbluser SET username = ?, password = ?, fullname = ?, permission = ?, depcode = ?";
		sql += " WHERE id = ?";
		PreparedStatement pst = null;
		int rowUpdated = 0;
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, user.getUsername());
			pst.setString(2, user.getPassword());
			pst.setString(3, user.getFullname());
			pst.setString(4, user.getPermission());
			pst.setString(5, user.getDepcode());
			pst.setInt(6, user.getId());
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
	
	public boolean deleteUser(int id) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sql = "DELETE FROM tbluser WHERE id = ?";
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
	
	public User selectUser(int id) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sql = "SELECT * FROM tbluser WHERE id = ?";
		User user = new User();
		try {
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setInt(1, id);
			ResultSet result = pst.executeQuery();
			if (result.next()) {
				String username = result.getString("username");
				String password = result.getString("password");
				String fullname = result.getString("fullname");
				String permission = result.getString("permission");
				String depcode = result.getString("depcode");
				user = new User(id, username, password, fullname, permission, depcode);
				DBConnection.closeConnect(conn);
				DBConnection.closePreparedStatement(pst);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}
	
	public User selectUserByUsername(String username) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		String sql = "SELECT * FROM tbluser WHERE username = ?";
		User user = new User();
		try {
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, username);
			ResultSet result = pst.executeQuery();
			if (result.next()) {
				Integer id = result.getInt("id");
				String password = result.getString("password");
				String fullname = result.getString("fullname");
				String permission = result.getString("permission");
				String depcode = result.getString("depcode");
				user = new User(id, username, password, fullname, permission, depcode);
				DBConnection.closeConnect(conn);
				DBConnection.closePreparedStatement(pst);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}
}
