package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import bean.LoginBean;

public class LoginModel {
	private String jdbcURL;
	private String jdbcUsername;
	private String jdbcPassword;
	private Connection conn;
	
	public LoginModel(String jdbcURL, String jdbcUsername, String jdbcPassword) {
		this.jdbcURL = jdbcURL;
		this.jdbcUsername = jdbcUsername;
		this.jdbcPassword = jdbcPassword;
	}
	
	public boolean checkAccount(LoginBean loginBean) {
		conn = DBConnection.create(jdbcURL, jdbcUsername, jdbcPassword);
		PreparedStatement pst = null;
		boolean status = false;
		try {
			String sql = "Select * from tbluser WHERE username = ? AND password = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, loginBean.getUsername());
			pst.setString(2, loginBean.getPassword());
			ResultSet rs = pst.executeQuery();
			status = rs.next();	
		} catch (Exception ex) {
			System.err.print("Exception: ");
			System.err.println(ex.getMessage());
		} finally {
			DBConnection.closeConnect(conn);
			DBConnection.closeStatement(pst);
		}
		return status;
	}
	
	public void logout() {
		Session sess = Sessions.getCurrent();
		sess.removeAttribute("sessionCurr");
	}
}
