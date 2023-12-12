package com.example.Course.Registration.payload.response;

import java.util.List;

public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private String id;
	private String name;
	// private String email;
	private List<String> roles;
	private List<String> courses;

	public JwtResponse(String accessToken, String id, String name, List<String> roles,List<String> courses) {
		this.token = accessToken;
		this.id = id;
		this.name = name;
		// this.email = email;
		this.roles = roles;
		this.courses = courses;
	}

	public String getAccessToken() {
		return token;
	}

	public List<String> getCourses() {
		return courses;
	}

	public void setAccessToken(String accessToken) {
		this.token = accessToken;
	}

	public String getTokenType() {
		return type;
	}

	public void setTokenType(String tokenType) {
		this.type = tokenType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	// public String getEmail() {
	// 	return email;
	// }

	// public void setEmail(String email) {
	// 	this.email = email;
	// }

	public String getName() {
		return name;
	}

	public void setUsername(String username) {
		this.name = username;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRole(List<String> roles) {
		this.roles = roles;
	}
	
}
