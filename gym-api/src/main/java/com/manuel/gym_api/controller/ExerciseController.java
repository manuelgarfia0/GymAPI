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
import com.manuel.gym_api.service.impl.ExerciseImportServiceImpl;
import com.manuel.gym_api.service.impl.ExerciseServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/exercises")
public class ExerciseController {

	private final ExerciseServiceImpl service;
	private final ExerciseImportServiceImpl importService;

	public ExerciseController(ExerciseServiceImpl service, ExerciseImportServiceImpl importService) {
		this.service = service;
		this.importService = importService;
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

	@PostMapping("/import")
	public void importExercises() {
		importService.importExercises();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
		service.deleteExercise(id);
		return ResponseEntity.noContent().build();
	}
}