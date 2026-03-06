package com.manuel.gym_api.mapper;

import com.manuel.gym_api.dto.ExerciseDTO;
import com.manuel.gym_api.model.Exercise;
import com.manuel.gym_api.model.Muscle;
import com.manuel.gym_api.repository.MuscleRepository;

public class ExerciseMapper {

	public static ExerciseDTO toDTO(Exercise exercise) {

		Long secondaryMuscleId = null;

		if (exercise.getSecondaryMuscle() != null) {
			secondaryMuscleId = exercise.getSecondaryMuscle().getId();
		}

		return new ExerciseDTO(exercise.getId(), exercise.getName(), exercise.getPrimaryMuscle().getId(),
				secondaryMuscleId);
	}

	public static Exercise toEntity(ExerciseDTO dto, MuscleRepository muscleRepository) {

		Exercise exercise = new Exercise();

		exercise.setName(dto.getName());

		Muscle primary = muscleRepository.findById(dto.getPrimaryMuscleId())
				.orElseThrow(() -> new RuntimeException("Primary muscle not found"));

		exercise.setPrimaryMuscle(primary);

		if (dto.getSecondaryMuscleId() != null) {

			Muscle secondary = muscleRepository.findById(dto.getSecondaryMuscleId())
					.orElseThrow(() -> new RuntimeException("Secondary muscle not found"));

			exercise.setSecondaryMuscle(secondary);
		}

		return exercise;
	}
}