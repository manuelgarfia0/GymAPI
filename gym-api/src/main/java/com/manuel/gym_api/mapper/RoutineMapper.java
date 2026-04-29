package com.manuel.gym_api.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.manuel.gym_api.dto.RoutineDTO;
import com.manuel.gym_api.dto.RoutineExerciseDTO;
import com.manuel.gym_api.model.Exercise;
import com.manuel.gym_api.model.Routine;
import com.manuel.gym_api.model.RoutineExercise;
import com.manuel.gym_api.model.User;

@Component
public class RoutineMapper {

	public RoutineDTO toDTO(Routine routine) {
		if (routine == null)
			return null;

		List<RoutineExerciseDTO> exercisesDTO = routine.getRoutineExercises() != null
				? routine.getRoutineExercises().stream().map(this::toExerciseDTO).collect(Collectors.toList())
				: Collections.emptyList();

		String createdAtStr = routine.getCreatedAt() != null ? routine.getCreatedAt().toString() : null;

		return new RoutineDTO(routine.getId(), routine.getName(), routine.getDescription(), routine.getUser().getId(),
				exercisesDTO, createdAtStr);
	}

	public RoutineExerciseDTO toExerciseDTO(RoutineExercise entity) {
		if (entity == null)
			return null;

		// Extraemos el nombre del ejercicio (si existe)
		String exerciseName = (entity.getExercise() != null) ? entity.getExercise().getName() : null;

		return new RoutineExerciseDTO(entity.getId(), entity.getExercise().getId(), exerciseName,
				entity.getOrderIndex(), entity.getSets(), entity.getReps(), entity.getRestSeconds(), entity.getTargetWeight(), entity.getNotes());
	}

	public Routine toEntity(RoutineDTO dto, User user) {
		Routine routine = new Routine();
		routine.setName(dto.getName());
		routine.setDescription(dto.getDescription());
		routine.setUser(user);
		return routine;
	}

	public RoutineExercise toRoutineExerciseEntity(RoutineExerciseDTO dto, Routine routine, Exercise exercise) {
		RoutineExercise entity = new RoutineExercise();
		entity.setRoutine(routine);
		entity.setExercise(exercise);
		entity.setOrderIndex(dto.getOrderIndex());
		entity.setSets(dto.getSets());
		entity.setReps(dto.getReps());
		entity.setRestSeconds(dto.getRestSeconds());
		entity.setTargetWeight(dto.getTargetWeight());
		entity.setNotes(dto.getNotes());
		return entity;
	}
}