package com.manuel.gym_api.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.manuel.gym_api.dto.WorkoutDTO;
import com.manuel.gym_api.dto.WorkoutSetDTO;
import com.manuel.gym_api.model.Exercise;
import com.manuel.gym_api.model.Routine;
import com.manuel.gym_api.model.User;
import com.manuel.gym_api.model.Workout;
import com.manuel.gym_api.model.WorkoutSet;

@Component
public class WorkoutMapper {

	public WorkoutDTO toDTO(Workout workout) {
		if (workout == null)
			return null;

		WorkoutDTO dto = new WorkoutDTO();
		dto.setId(workout.getId());
		dto.setName(workout.getName());
		dto.setNotes(workout.getNotes());
		dto.setStartTime(workout.getStartTime());
		dto.setEndTime(workout.getEndTime());
		dto.setUserId(workout.getUser().getId());

		if (workout.getRoutine() != null) {
			dto.setRoutineId(workout.getRoutine().getId());
		}

		List<WorkoutSetDTO> setsDTO = workout.getSets() != null
				? workout.getSets().stream().map(this::toSetDTO).collect(Collectors.toList())
				: Collections.emptyList();

		dto.setSets(setsDTO);
		return dto;
	}

	public WorkoutSetDTO toSetDTO(WorkoutSet set) {
		if (set == null)
			return null;

		WorkoutSetDTO dto = new WorkoutSetDTO();
		dto.setId(set.getId());
		dto.setExerciseId(set.getExercise().getId());

		if (set.getExercise() != null && set.getExercise().getName() != null) {
			dto.setExerciseName(set.getExercise().getName());
		}

		dto.setExerciseOrder(set.getExerciseOrder());
		dto.setSetNumber(set.getSetNumber());
		dto.setWeight(set.getWeight());
		dto.setReps(set.getReps());
		dto.setNotes(set.getNotes());
		dto.setWarmup(set.isWarmup());
		dto.setCompleted(set.isCompleted());

		return dto;
	}

	public Workout toEntity(WorkoutDTO dto, User user, Routine routine) {
		Workout workout = new Workout();
		workout.setName(dto.getName());
		workout.setNotes(dto.getNotes());
		workout.setStartTime(dto.getStartTime());
		workout.setEndTime(dto.getEndTime());
		workout.setUser(user);
		workout.setRoutine(routine);

		return workout;
	}

	public WorkoutSet toSetEntity(WorkoutSetDTO dto, Workout workout, Exercise exercise) {
		WorkoutSet set = new WorkoutSet();
		set.setWorkout(workout);
		set.setExercise(exercise);
		set.setExerciseOrder(dto.getExerciseOrder());
		set.setSetNumber(dto.getSetNumber());
		set.setWeight(dto.getWeight());
		set.setReps(dto.getReps());
		set.setNotes(dto.getNotes());
		set.setWarmup(dto.isWarmup());
		set.setCompleted(dto.isCompleted());

		return set;
	}
}