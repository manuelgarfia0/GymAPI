package com.manuel.gym_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.manuel.gym_api.dto.ExerciseDTO;
import com.manuel.gym_api.mapper.ExerciseMapper;
import com.manuel.gym_api.model.Exercise;
import com.manuel.gym_api.repository.ExerciseRepository;
import com.manuel.gym_api.repository.MuscleRepository;

@Service
public class ExerciseService {

	private final ExerciseRepository repository;
	private final MuscleRepository muscleRepository;

	public ExerciseService(ExerciseRepository repository, MuscleRepository muscleRepository) {
		this.repository = repository;
		this.muscleRepository = muscleRepository;
	}

	public List<ExerciseDTO> getAllExercises() {
		return repository.findAll().stream().map(ExerciseMapper::toDTO).toList();
	}

	public ExerciseDTO getExerciseById(Long id) {
		Exercise exercise = repository.findById(id).orElseThrow(() -> new RuntimeException("Exercise not found"));

		return ExerciseMapper.toDTO(exercise);
	}

	public ExerciseDTO saveExercise(ExerciseDTO dto) {

		Exercise exercise = ExerciseMapper.toEntity(dto, muscleRepository);

		Exercise saved = repository.save(exercise);

		return ExerciseMapper.toDTO(saved);
	}

	public void deleteExercise(Long id) {

		if (!repository.existsById(id)) {
			throw new RuntimeException("Exercise not found");
		}

		repository.deleteById(id);
	}
}