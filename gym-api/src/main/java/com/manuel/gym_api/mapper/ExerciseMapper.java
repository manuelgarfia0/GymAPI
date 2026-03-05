package com.manuel.gym_api.mapper;

import com.manuel.gym_api.dto.ExerciseDTO;
import com.manuel.gym_api.model.Exercise;

public class ExerciseMapper {

	public static ExerciseDTO toDTO(Exercise exercise) {
		return new ExerciseDTO(exercise.getId(), exercise.getName(), exercise.getMuscleGroup());
	}

	public static Exercise toEntity(ExerciseDTO dto) {
		Exercise exercise = new Exercise();
		exercise.setId(dto.getId());
		exercise.setName(dto.getName());
		exercise.setMuscleGroup(dto.getMuscleGroup());
		return exercise;
	}
}