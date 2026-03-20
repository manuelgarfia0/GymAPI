package com.manuel.gym_api.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
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
import com.manuel.gym_api.dto.UserRegistrationDTO;
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
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	// AuthService eliminado: ya no se necesita para cargar el usuario tras
	// registro,
	// porque registerUser() devuelve el User directamente.
	public AuthController(AuthenticationManager authenticationManager, TokenService tokenService,
			UserService userService) {
		this.authenticationManager = authenticationManager;
		this.tokenService = tokenService;
		this.userService = userService;
	}

	@PostMapping("/login")
	public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
		try {
			UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
					loginDTO.getUsername().trim(), loginDTO.getPassword());

			Authentication auth = this.authenticationManager.authenticate(usernamePassword);

			// CORRECCIÓN: auth.getPrincipal() devuelve UserPrincipal (no User)
			// porque AuthService ahora envuelve User en UserPrincipal.
			// Extraemos el User del principal para generar el token.
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
			// registerUser() devuelve directamente el User — sin segunda consulta a la BD.
			User user = userService.registerUser(registrationDTO);
			var token = tokenService.generateToken(user);
			return ResponseEntity.ok(new TokenDTO(token));
		} catch (Exception e) {
			log.error("Error during registration for user: {}", registrationDTO.getUsername(), e);
			throw e;
		}
	}

	@GetMapping("/me")
	public ResponseEntity<Map<String, Object>> getCurrentUser(Authentication authentication) {
		try {
			// getPrincipal() devuelve UserPrincipal — usamos instanceof pattern matching
			// (Java 16+)
			if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal principal) {
				User user = principal.getUser();

				Map<String, Object> response = new HashMap<>();
				response.put("id", user.getId());
				response.put("username", user.getUsername());
				response.put("email", user.getEmail());
				response.put("isPremium", user.isPremium());
				response.put("publicProfile", user.isPublicProfile());
				response.put("languagePreference", user.getLanguagePreference());
				response.put("createdAt", user.getCreatedAt() != null ? user.getCreatedAt().toString() : null);

				return ResponseEntity.ok(response);
			} else {
				return ResponseEntity.status(401).build();
			}
		} catch (Exception e) {
			log.error("Error fetching current user", e);
			throw e;
		}
	}
}