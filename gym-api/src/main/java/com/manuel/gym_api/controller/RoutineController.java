package com.manuel.gym_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manuel.gym_api.dto.RoutineDTO;
import com.manuel.gym_api.model.User;
import com.manuel.gym_api.security.UserPrincipal;
import com.manuel.gym_api.service.ProfileAccessService;
import com.manuel.gym_api.service.RoutineService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/routines")
public class RoutineController {

	private final RoutineService routineService;
	private final ProfileAccessService profileAccessService;

	public RoutineController(RoutineService routineService, ProfileAccessService profileAccessService) {
		this.routineService = routineService;
		this.profileAccessService = profileAccessService;
	}

	@PostMapping
	public ResponseEntity<RoutineDTO> createRoutine(@RequestBody @Valid RoutineDTO routineDTO,
			Authentication authentication) {
		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
		routineDTO.setUserId(principal.getId());
		RoutineDTO createdRoutine = routineService.createRoutine(routineDTO);
		return new ResponseEntity<>(createdRoutine, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<RoutineDTO> getRoutineById(@PathVariable Long id, Authentication authentication) {
		User currentUser = (User) authentication.getPrincipal();
		RoutineDTO routine = routineService.getRoutineById(id);
		if (!profileAccessService.canViewRoutines(currentUser.getId(), routine.getUserId())) {
			return ResponseEntity.status(403).build();
		}
		return ResponseEntity.ok(routine);
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<RoutineDTO>> getRoutinesByUserId(@PathVariable Long userId,
			Authentication authentication) {
		User currentUser = (User) authentication.getPrincipal();
		if (!profileAccessService.canViewRoutines(currentUser.getId(), userId)) {
			return ResponseEntity.status(403).build();
		}
		return ResponseEntity.ok(routineService.getRoutinesByUserId(userId));
	}

	@PutMapping("/{id}")
	public ResponseEntity<RoutineDTO> updateRoutine(@PathVariable Long id, @RequestBody @Valid RoutineDTO routineDTO,
			Authentication authentication) {
		User currentUser = (User) authentication.getPrincipal();
		RoutineDTO routine = routineService.getRoutineById(id);
		if (!routine.getUserId().equals(currentUser.getId())) {
			return ResponseEntity.status(403).build();
		}
		return ResponseEntity.ok(routineService.updateRoutine(id, routineDTO));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteRoutine(@PathVariable Long id, Authentication authentication) {
		User currentUser = (User) authentication.getPrincipal();
		RoutineDTO routine = routineService.getRoutineById(id);
		if (!routine.getUserId().equals(currentUser.getId())) {
			return ResponseEntity.status(403).build();
		}
		routineService.deleteRoutine(id);
		return ResponseEntity.noContent().build();
	}
}