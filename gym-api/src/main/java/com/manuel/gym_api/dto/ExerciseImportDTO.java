package com.manuel.gym_api.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExerciseImportDTO {
	private String name;
	private String description;
	private String primaryMuscle;
	private String equipment;
	private List<String> secondaryMuscles;

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

	public String getPrimaryMuscle() {
		return primaryMuscle;
	}

	public void setPrimaryMuscle(String primaryMuscle) {
		this.primaryMuscle = primaryMuscle;
	}

	public String getEquipment() {
		return equipment;
	}

	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}

	public List<String> getSecondaryMuscles() {
		return secondaryMuscles;
	}

	public void setSecondaryMuscles(List<String> secondaryMuscles) {
		this.secondaryMuscles = secondaryMuscles;
	}

}
