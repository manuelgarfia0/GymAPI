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

	public Muscle() {
	}

	public Muscle(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}