package bean;

public class User {
	private Integer id;
	private String username;
	private String password;
	private String fullname;
	private String permission;
	private String depcode;
	
	public User() {
		
	}

	public User(Integer id, String username, String password, String fullname, String permission, String depcode) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.fullname = fullname;
		this.permission = permission;
		this.depcode = depcode;
	}

	
	public String getDepcode() {
		return depcode;
	}

	public void setDepcode(String depcode) {
		this.depcode = depcode;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}
	

}
