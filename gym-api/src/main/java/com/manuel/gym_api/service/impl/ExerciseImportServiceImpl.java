package com.manuel.gym_api.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manuel.gym_api.dto.ExerciseImportDTO;
import com.manuel.gym_api.model.Equipment;
import com.manuel.gym_api.model.Exercise;
import com.manuel.gym_api.model.Muscle;
import com.manuel.gym_api.repository.EquipmentRepository;
import com.manuel.gym_api.repository.ExerciseRepository;
import com.manuel.gym_api.repository.MuscleRepository;
import com.manuel.gym_api.service.ExerciseImportService;

import tools.jackson.databind.ObjectMapper;

@Service
public class ExerciseImportServiceImpl implements ExerciseImportService {

	private static final Logger log = LoggerFactory.getLogger(ExerciseImportServiceImpl.class);
	private static final String EXERCISES_JSON_PATH = "exercises.json";

	private final ObjectMapper objectMapper;
	private final ExerciseRepository exerciseRepository;
	private final MuscleRepository muscleRepository;
	private final EquipmentRepository equipmentRepository;

	public ExerciseImportServiceImpl(ObjectMapper objectMapper, ExerciseRepository exerciseRepository,
			MuscleRepository muscleRepository, EquipmentRepository equipmentRepository) {
		this.objectMapper = objectMapper;
		this.exerciseRepository = exerciseRepository;
		this.muscleRepository = muscleRepository;
		this.equipmentRepository = equipmentRepository;
	}

	@Override
	@Transactional
	public void importExercises() {
		List<ExerciseImportDTO> exercises = loadExercisesFromJson();

		if (exercises.isEmpty()) {
			log.warn("No exercises found in {}", EXERCISES_JSON_PATH);
			return;
		}

		int imported = 0;
		int skipped = 0;

		for (ExerciseImportDTO dto : exercises) {
			if (dto.getName() == null || dto.getName().isBlank()) {
				log.warn("Skipping exercise with empty name");
				skipped++;
				continue;
			}

			if (exerciseRepository.existsByNameIgnoreCase(dto.getName())) {
				log.debug("Exercise '{}' already exists, skipping", dto.getName());
				skipped++;
				continue;
			}

			Exercise exercise = mapToEntity(dto);
			exerciseRepository.save(exercise);
			imported++;
		}

		log.info("Exercise import complete — imported: {}, skipped: {}", imported, skipped);
	}

	private List<ExerciseImportDTO> loadExercisesFromJson() {
		try {
			ClassPathResource resource = new ClassPathResource(EXERCISES_JSON_PATH);

			if (!resource.exists()) {
				log.error("Resource file not found: {}", EXERCISES_JSON_PATH);
				return new ArrayList<>();
			}

			ExerciseImportDTO[] array = objectMapper.readValue(resource.getInputStream(), ExerciseImportDTO[].class);
			return Arrays.asList(array);

		} catch (IOException e) {
			log.error("Failed to parse {}: {}", EXERCISES_JSON_PATH, e.getMessage());
			return new ArrayList<>();
		}
	}

	private Exercise mapToEntity(ExerciseImportDTO dto) {
		Exercise exercise = new Exercise();
		exercise.setName(dto.getName());
		exercise.setDescription(dto.getDescription());

		if (dto.getPrimaryMuscle() != null) {
			exercise.setPrimaryMuscle(findOrCreateMuscle(dto.getPrimaryMuscle()));
		}

		if (dto.getEquipment() != null) {
			exercise.setEquipment(findOrCreateEquipment(dto.getEquipment()));
		}

		if (dto.getSecondaryMuscles() != null && !dto.getSecondaryMuscles().isEmpty()) {
			dto.getSecondaryMuscles()
					.forEach(muscleName -> exercise.getSecondaryMuscles().add(findOrCreateMuscle(muscleName)));
		}

		return exercise;
	}

	private Muscle findOrCreateMuscle(String name) {
		return muscleRepository.findByNameIgnoreCase(name).orElseGet(() -> {
			Muscle muscle = new Muscle();
			muscle.setName(name);
			return muscleRepository.save(muscle);
		});
	}

	private Equipment findOrCreateEquipment(String name) {
		return equipmentRepository.findByNameIgnoreCase(name).orElseGet(() -> {
			Equipment equipment = new Equipment();
			equipment.setName(name);
			return equipmentRepository.save(equipment);
		});
	}
}