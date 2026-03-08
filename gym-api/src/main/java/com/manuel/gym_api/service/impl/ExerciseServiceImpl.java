package com.manuel.gym_api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.manuel.gym_api.dto.ExerciseDTO;
import com.manuel.gym_api.exception.ResourceNotFoundException;
import com.manuel.gym_api.mapper.ExerciseMapper;
import com.manuel.gym_api.model.Exercise;
import com.manuel.gym_api.model.Muscle;
import com.manuel.gym_api.repository.ExerciseRepository;
import com.manuel.gym_api.repository.MuscleRepository;
import com.manuel.gym_api.service.ExerciseService;

@Service
public class ExerciseServiceImpl implements ExerciseService {

	private final ExerciseRepository repository;
	private final MuscleRepository muscleRepository;
	private final ExerciseMapper exerciseMapper;

	public ExerciseServiceImpl(ExerciseRepository repository, MuscleRepository muscleRepository,
			ExerciseMapper exerciseMapper) {
		this.repository = repository;
		this.muscleRepository = muscleRepository;
		this.exerciseMapper = exerciseMapper;
	}

	@Override
	public List<ExerciseDTO> getAllExercises() {
		return repository.findAll().stream().map(exerciseMapper::toDTO).toList();
	}

	@Override
	public ExerciseDTO getExerciseById(Long id) {
		Exercise exercise = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Ejercicio no encontrado con ID: " + id));

		return exerciseMapper.toDTO(exercise);
	}

	@Override
	public ExerciseDTO saveExercise(ExerciseDTO dto) {
		Muscle primary = muscleRepository.findById(dto.getPrimaryMuscleId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Músculo principal no encontrado con ID: " + dto.getPrimaryMuscleId()));

		Muscle secondary = null;
		if (dto.getSecondaryMuscleId() != null) {
			secondary = muscleRepository.findById(dto.getSecondaryMuscleId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Músculo secundario no encontrado con ID: " + dto.getSecondaryMuscleId()));
		}

		Exercise exercise = exerciseMapper.toEntity(dto, primary, secondary);

		Exercise saved = repository.save(exercise);

		return exerciseMapper.toDTO(saved);
	}

	@Override
	public void deleteExercise(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("No se puede borrar, el ejercicio no existe con ID: " + id);
		}
		repository.deleteById(id);
	}
}