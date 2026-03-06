package com.manuel.gym_api.dto;

public class ExerciseDTO {

	private Long id;
	private String name;
	private Long primaryMuscleId;
	private Long secondaryMuscleId;

	public ExerciseDTO() {
	}

	public ExerciseDTO(Long id, String name, Long primaryMuscleId, Long secondaryMuscleId) {
		this.id = id;
		this.name = name;
		this.primaryMuscleId = primaryMuscleId;
		this.secondaryMuscleId = secondaryMuscleId;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Long getPrimaryMuscleId() {
		return primaryMuscleId;
	}

	public Long getSecondaryMuscleId() {
		return secondaryMuscleId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrimaryMuscleId(Long primaryMuscleId) {
		this.primaryMuscleId = primaryMuscleId;
	}

	public void setSecondaryMuscleId(Long secondaryMuscleId) {
		this.secondaryMuscleId = secondaryMuscleId;
	}
}