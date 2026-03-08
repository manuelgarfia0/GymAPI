package com.manuel.gym_api.integration.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WgerExerciseDTO {
	private String name;
	private String description;

	@JsonProperty("muscles")
	private List<Long> muscles;

	@JsonProperty("muscles_secondary")
	private List<Long> muscles_secondary;

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

	public List<Long> getMuscles() {
		return muscles;
	}

	public void setMuscles(List<Long> muscles) {
		this.muscles = muscles;
	}

	public List<Long> getMuscles_secondary() {
		return muscles_secondary;
	}

	public void setMuscles_secondary(List<Long> muscles_secondary) {
		this.muscles_secondary = muscles_secondary;
	}
}