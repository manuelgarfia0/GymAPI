package com.manuel.gym_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manuel.gym_api.dto.LoginDTO;
import com.manuel.gym_api.dto.TokenDTO;
import com.manuel.gym_api.model.User;
import com.manuel.gym_api.security.TokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final TokenService tokenService;

	public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
		this.authenticationManager = authenticationManager;
		this.tokenService = tokenService;
	}

	@PostMapping("/login")
	public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
		// Creamos un token interno de Spring con usuario y contraseña
		UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
				loginDTO.getUsername(), loginDTO.getPassword());

		// El AuthenticationManager irá automáticamente al AuthService que creamos a
		// buscar al usuario
		// y usará el PasswordEncoder para comprobar que la contraseña coincide.
		Authentication auth = this.authenticationManager.authenticate(usernamePassword);

		// Si todo fue bien, generamos nuestro JWT real
		var token = tokenService.generateToken((User) auth.getPrincipal());

		return ResponseEntity.ok(new TokenDTO(token));
	}
}