package com.manuel.gym_api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manuel.gym_api.dto.LoginDTO;
import com.manuel.gym_api.dto.TokenDTO;
import com.manuel.gym_api.dto.UserDTO;
import com.manuel.gym_api.dto.UserRegistrationDTO;
import com.manuel.gym_api.mapper.UserMapper;
import com.manuel.gym_api.model.User;
import com.manuel.gym_api.security.TokenService;
import com.manuel.gym_api.security.UserPrincipal;
import com.manuel.gym_api.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final TokenService tokenService;
	private final UserService userService;
	private final UserMapper userMapper;
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	public AuthController(AuthenticationManager authenticationManager, TokenService tokenService,
			UserService userService, UserMapper userMapper) {
		this.authenticationManager = authenticationManager;
		this.tokenService = tokenService;
		this.userService = userService;
		this.userMapper = userMapper;
	}

	@PostMapping("/login")
	public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
		try {
			UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
					loginDTO.getUsername().trim(), loginDTO.getPassword());

			Authentication auth = this.authenticationManager.authenticate(usernamePassword);

			UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
			var token = tokenService.generateToken(principal.getUser());

			return ResponseEntity.ok(new TokenDTO(token));
		} catch (DataAccessException e) {
			log.error("Database error during login for user: {}", loginDTO.getUsername(), e);
			throw e;
		} catch (Exception e) {
			log.error("Unexpected error during login for user: {}", loginDTO.getUsername(), e);
			throw e;
		}
	}

	@PostMapping("/register")
	public ResponseEntity<TokenDTO> register(@RequestBody @Valid UserRegistrationDTO registrationDTO) {
		try {
			User user = userService.registerUser(registrationDTO);
			var token = tokenService.generateToken(user);
			return ResponseEntity.ok(new TokenDTO(token));
		} catch (Exception e) {
			log.error("Error during registration for user: {}", registrationDTO.getUsername(), e);
			throw e;
		}
	}

	@GetMapping("/me")
	public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
		try {
			if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal principal) {
				User user = principal.getUser();
				return ResponseEntity.ok(userMapper.toDTO(user));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
		} catch (Exception e) {
			log.error("Error fetching current user", e);
			throw e;
		}
	}
}
