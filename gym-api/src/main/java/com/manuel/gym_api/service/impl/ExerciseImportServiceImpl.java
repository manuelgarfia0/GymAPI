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
			for (WgerExerciseDTO wgerData : response.getResults()) {

				// 1. Verificamos que el ejercicio no exista ya (por el nombre) para no duplicar
				boolean exists = exerciseRepository.findAll().stream()
						.anyMatch(e -> e.getName().equalsIgnoreCase(wgerData.getName()));

				if (!exists) {
					Exercise newExercise = new Exercise();
					newExercise.setName(wgerData.getName());

					// Wger devuelve las descripciones con etiquetas HTML <p>, etc. Podrías
					// limpiarlo si quisieras.
					newExercise.setDescription(wgerData.getDescription());

					newExercise.setSystemExercise(true); // Es de sistema porque viene de la API pública

					/*
					 * Nota: Para los músculos, Wger devuelve un array de IDs (ej: [1, 4]). Por
					 * ahora los dejamos a null porque los IDs de Wger no coinciden con los IDs de
					 * tu base de datos de PostgreSQL a menos que importes también los músculos.
					 */
					newExercise.setPrimaryMuscle(null);
					newExercise.setSecondaryMuscle(null);

					// Guardamos el ejercicio en tu BBDD
					exerciseRepository.save(newExercise);
				}
			}
			System.out.println("Importación completada con éxito.");
		}
	}
}