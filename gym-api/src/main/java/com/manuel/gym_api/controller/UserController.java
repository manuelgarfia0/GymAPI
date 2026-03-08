package com.manuel.gym_api.controller;

import com.manuel.gym_api.dto.UserDTO;
import com.manuel.gym_api.dto.UserRegistrationDTO;
import com.manuel.gym_api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/register")
	public ResponseEntity<UserDTO> registerUser(@RequestBody UserRegistrationDTO registrationDTO) {
		UserDTO createdUser = userService.registerUser(registrationDTO);
		return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
		UserDTO user = userService.getUserById(id);
		return ResponseEntity.ok(user);
	}
}