package com.manuel.gym_api.integration.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WgerExerciseDTO {

	private List<WgerExerciseTranslation> exercises;

	public List<WgerExerciseTranslation> getExercises() {
		return exercises;
	}

	public void setExercises(List<WgerExerciseTranslation> exercises) {
		this.exercises = exercises;
	}

	// Clase interna para leer el nombre y la descripción de la traducción en inglés
	// (o el idioma que haya)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class WgerExerciseTranslation {
		private String name;
		private String description;
		private int language; // 2 = Inglés

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public int getLanguage() {
			return language;
		}

		public void setLanguage(int language) {
			this.language = language;
		}
	}
}