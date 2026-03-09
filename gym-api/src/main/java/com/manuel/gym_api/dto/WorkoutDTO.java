package com.manuel.gym_api.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class WorkoutDTO {

	private Long id;

	@NotBlank(message = "Workout name is required")
	private String name;

	private String notes;

	@NotNull(message = "Start time is required")
	private LocalDateTime startTime;

	private LocalDateTime endTime;

	@NotNull(message = "User ID is required")
	private Long userId;

	private Long routineId; // Puede ser nulo si es un entrenamiento libre

	@Valid // Valida que la lista de series cumpla con las reglas del WorkoutSetDTO
	private List<WorkoutSetDTO> sets;

	public WorkoutDTO() {
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

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoutineId() {
		return routineId;
	}

	public void setRoutineId(Long routineId) {
		this.routineId = routineId;
	}

	public List<WorkoutSetDTO> getSets() {
		return sets;
	}

	public void setSets(List<WorkoutSetDTO> sets) {
		this.sets = sets;
	}
}