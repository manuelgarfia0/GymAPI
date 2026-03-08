package com.manuel.gym_api.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manuel.gym_api.integration.WgerClient;
import com.manuel.gym_api.integration.dto.WgerExerciseDTO;
import com.manuel.gym_api.integration.dto.WgerResponseDTO;
import com.manuel.gym_api.model.Exercise;
import com.manuel.gym_api.repository.ExerciseRepository;
import com.manuel.gym_api.service.ExerciseImportService;

@Service
public class ExerciseImportServiceImpl implements ExerciseImportService {

	private final WgerClient wgerClient;
	private final ExerciseRepository exerciseRepository;

	public ExerciseImportServiceImpl(WgerClient wgerClient, ExerciseRepository exerciseRepository) {
		this.wgerClient = wgerClient;
		this.exerciseRepository = exerciseRepository;
	}

	@Override
	@Transactional
	public void importExercises() {
		WgerResponseDTO response = wgerClient.getExercises();

		if (response != null && response.getResults() != null) {
			int count = 0;

			for (WgerExerciseDTO wgerDto : response.getResults()) {
				try {
					// 1. Evitamos meter ejercicios vacíos o sin nombre
					if (wgerDto.getName() == null || wgerDto.getName().isBlank()) {
						continue;
					}

					// 2. Comprobación LIMPIA y RÁPIDA directamente en base de datos
					if (exerciseRepository.existsByNameIgnoreCase(wgerDto.getName().trim())) {
						continue; // Si ya existe, nos lo saltamos
					}

					// 3. Creamos la entidad
					Exercise exercise = new Exercise();
					exercise.setName(wgerDto.getName().trim());

					// Limpiamos las etiquetas de HTML
					String cleanDescription = stripHtmlTags(wgerDto.getDescription());
					if (cleanDescription != null && cleanDescription.length() > 2000) {
						cleanDescription = cleanDescription.substring(0, 1997) + "...";
					}
					exercise.setDescription(cleanDescription);
					exercise.setSystemExercise(true);

					// 4. Guardamos
					exerciseRepository.save(exercise);
					count++;

				} catch (Exception e) {
					// Si falla UN ejercicio en concreto, imprimimos el error pero seguimos con el
					// bucle
					System.err.println("Error importing exercise '" + wgerDto.getName() + "': " + e.getMessage());
				}
			}
			System.out.println("Importación finalizada. Se han guardado " + count + " ejercicios nuevos.");
		}
	}

	private String stripHtmlTags(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("<[^>]*>", "").trim();
	}
}