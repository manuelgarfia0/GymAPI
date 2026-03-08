package com.manuel.gym_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manuel.gym_api.dto.ExerciseDTO;
import com.manuel.gym_api.service.ExerciseImportService;
import com.manuel.gym_api.service.ExerciseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/exercises")
public class ExerciseController {

	private final ExerciseService exerciseService;
	private final ExerciseImportService importService;

	public ExerciseController(ExerciseService exerciseService, ExerciseImportService importService) {
		this.exerciseService = exerciseService;
		this.importService = importService;
	}

	@GetMapping
	public ResponseEntity<List<ExerciseDTO>> getExercises() {
		return ResponseEntity.ok(exerciseService.getAllExercises());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ExerciseDTO> getExercise(@PathVariable Long id) {
		return ResponseEntity.ok(exerciseService.getExerciseById(id));
	}

	@PostMapping
	public ResponseEntity<ExerciseDTO> createExercise(@RequestBody @Valid ExerciseDTO dto) {
		return ResponseEntity.ok(exerciseService.saveExercise(dto));
	}

	@PostMapping("/import")
	public ResponseEntity<Void> importExercises() {
		importService.importExercises();
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
		exerciseService.deleteExercise(id);
		return ResponseEntity.noContent().build();
	}
}