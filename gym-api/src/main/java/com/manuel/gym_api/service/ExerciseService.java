package com.manuel.gym_api.service;

import java.util.List;
import com.manuel.gym_api.dto.ExerciseDTO;

public interface ExerciseService {
	List<ExerciseDTO> getAllExercises();

	ExerciseDTO getExerciseById(Long id);

	ExerciseDTO saveExercise(ExerciseDTO dto);

	void deleteExercise(Long id);
}