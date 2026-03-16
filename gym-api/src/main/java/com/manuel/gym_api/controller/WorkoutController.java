package com.manuel.gym_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.manuel.gym_api.dto.WorkoutDTO;
import com.manuel.gym_api.model.User;
import com.manuel.gym_api.service.WorkoutService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

	private final WorkoutService workoutService;

	public WorkoutController(WorkoutService workoutService) {
		this.workoutService = workoutService;
	}

	@PostMapping
	public ResponseEntity<WorkoutDTO> createWorkout(@RequestBody @Valid WorkoutDTO workoutDTO,
			Authentication authentication) {
		User currentUser = (User) authentication.getPrincipal();
		workoutDTO.setUserId(currentUser.getId());
		WorkoutDTO createdWorkout = workoutService.createWorkout(workoutDTO);
		return new ResponseEntity<>(createdWorkout, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<WorkoutDTO> getWorkoutById(@PathVariable Long id) {
		return ResponseEntity.ok(workoutService.getWorkoutById(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<WorkoutDTO> updateWorkout(@PathVariable Long id, @RequestBody @Valid WorkoutDTO workoutDTO,
			Authentication authentication) {
		User currentUser = (User) authentication.getPrincipal();
		WorkoutDTO workout = workoutService.getWorkoutById(id);
		if (!workout.getUserId().equals(currentUser.getId())) {
			return ResponseEntity.status(403).build();
		}
		return ResponseEntity.ok(workoutService.updateWorkout(id, workoutDTO));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteWorkout(@PathVariable Long id, Authentication authentication) {
		User currentUser = (User) authentication.getPrincipal();
		WorkoutDTO workout = workoutService.getWorkoutById(id);
		if (!workout.getUserId().equals(currentUser.getId())) {
			return ResponseEntity.status(403).build();
		}
		workoutService.deleteWorkout(id);
		return ResponseEntity.noContent().build();
	}

	// Para que Flutter pueda buscar por userId con query param
	@GetMapping
	public ResponseEntity<List<WorkoutDTO>> getWorkoutsByUserIdParam(@RequestParam Long userId,
			Authentication authentication) {
		User currentUser = (User) authentication.getPrincipal();
		if (!currentUser.getId().equals(userId)) {
			return ResponseEntity.status(403).build();
		}
		return ResponseEntity.ok(workoutService.getWorkoutsByUserId(userId));
	}

	// Flutter llama a PATCH /api/workouts/{id}/end
	@PatchMapping("/{id}/end")
	public ResponseEntity<WorkoutDTO> endWorkout(@PathVariable Long id, Authentication authentication) {
		User currentUser = (User) authentication.getPrincipal();
		WorkoutDTO workout = workoutService.getWorkoutById(id);
		if (!workout.getUserId().equals(currentUser.getId())) {
			return ResponseEntity.status(403).build();
		}
		return ResponseEntity.ok(workoutService.endWorkout(id));
	}
}