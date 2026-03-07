package com.manuel.gym_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "muscles")
public class Muscle {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@ManyToOne
	@JoinColumn(name = "muscle_group_id")
	private MuscleGroup muscleGroup;

	public Muscle() {
	}

	public Muscle(String name, MuscleGroup muscleGroup) {
		this.name = name;
		this.muscleGroup = muscleGroup;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public MuscleGroup getMuscleGroup() {
		return muscleGroup;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMuscleGroup(MuscleGroup muscleGroup) {
		this.muscleGroup = muscleGroup;
	}
}