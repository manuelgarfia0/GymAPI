package com.manuel.gym_api.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ExerciseController {

	@GetMapping("/exercises")
	public ResponseEntity<Map<String, Object>> getExercises() {
		try {
			Map<String, Object> response = new HashMap<>();
			response.put("status", "success");
			response.put("message", "Exercises retrieved successfully");

			// Datos de ejemplo - reemplazar con servicio real después
			List<Map<String, Object>> exercises = Arrays.asList(
					createExercise(1L, "Push-ups", "Chest", "Bodyweight exercise for chest and arms"),
					createExercise(2L, "Squats", "Legs", "Lower body compound exercise"),
					createExercise(3L, "Pull-ups", "Back", "Upper body pulling exercise"),
					createExercise(4L, "Bench Press", "Chest", "Barbell chest exercise"),
					createExercise(5L, "Deadlift", "Full Body", "Compound lifting exercise"));

			response.put("exercises", exercises);
			response.put("count", exercises.size());

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			// Log del error para debugging
			System.err.println("Error en getExercises: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	private Map<String, Object> createExercise(Long id, String name, String category, String description) {
		Map<String, Object> exercise = new HashMap<>();
		exercise.put("id", id);
		exercise.put("name", name);
		exercise.put("category", category);
		exercise.put("description", description);
		return exercise;
	}
}