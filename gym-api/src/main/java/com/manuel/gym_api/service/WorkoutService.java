package com.manuel.gym_api.service;

import java.util.List;

import com.manuel.gym_api.dto.WorkoutDTO;

public interface WorkoutService {
	WorkoutDTO createWorkout(WorkoutDTO workoutDTO);

	WorkoutDTO getWorkoutById(Long id);

	List<WorkoutDTO> getWorkoutsByUserId(Long userId);

	WorkoutDTO updateWorkout(Long id, WorkoutDTO workoutDTO);

	void deleteWorkout(Long id);
}