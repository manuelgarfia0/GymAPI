package com.manuel.gym_api.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.manuel.gym_api.dto.ExerciseDTO;
import com.manuel.gym_api.model.Equipment;
import com.manuel.gym_api.model.Exercise;
import com.manuel.gym_api.model.Muscle;

@Component
public class ExerciseMapper {

	public ExerciseDTO toDTO(Exercise exercise) {
		if (exercise == null) {
			return null;
		}

		Long primaryId = exercise.getPrimaryMuscle() != null ? exercise.getPrimaryMuscle().getId() : null;
		String primaryName = exercise.getPrimaryMuscle() != null ? exercise.getPrimaryMuscle().getName()
				: "Sin especificar";
		String equipmentName = exercise.getEquipment() != null ? exercise.getEquipment().getName() : "Peso corporal";

		List<Long> secIds = new ArrayList<>();
		List<String> secNames = new ArrayList<>();

		if (exercise.getSecondaryMuscles() != null) {
			for (Muscle m : exercise.getSecondaryMuscles()) {
				secIds.add(m.getId());
				secNames.add(m.getName());
			}
		}

		return new ExerciseDTO(exercise.getId(), exercise.getName(), exercise.getDescription(), primaryId, primaryName,
				equipmentName, secIds, secNames);
	}

	public Exercise toEntity(ExerciseDTO dto, Muscle primary, Set<Muscle> secondary, Equipment eq) {
		if (dto == null) {
			return null;
		}

		Exercise result = new Exercise();
		result.setName(dto.getName());
		result.setDescription(dto.getDescription());
		result.setPrimaryMuscle(primary);
		if (secondary != null) {
			result.setSecondaryMuscles(secondary);
		}
		result.setEquipment(eq);
		return result;
	}
}