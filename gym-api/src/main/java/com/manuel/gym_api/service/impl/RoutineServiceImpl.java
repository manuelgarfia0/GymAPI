package com.manuel.gym_api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manuel.gym_api.dto.RoutineDTO;
import com.manuel.gym_api.dto.RoutineExerciseDTO;
import com.manuel.gym_api.exception.ResourceNotFoundException;
import com.manuel.gym_api.mapper.RoutineMapper;
import com.manuel.gym_api.model.Exercise;
import com.manuel.gym_api.model.Routine;
import com.manuel.gym_api.model.User;
import com.manuel.gym_api.repository.ExerciseRepository;
import com.manuel.gym_api.repository.RoutineRepository;
import com.manuel.gym_api.repository.UserRepository;
import com.manuel.gym_api.service.RoutineService;

@Service
public class RoutineServiceImpl implements RoutineService {

	private final RoutineRepository routineRepository;
	private final UserRepository userRepository;
	private final ExerciseRepository exerciseRepository;
	private final RoutineMapper routineMapper;

	public RoutineServiceImpl(RoutineRepository routineRepository, UserRepository userRepository,
			ExerciseRepository exerciseRepository, RoutineMapper routineMapper) {
		this.routineRepository = routineRepository;
		this.userRepository = userRepository;
		this.exerciseRepository = exerciseRepository;
		this.routineMapper = routineMapper;
	}

	@Override
	@Transactional
	public RoutineDTO createRoutine(RoutineDTO dto) {
		User user = getUserOrThrow(dto.getUserId());
		Routine routine = routineMapper.toEntity(dto, user);

		assignExercisesToRoutine(routine, dto.getExercises());

		Routine savedRoutine = routineRepository.save(routine);
		return routineMapper.toDTO(savedRoutine);
	}

	@Override
	@Transactional(readOnly = true)
	public RoutineDTO getRoutineById(Long id) {
		Routine routine = getRoutineOrThrow(id);
		return routineMapper.toDTO(routine);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RoutineDTO> getRoutinesByUserId(Long userId) {
		// Validar que el usuario existe antes de buscar
		getUserOrThrow(userId);

		return routineRepository.findByUserId(userId).stream().map(routineMapper::toDTO).toList();
	}

	@Override
	@Transactional
	public RoutineDTO updateRoutine(Long id, RoutineDTO dto) {
		Routine routine = getRoutineOrThrow(id);

		// Actualizar propiedades simples
		routine.setName(dto.getName());
		routine.setDescription(dto.getDescription());

		// Limpiar colección e insertar nuevos
		routine.getRoutineExercises().clear();

		// IMPORTANTE: Hacemos un flush de la base de datos para borrar huellas viejas
		routineRepository.saveAndFlush(routine);

		assignExercisesToRoutine(routine, dto.getExercises());

		Routine updatedRoutine = routineRepository.save(routine);
		return routineMapper.toDTO(updatedRoutine);
	}

	@Override
	@Transactional
	public void deleteRoutine(Long id) {
		if (!routineRepository.existsById(id)) {
			throw new ResourceNotFoundException("Cannot delete. Routine not found with ID: " + id);
		}
		routineRepository.deleteById(id);
	}

	private void assignExercisesToRoutine(Routine routine, List<RoutineExerciseDTO> exerciseDTOs) {
		if (exerciseDTOs == null || exerciseDTOs.isEmpty()) {
			return;
		}

		exerciseDTOs.forEach(exDTO -> {
			Exercise exercise = getExerciseOrThrow(exDTO.getExerciseId());
			// Como aquí recuperamos el 'exercise' de la BD, su nombre DEBE estar presente
			routine.getRoutineExercises().add(routineMapper.toRoutineExerciseEntity(exDTO, routine, exercise));
		});
	}

	private User getUserOrThrow(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
	}

	private Routine getRoutineOrThrow(Long routineId) {
		return routineRepository.findById(routineId)
				.orElseThrow(() -> new ResourceNotFoundException("Routine not found with ID: " + routineId));
	}

	private Exercise getExerciseOrThrow(Long exerciseId) {
		return exerciseRepository.findById(exerciseId)
				.orElseThrow(() -> new ResourceNotFoundException("Exercise not found with ID: " + exerciseId));
	}
}