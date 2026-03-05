package com.manuel.gym_api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manuel.gym_api.dto.ExerciseDTO;
import com.manuel.gym_api.service.ExerciseService;

@RestController
@RequestMapping("/exercises")
public class ExerciseController {

	private final ExerciseService service;

	public ExerciseController(ExerciseService service) {
		this.service = service;
	}

	@GetMapping
	public List<ExerciseDTO> getExercises() {
		return service.getAllExercises();
	}

	@PostMapping
	public ExerciseDTO createExercise(@RequestBody ExerciseDTO dto) {
		return service.saveExercise(dto);
	}

	@GetMapping("/{id}")
	public ExerciseDTO getExercise(@PathVariable Long id) {
		return service.getExerciseById(id);
	}

	@DeleteMapping("/{id}")
	public void deleteExercise(@PathVariable Long id) {
		service.deleteExercise(id);
	}

}
