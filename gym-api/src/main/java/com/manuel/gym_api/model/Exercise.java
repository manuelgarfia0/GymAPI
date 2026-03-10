package com.manuel.gym_api.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "exercises")
public class Exercise {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(length = 2000)
	private String description;

	@ManyToOne
	@JoinColumn(name = "equipment_id")
	private Equipment equipment;

	@ManyToOne
	@JoinColumn(name = "primary_muscle_id")
	private Muscle primaryMuscle;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "exercise_secondary_muscles", joinColumns = @JoinColumn(name = "exercise_id"), inverseJoinColumns = @JoinColumn(name = "muscle_id"))
	private Set<Muscle> secondaryMuscles = new HashSet<>();

	@Column(name = "system_exercise", nullable = false)
	private boolean systemExercise = false;

	@Column(name = "user_id")
	private Long userId;

	public Exercise() {
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

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public Muscle getPrimaryMuscle() {
		return primaryMuscle;
	}

	public void setPrimaryMuscle(Muscle primaryMuscle) {
		this.primaryMuscle = primaryMuscle;
	}

	public Set<Muscle> getSecondaryMuscles() {
		return secondaryMuscles;
	}

	public void setSecondaryMuscles(Set<Muscle> secondaryMuscles) {
		this.secondaryMuscles = secondaryMuscles;
	}

	public boolean isSystemExercise() {
		return systemExercise;
	}

	public void setSystemExercise(boolean systemExercise) {
		this.systemExercise = systemExercise;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}