package com.manuel.gym_api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	// Inyección de dependencias por constructor
	public ExerciseServiceImpl(ExerciseRepository repository, MuscleRepository muscleRepository,
			ExerciseMapper exerciseMapper) {
		this.repository = repository;
		this.muscleRepository = muscleRepository;
		this.exerciseMapper = exerciseMapper;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ExerciseDTO> getAllExercises() {
		return repository.findAll().stream().map(exerciseMapper::toDTO).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public ExerciseDTO getExerciseById(Long id) {
		Exercise exercise = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Exercise not found with ID: " + id));

		return exerciseMapper.toDTO(exercise);
	}

	@Override
	@Transactional
	public ExerciseDTO saveExercise(ExerciseDTO dto) {
		// Buscamos el músculo principal
		Muscle primary = muscleRepository.findById(dto.getPrimaryMuscleId()).orElseThrow(
				() -> new ResourceNotFoundException("Primary muscle not found with ID: " + dto.getPrimaryMuscleId()));

		// Buscamos el músculo secundario (si existe)
		Muscle secondary = null;
		if (dto.getSecondaryMuscleId() != null) {
			secondary = muscleRepository.findById(dto.getSecondaryMuscleId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Secondary muscle not found with ID: " + dto.getSecondaryMuscleId()));
		}

		// Transformamos a Entidad usando el Mapper
		Exercise exercise = exerciseMapper.toEntity(dto, primary, secondary);

		// Guardamos en base de datos
		Exercise saved = repository.save(exercise);

		// Devolvemos el DTO
		return exerciseMapper.toDTO(saved);
	}

	@Override
	@Transactional
	public void deleteExercise(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Cannot delete. Exercise not found with ID: " + id);
		}
		repository.deleteById(id);
	}
}