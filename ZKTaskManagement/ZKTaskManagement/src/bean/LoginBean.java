package bean;

public class LoginBean {
	private String username;
	private String password;
	private String name;

	public LoginBean() {
		
	}
	
	public LoginBean(String username, String name) {
		this.username = username;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName() {
		this.name = name;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
