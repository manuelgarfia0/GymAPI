package com.manuel.gym_api.service;

import java.util.List;

import com.manuel.gym_api.dto.RoutineDTO;

public interface RoutineService {
	RoutineDTO createRoutine(RoutineDTO routineDTO);

	RoutineDTO getRoutineById(Long id);

	List<RoutineDTO> getRoutinesByUserId(Long userId);

	RoutineDTO updateRoutine(Long id, RoutineDTO routineDTO);

	void deleteRoutine(Long id);
}