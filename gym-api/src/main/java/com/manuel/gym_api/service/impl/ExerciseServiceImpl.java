package com.manuel.gym_api.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	public ExerciseServiceImpl(ExerciseRepository repository, MuscleRepository muscleRepository,
			ExerciseMapper exerciseMapper) {
		this.repository = repository;
		this.muscleRepository = muscleRepository;
		this.exerciseMapper = exerciseMapper;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ExerciseDTO> getAllExercises(Pageable pageable) {
		return repository.findAll(pageable).map(exerciseMapper::toDTO);
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
		Muscle primary = muscleRepository.findById(dto.getPrimaryMuscleId()).orElseThrow(
				() -> new ResourceNotFoundException("Primary muscle not found with ID: " + dto.getPrimaryMuscleId()));

		Set<Muscle> secondarySet = new HashSet<>();
		if (dto.getSecondaryMuscleIds() != null && !dto.getSecondaryMuscleIds().isEmpty()) {
			for (Long secId : dto.getSecondaryMuscleIds()) {
				muscleRepository.findById(secId).ifPresent(secondarySet::add);
			}
		}

		Exercise exercise = exerciseMapper.toEntity(dto, primary, secondarySet, null);

		Exercise saved = repository.save(exercise);

		return exerciseMapper.toDTO(saved);
	}

	@Override
	public List<ExerciseDTO> searchExercises(String query) {
		return repository.findByNameContainingIgnoreCase(query).stream().map(exerciseMapper::toDTO).toList();
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