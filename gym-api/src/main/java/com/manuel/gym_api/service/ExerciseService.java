package com.manuel.gym_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.manuel.gym_api.dto.ExerciseDTO;
import com.manuel.gym_api.mapper.ExerciseMapper;
import com.manuel.gym_api.model.Exercise;
import com.manuel.gym_api.repository.ExerciseRepository;

@Service
public class ExerciseService {
	private final ExerciseRepository repository;

	public ExerciseService(ExerciseRepository repository) {
		this.repository = repository;
	}

	public List<ExerciseDTO> getAllExercises() {
		return repository.findAll().stream().map(ExerciseMapper::toDTO).toList();
	}

	public ExerciseDTO getExerciseById(Long id) {
		Exercise exercise = repository.findById(id).orElseThrow(() -> new RuntimeException("Exercise not found"));

		return ExerciseMapper.toDTO(exercise);
	}

	public void deleteExercise(Long id) {
		repository.deleteById(id);
	}

	public ExerciseDTO saveExercise(ExerciseDTO dto) {

		Exercise exercise = ExerciseMapper.toEntity(dto);
		Exercise saved = repository.save(exercise);

		return ExerciseMapper.toDTO(saved);
	}
}
