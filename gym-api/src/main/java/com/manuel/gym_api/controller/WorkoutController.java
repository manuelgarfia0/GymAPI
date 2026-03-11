package com.manuel.gym_api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<WorkoutDTO> createWorkout(@RequestBody @Valid WorkoutDTO workoutDTO) {
		WorkoutDTO createdWorkout = workoutService.createWorkout(workoutDTO);
		return new ResponseEntity<>(createdWorkout, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<WorkoutDTO> getWorkoutById(@PathVariable Long id) {
		return ResponseEntity.ok(workoutService.getWorkoutById(id));
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<WorkoutDTO>> getWorkoutsByUserId(@PathVariable Long userId) {
		return ResponseEntity.ok(workoutService.getWorkoutsByUserId(userId));
	}

	@PutMapping("/{id}")
	public ResponseEntity<WorkoutDTO> updateWorkout(@PathVariable Long id, @RequestBody @Valid WorkoutDTO workoutDTO) {
		return ResponseEntity.ok(workoutService.updateWorkout(id, workoutDTO));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteWorkout(@PathVariable Long id) {
		workoutService.deleteWorkout(id);
		return ResponseEntity.noContent().build();
	}

	// Para que Flutter pueda buscar por userId con query param
	@GetMapping
	public ResponseEntity<List<WorkoutDTO>> getWorkoutsByUserIdParam(@RequestParam Long userId) {
		return ResponseEntity.ok(workoutService.getWorkoutsByUserId(userId));
	}

	// Flutter llama a POST /api/workouts/save
	@PostMapping("/save")
	public ResponseEntity<WorkoutDTO> saveWorkout(@RequestBody @Valid WorkoutDTO workoutDTO) {
		WorkoutDTO saved = workoutService.createWorkout(workoutDTO);
		return ResponseEntity.ok(saved);
	}

	// Flutter llama a GET /api/workouts/active?userId=1
	@GetMapping("/active")
	public ResponseEntity<WorkoutDTO> getActiveWorkout(@RequestParam Long userId) {
		// implementar lógica o devolver 404 si no hay activo
		return ResponseEntity.notFound().build();
	}

	// Flutter llama a PATCH /api/workouts/{id}/end
	@PatchMapping("/{id}/end")
	public ResponseEntity<WorkoutDTO> endWorkout(@PathVariable Long id, @RequestBody Map<String, String> body) {
		return ResponseEntity.ok(workoutService.getWorkoutById(id));
	}
}