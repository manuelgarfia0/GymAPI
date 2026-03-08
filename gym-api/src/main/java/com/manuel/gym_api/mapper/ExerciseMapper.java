package com.manuel.gym_api.mapper;

import org.springframework.stereotype.Component;

import com.manuel.gym_api.dto.ExerciseDTO;
import com.manuel.gym_api.model.Exercise;
import com.manuel.gym_api.model.Muscle;

@Component
public class ExerciseMapper {

	public ExerciseDTO toDTO(Exercise exercise) {
		ExerciseDTO result = null;

		if (exercise != null) {
			Long primaryMuscleId = (exercise.getPrimaryMuscle() != null) ? exercise.getPrimaryMuscle().getId() : null;
			Long secondaryMuscleId = (exercise.getSecondaryMuscle() != null) ? exercise.getSecondaryMuscle().getId()
					: null;

			result = new ExerciseDTO(exercise.getId(), exercise.getName(), primaryMuscleId, secondaryMuscleId);
		}

		return result;
	}

	public Exercise toEntity(ExerciseDTO dto, Muscle primary, Muscle secondary) {
		Exercise result = null;

		if (dto != null) {
			result = new Exercise();
			result.setName(dto.getName());
			result.setPrimaryMuscle(primary);
			result.setSecondaryMuscle(secondary);
		}

		return result;
	}
}