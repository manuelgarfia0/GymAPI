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
import com.manuel.gym_api.repository.ExerciseRepository;
import com.manuel.gym_api.service.ExerciseImportService;

@Service
public class ExerciseImportServiceImpl implements ExerciseImportService {

	private final ExerciseRepository exerciseRepository;

	// Solo inyectamos el repositorio, ¡nada de Jackson!
	public ExerciseImportServiceImpl(ExerciseRepository exerciseRepository) {
		this.exerciseRepository = exerciseRepository;
	}

	@Override
	@Transactional
	public void importExercises() {
		try {
			// 1. Leemos el archivo JSON local
			ClassPathResource resource = new ClassPathResource("exercises.json");
			InputStream inputStream = resource.getInputStream();

			// 2. Usamos un lector de texto nativo de Java
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
			StringBuilder jsonBuilder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				jsonBuilder.append(line);
			}
			String jsonContent = jsonBuilder.toString();

			// 3. Parseamos el JSON manualmente de forma súper sencilla (sin librerías
			// extra)
			List<Exercise> exercisesToSave = parseJsonManually(jsonContent);

			int count = 0;

			// 4. Guardamos en base de datos
			for (Exercise ex : exercisesToSave) {
				// Comprobamos si el nombre existe
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

	/**
	 * Un mini-parseador casero para no depender de librerías como Jackson. Lee el
	 * JSON buscando las palabras "name" y "description".
	 */
	private List<Exercise> parseJsonManually(String json) {
		List<Exercise> exercises = new ArrayList<>();

		// Partimos el JSON por las llaves de apertura '{'
		String[] blocks = json.split("\\{");

		for (String block : blocks) {
			if (block.contains("\"name\"") && block.contains("\"description\"")) {
				try {
					// Extraemos el nombre
					String namePart = block.split("\"name\"\\s*:\\s*\"")[1];
					String name = namePart.substring(0, namePart.indexOf("\""));

					// Extraemos la descripción
					String descPart = block.split("\"description\"\\s*:\\s*\"")[1];
					String description = descPart.substring(0, descPart.indexOf("\""));

					Exercise ex = new Exercise();
					ex.setName(name.trim());
					ex.setDescription(description.trim());
					ex.setSystemExercise(true);

					exercises.add(ex);
				} catch (Exception e) {
					// Si algún bloque se lee mal, lo ignoramos y seguimos
				}
			}
		}

		return exercises;
	}
}