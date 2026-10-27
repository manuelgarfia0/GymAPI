package com.manuel.gym_api.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.PersistenceException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.NOT_FOUND.value());
		body.put("error", "Resource Not Found");

		log.warn("Resource not found: {}", ex.getMessage());

		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<Object> handleDuplicateResourceException(DuplicateResourceException ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.CONFLICT.value());
		body.put("error", "Duplicate Resource");

		log.warn("Duplicate resource: {}", ex.getMessage());

		return new ResponseEntity<>(body, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> fieldErrors = new HashMap<>();
		ex.getBindingResult().getFieldErrors()
				.forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));

		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", "Validation failed");
		body.put("status", HttpStatus.BAD_REQUEST.value());
		body.put("error", "Bad Request");
		body.put("fieldErrors", fieldErrors);

		log.warn("Validation error: {}", fieldErrors);

		return ResponseEntity.badRequest().body(body);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.BAD_REQUEST.value());
		body.put("error", "Bad Request");

		log.warn("Illegal argument: {}", ex.getMessage());

		return ResponseEntity.badRequest().body(body);
	}

	// Manejo específico de errores de autenticación
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<Map<String, Object>> handleBadCredentialsException(BadCredentialsException ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", "Invalid username or password");
		body.put("status", HttpStatus.UNAUTHORIZED.value());
		body.put("error", "Unauthorized");

		log.warn("Bad credentials: {}", ex.getMessage());

		return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
	}

	// Manejo específico de usuario no encontrado
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", "Invalid username or password");
		body.put("status", HttpStatus.UNAUTHORIZED.value());
		body.put("error", "Unauthorized");

		log.warn("Username not found: {}", ex.getMessage());

		return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
	}

	// Manejo específico de errores de base de datos
	@ExceptionHandler({ DataAccessException.class, PersistenceException.class })
	public ResponseEntity<Map<String, Object>> handleDatabaseException(Exception ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", "Database connection error. Please try again later.");
		body.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
		body.put("error", "Service Unavailable");

		log.error("Database error: {}", ex.getMessage(), ex);

		return new ResponseEntity<>(body, HttpStatus.SERVICE_UNAVAILABLE);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
		throw ex;
	}

	// Manejo de errores generales - ÚLTIMO RECURSO
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleGlobalExceptions(Exception ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		body.put("error", "Internal Server Error");

		log.error("Unexpected error: {}", ex.getMessage(), ex);

		if (ex.getMessage() != null) {
			if (ex.getMessage().toLowerCase().contains("connection")) {
				body.put("message", "Database connection failed. Please check server configuration.");
			} else if (ex.getMessage().toLowerCase().contains("jwt")
					|| ex.getMessage().toLowerCase().contains("token")) {
				body.put("message", "JWT token processing error. Please check authentication configuration.");
			} else {
				body.put("message", "An unexpected error occurred. Please contact support if the problem persists.");
			}
		} else {
			body.put("message", "An unexpected error occurred. Please contact support if the problem persists.");
		}

		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
