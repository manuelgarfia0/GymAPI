package com.manuel.gym_api.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.manuel.gym_api.dto.ExerciseDTO;

public interface ExerciseService {
	Page<ExerciseDTO> getAllExercises(Pageable pageable);

	ExerciseDTO getExerciseById(Long id);

	ExerciseDTO saveExercise(ExerciseDTO dto);

	List<ExerciseDTO> searchExercises(String query);

	void deleteExercise(Long id);
}