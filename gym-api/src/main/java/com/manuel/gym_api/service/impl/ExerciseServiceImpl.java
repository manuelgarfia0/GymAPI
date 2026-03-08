package com.manuel.gym_api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.manuel.gym_api.dto.ExerciseDTO;
import com.manuel.gym_api.exception.ResourceNotFoundException;
import com.manuel.gym_api.mapper.ExerciseMapper;
import com.manuel.gym_api.model.Exercise;
import com.manuel.gym_api.repository.ExerciseRepository;
import com.manuel.gym_api.repository.MuscleRepository;
import com.manuel.gym_api.service.ExerciseService;

@Service
public class ExerciseServiceImpl implements ExerciseService {

	private final ExerciseRepository repository;
	private final MuscleRepository muscleRepository;

	public ExerciseServiceImpl(ExerciseRepository repository, MuscleRepository muscleRepository) {
		this.repository = repository;
		this.muscleRepository = muscleRepository;
	}

	@Override
	public List<ExerciseDTO> getAllExercises() {
		return repository.findAll().stream().map(ExerciseMapper::toDTO).toList();
	}

	@Override
	public ExerciseDTO getExerciseById(Long id) {
		Exercise exercise = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Ejercicio no encontrado con id: " + id));

		return ExerciseMapper.toDTO(exercise);
	}

	@Override
	public ExerciseDTO saveExercise(ExerciseDTO dto) {
		Exercise exercise = ExerciseMapper.toEntity(dto, muscleRepository);
		Exercise saved = repository.save(exercise);
		return ExerciseMapper.toDTO(saved);
	}

	@Override
	public void deleteExercise(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("No se puede borrar, ejercicio no encontrado con id: " + id);
		}
		repository.deleteById(id);
	}
}