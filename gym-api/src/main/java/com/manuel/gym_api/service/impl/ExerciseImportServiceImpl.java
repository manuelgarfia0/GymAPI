package com.manuel.gym_api.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manuel.gym_api.integration.WgerClient;
import com.manuel.gym_api.integration.dto.WgerExerciseDTO;
import com.manuel.gym_api.integration.dto.WgerExerciseDTO.WgerExerciseTranslation;
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

			for (WgerExerciseDTO wgerBase : response.getResults()) {

				// Buscamos el nombre en la lista de ejercicios anidada (preferimos language = 2
				// que es inglés)
				WgerExerciseTranslation englishTranslation = null;

				if (wgerBase.getExercises() != null) {
					for (WgerExerciseTranslation trans : wgerBase.getExercises()) {
						if (trans.getLanguage() == 2 && trans.getName() != null && !trans.getName().isBlank()) {
							englishTranslation = trans;
							break; // Encontramos la inglesa, nos vale.
						}
					}
					// Si no tiene inglés, pillamos la primera que haya por tener algo.
					if (englishTranslation == null && !wgerBase.getExercises().isEmpty()) {
						englishTranslation = wgerBase.getExercises().get(0);
					}
				}

				if (englishTranslation == null || englishTranslation.getName() == null
						|| englishTranslation.getName().isBlank()) {
					continue; // Sigue sin nombre válido
				}

				String exerciseName = englishTranslation.getName().trim();

				// Comprobamos si ya existe en BBDD
				if (exerciseRepository.existsByNameIgnoreCase(exerciseName)) {
					continue;
				}

				// Creamos la entidad
				Exercise exercise = new Exercise();
				exercise.setName(exerciseName);

				// Limpiamos descripción HTML
				String cleanDescription = stripHtmlTags(englishTranslation.getDescription());
				if (cleanDescription != null && cleanDescription.length() > 2000) {
					cleanDescription = cleanDescription.substring(0, 1997) + "...";
				}
				exercise.setDescription(cleanDescription);
				exercise.setSystemExercise(true);

				// Guardamos
				exerciseRepository.save(exercise);
				count++;
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