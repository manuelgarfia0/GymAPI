package com.manuel.gym_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class WorkoutSetDTO {

	private Long id;

	@NotNull(message = "Exercise ID is required")
	private Long exerciseId;

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

	private boolean isWarmup;
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

	public boolean isWarmup() {
		return isWarmup;
	}

	public void setWarmup(boolean warmup) {
		this.isWarmup = warmup;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean completed) {
		this.isCompleted = completed;
	}
}