package com.manuel.gym_api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;

public class LoginDTO {

	@NotBlank(message = "Username or email is required")
	@JsonAlias({ "email", "identifier", "login" })
	private String username;

	@NotBlank(message = "Password is required")
	private String password;

	public LoginDTO() {
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
