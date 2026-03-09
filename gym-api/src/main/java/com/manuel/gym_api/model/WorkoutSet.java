package com.manuel.gym_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "workout_sets")
public class WorkoutSet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "workout_id", nullable = false)
	private Workout workout;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "exercise_id", nullable = false)
	private Exercise exercise;

	@Column(name = "exercise_order", nullable = false)
	private Integer exerciseOrder; // Orden del ejercicio dentro del entrenamiento (1, 2, 3...)

	@Column(name = "set_number", nullable = false)
	private Integer setNumber; // Número de serie para este ejercicio (1, 2, 3...)

	@Column(nullable = false)
	private Double weight; // Kilogramos o Libras

	@Column(nullable = false)
	private Integer reps; // Repeticiones reales conseguidas

	@Column(name = "is_warmup", nullable = false)
	private boolean isWarmup = false; // Para marcar series de calentamiento

	@Column(name = "is_completed", nullable = false)
	private boolean isCompleted = false; // En apps como Hevy, marcas un "tick" cuando terminas la serie

	public WorkoutSet() {
	}

	// Getters y Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Workout getWorkout() {
		return workout;
	}

	public void setWorkout(Workout workout) {
		this.workout = workout;
	}

	public Exercise getExercise() {
		return exercise;
	}

	public void setExercise(Exercise exercise) {
		this.exercise = exercise;
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
		isWarmup = warmup;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean completed) {
		isCompleted = completed;
	}
}