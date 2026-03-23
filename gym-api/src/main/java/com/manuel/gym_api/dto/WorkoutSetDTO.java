package com.manuel.gym_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class WorkoutSetDTO {

	private Long id;

	@NotNull(message = "Exercise ID is required")
	private Long exerciseId;

	private String exerciseName;

	@NotNull(message = "Order of the exercises is required")
	private Integer exerciseOrder;

	@NotNull(message = "El número de serie es obligatorio")
	private Integer setNumber;

	@NotNull(message = "Weight is required")
	@Min(value = 0, message = "Weight cannot be negative")
	private Double weight;

	@NotNull(message = "Las repeticiones son obligatorias")
	@Min(value = 0, message = "Las repeticiones no pueden ser negativas")
	private Integer reps;

	private String notes;

	// CORRECCIÓN: Jackson serializa isWarmup() como "warmup" (strips "is").
	// Flutter envía "isWarmup". @JsonProperty fuerza el nombre correcto en
	// ambas direcciones (serialización y deserialización).
	@JsonProperty("isWarmup")
	private boolean isWarmup;

	@JsonProperty("isCompleted")
	private boolean isCompleted;

	public WorkoutSetDTO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getExerciseId() {
		return exerciseId;
	}

	public void setExerciseId(Long exerciseId) {
		this.exerciseId = exerciseId;
	}

	public String getExerciseName() {
		return exerciseName;
	}

	public void setExerciseName(String exerciseName) {
		this.exerciseName = exerciseName;
	}

	public Integer getExerciseOrder() {
		return exerciseOrder;
	}

	public void setExerciseOrder(Integer exerciseOrder) {
		this.exerciseOrder = exerciseOrder;
	}

	public Integer getSetNumber() {
		return setNumber;
	}

	public void setSetNumber(Integer setNumber) {
		this.setNumber = setNumber;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Integer getReps() {
		return reps;
	}

	public void setReps(Integer reps) {
		this.reps = reps;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@JsonProperty("isWarmup")
	public boolean isWarmup() {
		return isWarmup;
	}

	@JsonProperty("isWarmup")
	public void setWarmup(boolean warmup) {
		this.isWarmup = warmup;
	}

	@JsonProperty("isCompleted")
	public boolean isCompleted() {
		return isCompleted;
	}

	@JsonProperty("isCompleted")
	public void setCompleted(boolean completed) {
		this.isCompleted = completed;
	}
}