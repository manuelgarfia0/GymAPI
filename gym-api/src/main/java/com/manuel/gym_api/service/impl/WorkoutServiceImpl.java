package com.manuel.gym_api.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manuel.gym_api.dto.WorkoutDTO;
import com.manuel.gym_api.dto.WorkoutSetDTO;
import com.manuel.gym_api.exception.ResourceNotFoundException;
import com.manuel.gym_api.mapper.WorkoutMapper;
import com.manuel.gym_api.model.Exercise;
import com.manuel.gym_api.model.Routine;
import com.manuel.gym_api.model.User;
import com.manuel.gym_api.model.Workout;
import com.manuel.gym_api.repository.ExerciseRepository;
import com.manuel.gym_api.repository.RoutineRepository;
import com.manuel.gym_api.repository.UserRepository;
import com.manuel.gym_api.repository.WorkoutRepository;
import com.manuel.gym_api.service.WorkoutService;

@Service
public class WorkoutServiceImpl implements WorkoutService {

	private final WorkoutRepository workoutRepository;
	private final UserRepository userRepository;
	private final RoutineRepository routineRepository;
	private final ExerciseRepository exerciseRepository;
	private final WorkoutMapper workoutMapper;

	public WorkoutServiceImpl(WorkoutRepository workoutRepository, UserRepository userRepository,
			RoutineRepository routineRepository, ExerciseRepository exerciseRepository, WorkoutMapper workoutMapper) {
		this.workoutRepository = workoutRepository;
		this.userRepository = userRepository;
		this.routineRepository = routineRepository;
		this.exerciseRepository = exerciseRepository;
		this.workoutMapper = workoutMapper;
	}

	@Override
	@Transactional
	public WorkoutDTO createWorkout(WorkoutDTO dto) {
		User user = getUserOrThrow(dto.getUserId());
		Routine routine = null;

		if (dto.getRoutineId() != null) {
			routine = routineRepository.findById(dto.getRoutineId()).orElseThrow(
					() -> new ResourceNotFoundException("Routine not found with ID: " + dto.getRoutineId()));
		}

		Workout workout = workoutMapper.toEntity(dto, user, routine);
		assignSetsToWorkout(workout, dto.getSets());

		Workout savedWorkout = workoutRepository.save(workout);
		return workoutMapper.toDTO(savedWorkout);
	}

	@Override
	@Transactional(readOnly = true)
	public WorkoutDTO getWorkoutById(Long id) {
		Workout workout = getWorkoutOrThrow(id);
		return workoutMapper.toDTO(workout);
	}

	@Override
	@Transactional(readOnly = true)
	public List<WorkoutDTO> getWorkoutsByUserId(Long userId) {
		getUserOrThrow(userId); // Validate user exists

		return workoutRepository.findByUserIdOrderByStartTimeDesc(userId).stream().map(workoutMapper::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public WorkoutDTO updateWorkout(Long id, WorkoutDTO dto) {
		Workout workout = getWorkoutOrThrow(id);

		// Update base properties
		workout.setName(dto.getName());
		workout.setNotes(dto.getNotes());
		workout.setStartTime(dto.getStartTime());
		workout.setEndTime(dto.getEndTime());

		// Clear existing sets and add the new ones
		workout.getSets().clear();
		assignSetsToWorkout(workout, dto.getSets());

		Workout updatedWorkout = workoutRepository.save(workout);
		return workoutMapper.toDTO(updatedWorkout);
	}

	@Override
	@Transactional
	public void deleteWorkout(Long id) {
		if (!workoutRepository.existsById(id)) {
			throw new ResourceNotFoundException("Cannot delete. Workout not found with ID: " + id);
		}
		workoutRepository.deleteById(id);
	}

	@Override
	@Transactional
	public WorkoutDTO endWorkout(Long id) {
		Workout workout = getWorkoutOrThrow(id);
		workout.setEndTime(LocalDateTime.now());
		return workoutMapper.toDTO(workoutRepository.save(workout));
	}

	private void assignSetsToWorkout(Workout workout, List<WorkoutSetDTO> setDTOs) {
		if (setDTOs == null || setDTOs.isEmpty()) {
			return;
		}

		setDTOs.forEach(setDTO -> {
			Exercise exercise = exerciseRepository.findById(setDTO.getExerciseId()).orElseThrow(
					() -> new ResourceNotFoundException("Exercise not found with ID: " + setDTO.getExerciseId()));

			workout.getSets().add(workoutMapper.toSetEntity(setDTO, workout, exercise));
		});
	}

	private User getUserOrThrow(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
	}

	private Workout getWorkoutOrThrow(Long workoutId) {
		return workoutRepository.findById(workoutId)
				.orElseThrow(() -> new ResourceNotFoundException("Workout not found with ID: " + workoutId));
	}
}