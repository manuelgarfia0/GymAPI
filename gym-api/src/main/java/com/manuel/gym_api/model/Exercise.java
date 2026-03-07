package com.manuel.gym_api.model;

import jakarta.persistence.*;

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

	@ManyToOne
	@JoinColumn(name = "secondary_muscle_id")
	private Muscle secondaryMuscle;

	private boolean systemExercise;

	private Long userId;

	public Exercise() {
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public Muscle getPrimaryMuscle() {
		return primaryMuscle;
	}

	public Muscle getSecondaryMuscle() {
		return secondaryMuscle;
	}

	public boolean isSystemExercise() {
		return systemExercise;
	}

	public Long getUserId() {
		return userId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public void setPrimaryMuscle(Muscle primaryMuscle) {
		this.primaryMuscle = primaryMuscle;
	}

	public void setSecondaryMuscle(Muscle secondaryMuscle) {
		this.secondaryMuscle = secondaryMuscle;
	}

	public void setSystemExercise(boolean systemExercise) {
		this.systemExercise = systemExercise;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}