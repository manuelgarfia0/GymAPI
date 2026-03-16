package com.manuel.gym_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegistrationDTO {

	@NotBlank(message = "El nombre de usuario es obligatorio")
	@Size(min = 3, max = 50, message = "El usuario debe tener entre 3 y 50 caracteres")
	private String username;

	@NotBlank(message = "El email es obligatorio")
	@Email(message = "El formato del email no es válido")
	private String email;

	@NotBlank(message = "La contraseña es obligatoria")
	@Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
	private String password;

	// Getters y Setters
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}