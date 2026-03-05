package com.manuel.gym_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.manuel.gym_api.dto.ExerciseDTO;
import com.manuel.gym_api.service.ExerciseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/exercises")
public class ExerciseController {

	private final ExerciseService service;

	public ExerciseController(ExerciseService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<ExerciseDTO>> getExercises() {
		return ResponseEntity.ok(service.getAllExercises());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ExerciseDTO> getExercise(@PathVariable Long id) {
		return ResponseEntity.ok(service.getExerciseById(id));
	}

	@PostMapping
	public ResponseEntity<ExerciseDTO> createExercise(@RequestBody @Valid ExerciseDTO dto) {
		ExerciseDTO created = service.saveExercise(dto);
		return ResponseEntity.ok(created);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
		service.deleteExercise(id);
		return ResponseEntity.noContent().build();
	}
}