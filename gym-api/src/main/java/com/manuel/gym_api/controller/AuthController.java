package com.manuel.gym_api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manuel.gym_api.dto.LoginDTO;
import com.manuel.gym_api.dto.TokenDTO;
import com.manuel.gym_api.dto.UserRegistrationDTO;
import com.manuel.gym_api.model.User;
import com.manuel.gym_api.security.AuthService;
import com.manuel.gym_api.security.TokenService;
import com.manuel.gym_api.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final TokenService tokenService;
	private final UserService userService;
	private final AuthService authService;

	public AuthController(AuthenticationManager authenticationManager, TokenService tokenService,
			UserService userService, AuthService authService) {
		this.authenticationManager = authenticationManager;
		this.tokenService = tokenService;
		this.userService = userService;
		this.authService = authService;
	}

	@PostMapping("/login")
	public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
		try {
			// Log de intento de login para debugging
			System.out.println("Intento de login para usuario: " + loginDTO.getUsername());

			// Validación adicional de entrada
			if (loginDTO.getUsername() == null || loginDTO.getUsername().trim().isEmpty()) {
				System.err.println("Error: Username vacío o nulo");
				throw new IllegalArgumentException("Username cannot be empty");
			}
			if (loginDTO.getPassword() == null || loginDTO.getPassword().trim().isEmpty()) {
				System.err.println("Error: Password vacío o nulo");
				throw new IllegalArgumentException("Password cannot be empty");
			}

			// Creamos un token interno de Spring con usuario y contraseña
			UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
					loginDTO.getUsername().trim(), loginDTO.getPassword());

			System.out.println("Intentando autenticar usuario: " + loginDTO.getUsername().trim());

			// El AuthenticationManager irá automáticamente al AuthService que creamos a
			// buscar al usuario y usará el PasswordEncoder para comprobar que la contraseña
			// coincide.
			Authentication auth = this.authenticationManager.authenticate(usernamePassword);

			System.out.println("Autenticación exitosa para usuario: " + loginDTO.getUsername().trim());

			// Si todo fue bien, generamos nuestro JWT real
			var token = tokenService.generateToken((User) auth.getPrincipal());

			System.out.println("Token JWT generado exitosamente para usuario: " + loginDTO.getUsername().trim());

			return ResponseEntity.ok(new TokenDTO(token));
		} catch (BadCredentialsException e) {
			// Error específico de credenciales inválidas
			System.err.println("Credenciales inválidas para usuario: " + loginDTO.getUsername());
			throw e; // Se maneja en GlobalExceptionHandler
		} catch (UsernameNotFoundException e) {
			// Error específico de usuario no encontrado
			System.err.println("Usuario no encontrado: " + loginDTO.getUsername());
			throw e; // Se maneja en GlobalExceptionHandler
		} catch (DataAccessException e) {
			// Error específico de base de datos
			System.err.println("Error de base de datos durante login: " + e.getMessage());
			e.printStackTrace();
			throw e; // Se maneja en GlobalExceptionHandler
		} catch (Exception e) {
			// Log detallado del error para debugging
			System.err.println("Error inesperado en login para usuario: " + loginDTO.getUsername());
			System.err.println("Tipo de error: " + e.getClass().getSimpleName());
			System.err.println("Mensaje de error: " + e.getMessage());
			e.printStackTrace();
			throw e; // Se maneja en GlobalExceptionHandler
		}
	}

	@PostMapping("/register")
	public ResponseEntity<TokenDTO> register(@RequestBody @Valid UserRegistrationDTO registrationDTO) {
		try {
			userService.registerUser(registrationDTO);

			// Cargamos el User real para poder generar el token
			User user = (User) authService.loadUserByUsername(registrationDTO.getUsername());
			var token = tokenService.generateToken(user);

			return ResponseEntity.ok(new TokenDTO(token));
		} catch (Exception e) {
			System.err.println("Error en register: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	@GetMapping("/me")
	public ResponseEntity<Map<String, Object>> getCurrentUser(Authentication authentication) {
		try {
			// Obtener el usuario autenticado desde el contexto de seguridad
			if (authentication != null && authentication.getPrincipal() instanceof User) {
				User user = (User) authentication.getPrincipal();

				// Crear respuesta que coincida con lo que espera Flutter
				Map<String, Object> response = new HashMap<>();
				response.put("id", user.getId());
				response.put("username", user.getUsername());
				response.put("email", user.getEmail());
				response.put("isPremium", user.isPremium()); // Usar el método correcto
				response.put("publicProfile", user.isPublicProfile()); // Usar el método correcto
				response.put("languagePreference", user.getLanguagePreference());
				response.put("createdAt", user.getCreatedAt() != null ? user.getCreatedAt().toString() : null);

				return ResponseEntity.ok(response);
			} else {
				// Si no hay usuario autenticado, devolver error 401
				return ResponseEntity.status(401).build();
			}
		} catch (Exception e) {
			// Log del error para debugging
			System.err.println("Error en getCurrentUser: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	// Endpoints temporales para testing con GET (los diagnósticos usan GET)
	@GetMapping("/login")
	public ResponseEntity<Map<String, String>> loginInfo() {
		Map<String, String> response = new HashMap<>();
		response.put("status", "info");
		response.put("message", "Use POST method for login");
		response.put("endpoint", "/api/auth/login");
		response.put("method", "POST");
		return ResponseEntity.ok(response);
	}

	@GetMapping("/register")
	public ResponseEntity<Map<String, String>> registerInfo() {
		Map<String, String> response = new HashMap<>();
		response.put("status", "info");
		response.put("message", "Use POST method for registration");
		response.put("endpoint", "/api/auth/register");
		response.put("method", "POST");
		return ResponseEntity.ok(response);
	}
}