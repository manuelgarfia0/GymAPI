package com.manuel.gym_api.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RoutineDTO {

	private Long id;
	@NotBlank(message = "Routine name is required")
	@Size(max = 100, message = "Name cannot exceed 100 characters")
	private String name;
	@Size(max = 500, message = "Description cannot exceed 500 characters")
	private String description;
	private Long userId;
	private List<RoutineExerciseDTO> exercises;

	public RoutineDTO() {
	}

	public RoutineDTO(Long id, String name, String description, Long userId, List<RoutineExerciseDTO> exercises) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.userId = userId;
		this.exercises = exercises;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<RoutineExerciseDTO> getExercises() {
		return exercises;
	}

	public void setExercises(List<RoutineExerciseDTO> exercises) {
		this.exercises = exercises;
	}
}