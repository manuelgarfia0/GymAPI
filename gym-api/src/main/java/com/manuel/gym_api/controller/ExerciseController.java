package com.manuel.gym_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.manuel.gym_api.dto.ExerciseDTO;
import com.manuel.gym_api.service.ExerciseService;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

	private final ExerciseService exerciseService;

	public ExerciseController(ExerciseService exerciseService) {
		this.exerciseService = exerciseService;
	}

	@GetMapping
	public ResponseEntity<List<ExerciseDTO>> getExercises() {
		return ResponseEntity.ok(exerciseService.getAllExercises());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ExerciseDTO> getExerciseById(@PathVariable Long id) {
		return ResponseEntity.ok(exerciseService.getExerciseById(id));
	}

	@GetMapping(params = "search")
	public ResponseEntity<List<ExerciseDTO>> searchExercises(@RequestParam String search) {
		// Implementar búsqueda en ExerciseService
		return ResponseEntity.ok(exerciseService.searchExercises(search));
	}
}