package com.manuel.gym_api.dto;

public class RoutineExerciseDTO {
	private Long id;
	private Long exerciseId;
	private String exerciseName;
	private Integer orderIndex;
	private Integer sets;
	private Integer reps;
	private Integer restSeconds;
	private String notes;

	public RoutineExerciseDTO() {
	}

	public RoutineExerciseDTO(Long id, Long exerciseId, String exerciseName, Integer orderIndex, Integer sets,
			Integer reps, Integer restSeconds, String notes) {
		this.id = id;
		this.exerciseId = exerciseId;
		this.exerciseName = exerciseName;
		this.orderIndex = orderIndex;
		this.sets = sets;
		this.reps = reps;
		this.restSeconds = restSeconds;
		this.notes = notes;
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

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

	public Integer getSets() {
		return sets;
	}

	public void setSets(Integer sets) {
		this.sets = sets;
	}

	public Integer getReps() {
		return reps;
	}

	public void setReps(Integer reps) {
		this.reps = reps;
	}

	public Integer getRestSeconds() {
		return restSeconds;
	}

	public void setRestSeconds(Integer restSeconds) {
		this.restSeconds = restSeconds;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}