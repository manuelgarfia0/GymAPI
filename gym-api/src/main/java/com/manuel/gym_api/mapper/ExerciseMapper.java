package com.manuel.gym_api.mapper;

import org.springframework.stereotype.Component;

import com.manuel.gym_api.dto.ExerciseDTO;
import com.manuel.gym_api.model.Exercise;
import com.manuel.gym_api.model.Muscle;

@Component
public class ExerciseMapper {

	public ExerciseDTO toDTO(Exercise exercise) {
		if (exercise == null)
			return null;

		Long secondaryMuscleId = (exercise.getSecondaryMuscle() != null) ? exercise.getSecondaryMuscle().getId() : null;

		return new ExerciseDTO(exercise.getId(), exercise.getName(), exercise.getPrimaryMuscle().getId(),
				secondaryMuscleId);
	}

	public Exercise toEntity(ExerciseDTO dto, Muscle primary, Muscle secondary) {
		if (dto == null)
			return null;

		Exercise exercise = new Exercise();
		exercise.setName(dto.getName());
		exercise.setPrimaryMuscle(primary);
		exercise.setSecondaryMuscle(secondary);

		return exercise;
	}
}