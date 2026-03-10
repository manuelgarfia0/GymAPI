package com.manuel.gym_api.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manuel.gym_api.model.Exercise;
import com.manuel.gym_api.repository.EquipmentRepository;
import com.manuel.gym_api.repository.ExerciseRepository;
import com.manuel.gym_api.repository.MuscleRepository;
import com.manuel.gym_api.service.ExerciseImportService;

@Service
public class ExerciseImportServiceImpl implements ExerciseImportService {

	private final ExerciseRepository exerciseRepository;
	private final MuscleRepository muscleRepository;
	private final EquipmentRepository equipmentRepository;

	public ExerciseImportServiceImpl(ExerciseRepository exerciseRepository, MuscleRepository muscleRepository,
			EquipmentRepository equipmentRepository) {
		this.exerciseRepository = exerciseRepository;
		this.muscleRepository = muscleRepository;
		this.equipmentRepository = equipmentRepository;
	}

	@Override
	@Transactional
	public void importExercises() {
		try {
			ClassPathResource resource = new ClassPathResource("exercises.json");
			InputStream inputStream = resource.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
			StringBuilder jsonBuilder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				jsonBuilder.append(line);
			}
			String jsonContent = jsonBuilder.toString();

			List<Exercise> exercisesToSave = parseJsonManually(jsonContent);

			int count = 0;
			for (Exercise ex : exercisesToSave) {
				if (!exerciseRepository.existsByNameIgnoreCase(ex.getName())) {
					exerciseRepository.save(ex);
					count++;
				}
			}

			System.out.println("Importación local finalizada. Se han guardado " + count + " ejercicios base.");

		} catch (Exception e) {
			System.err.println("Error fatal importando ejercicios locales: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private List<Exercise> parseJsonManually(String json) {
		List<Exercise> exercises = new ArrayList<>();
		String[] blocks = json.split("\\{");

		for (String block : blocks) {
			if (block.contains("\"name\"") && block.contains("\"description\"")) {
				try {
					String name = extractStringValue(block, "\"name\"");
					String description = extractStringValue(block, "\"description\"");

					Long primaryId = extractLongValue(block, "\"primary_muscle_id\"");
					Long equipmentId = extractLongValue(block, "\"equipment_id\"");
					List<Long> secondaryIds = extractLongArray(block, "\"secondary_muscle_ids\"");

					Exercise ex = new Exercise();
					ex.setName(name);
					ex.setDescription(description);
					ex.setSystemExercise(true);

					if (primaryId != null) {
						muscleRepository.findById(primaryId).ifPresent(ex::setPrimaryMuscle);
					}

					if (equipmentId != null) {
						equipmentRepository.findById(equipmentId).ifPresent(ex::setEquipment);
					}

					for (Long secId : secondaryIds) {
						muscleRepository.findById(secId).ifPresent(m -> ex.getSecondaryMuscles().add(m));
					}

					exercises.add(ex);
				} catch (Exception e) {
					// Ignorar bloque roto
				}
			}
		}
		return exercises;
	}

	private String extractStringValue(String block, String key) {
		try {
			String part = block.split(key + "\\s*:\\s*\"")[1];
			return part.substring(0, part.indexOf("\"")).trim();
		} catch (Exception e) {
			return "";
		}
	}

	private Long extractLongValue(String block, String key) {
		try {
			String part = block.split(key + "\\s*:\\s*")[1];
			// Quitamos las comas, los saltos de línea y los espacios de alrededor del
			// número
			String numberStr = part.split("[,}\\n]")[0].trim();
			if (numberStr.equals("null"))
				return null;
			return Long.parseLong(numberStr);
		} catch (Exception e) {
			return null;
		}
	}

	private List<Long> extractLongArray(String block, String key) {
		List<Long> result = new ArrayList<>();
		try {
			if (!block.contains(key))
				return result;

			String part = block.split(key + "\\s*:\\s*\\[")[1];
			String arrayStr = part.split("\\]")[0].trim();
			if (arrayStr.isEmpty())
				return result;

			String[] numbers = arrayStr.split(",");
			for (String num : numbers) {
				result.add(Long.parseLong(num.trim()));
			}
		} catch (Exception e) {
			// Ignorar
		}
		return result;
	}
}