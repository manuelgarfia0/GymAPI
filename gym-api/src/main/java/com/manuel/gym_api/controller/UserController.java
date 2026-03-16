package com.manuel.gym_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manuel.gym_api.dto.UserDTO;
import com.manuel.gym_api.model.User;
import com.manuel.gym_api.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
		UserDTO user = userService.getUserById(id);
		return ResponseEntity.ok(user);
	}

	@PutMapping("/{id}")
	public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO updateDTO,
			Authentication authentication) {
		User currentUser = (User) authentication.getPrincipal();
		if (!currentUser.getId().equals(id)) {
			return ResponseEntity.status(403).build();
		}
		UserDTO updated = userService.updateUser(id, updateDTO);
		return ResponseEntity.ok(updated);
	}
}