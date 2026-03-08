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
		// 1. Nos traemos los datos parseados del cliente de Wger
		WgerResponseDTO response = wgerClient.getExercises();

		if (response != null && response.getResults() != null) {

			// 2. Iteramos por cada ejercicio de la API
			for (WgerExerciseDTO wgerDto : response.getResults()) {

				// Evitamos meter ejercicios vacíos o sin nombre
				if (wgerDto.getName() == null || wgerDto.getName().isEmpty()) {
					continue;
				}

				// 3. Creamos la entidad
				Exercise exercise = new Exercise();
				exercise.setName(wgerDto.getName());

				// Limpiamos las etiquetas <p></p> que trae Wger
				String cleanDescription = stripHtmlTags(wgerDto.getDescription());

				// Las descripciones de Wger a veces son kilométricas. Cortamos a 2000 chars por
				// si acaso.
				if (cleanDescription != null && cleanDescription.length() > 2000) {
					cleanDescription = cleanDescription.substring(0, 1997) + "...";
				}
				exercise.setDescription(cleanDescription);

				// Marcamos que es un ejercicio del sistema (no creado por un usuario concreto)
				exercise.setSystemExercise(true);

				/*
				 * Nota: Aquí omitimos los Músculos y el Equipamiento porque requeriría importar
				 * primero TODA la base de datos de músculos de Wger para cruzar los IDs (ej:
				 * Wger dice que el músculo 1 es Biceps, pero en tu base de datos el ID 1 podría
				 * ser Pecho). De momento los importamos sin músculos, luego puedes asignárselos
				 * en la app.
				 */

				// 4. Guardamos en Postgres
				exerciseRepository.save(exercise);
			}
			System.out.println(
					"Importación finalizada. Se han guardado " + response.getResults().size() + " ejercicios.");
		}
	}

	// Método de utilidad para quitar las etiquetas HTML de la descripción
	private String stripHtmlTags(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("<[^>]*>", "").trim();
	}
}