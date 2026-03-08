package com.manuel.gym_api.integration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

// Esto mapea cada ejercicio dentro del array "results" de Wger
public class WgerExerciseDTO {
	private String name;
	private String description;

	// En Wger, los músculos principales no vienen como objetos, vienen como un
	// array de IDs [1, 2]
	@JsonProperty("muscles")
	private Integer[] primaryMuscles;

	@JsonProperty("muscles_secondary")
	private Integer[] secondaryMuscles;

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

	public Integer[] getPrimaryMuscles() {
		return primaryMuscles;
	}

	public void setPrimaryMuscles(Integer[] primaryMuscles) {
		this.primaryMuscles = primaryMuscles;
	}

	public Integer[] getSecondaryMuscles() {
		return secondaryMuscles;
	}

	public void setSecondaryMuscles(Integer[] secondaryMuscles) {
		this.secondaryMuscles = secondaryMuscles;
	}
}