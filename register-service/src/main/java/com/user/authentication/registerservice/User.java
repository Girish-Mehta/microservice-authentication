package com.user.authentication.registerservice;

public class User {
	
	private Integer id;
	private String password;
	private String username;

	protected User() {}

	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
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
}
