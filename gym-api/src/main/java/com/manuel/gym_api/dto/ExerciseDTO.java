package com.manuel.gym_api.dto;

import java.util.List;

public class ExerciseDTO {

	private Long id;
	private String name;
	private String description;

	private Long primaryMuscleId;
	private String primaryMuscleName;

	private String equipmentName;

	private List<Long> secondaryMuscleIds;
	private List<String> secondaryMuscleNames;

	public ExerciseDTO() {
	}

	public ExerciseDTO(Long id, String name, String description, Long primaryMuscleId, String primaryMuscleName,
			String equipmentName, List<Long> secondaryMuscleIds, List<String> secondaryMuscleNames) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.primaryMuscleId = primaryMuscleId;
		this.primaryMuscleName = primaryMuscleName;
		this.equipmentName = equipmentName;
		this.secondaryMuscleIds = secondaryMuscleIds;
		this.secondaryMuscleNames = secondaryMuscleNames;
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

	public Long getPrimaryMuscleId() {
		return primaryMuscleId;
	}

	public void setPrimaryMuscleId(Long primaryMuscleId) {
		this.primaryMuscleId = primaryMuscleId;
	}

	public String getPrimaryMuscleName() {
		return primaryMuscleName;
	}

	public void setPrimaryMuscleName(String primaryMuscleName) {
		this.primaryMuscleName = primaryMuscleName;
	}

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	public List<Long> getSecondaryMuscleIds() {
		return secondaryMuscleIds;
	}

	public void setSecondaryMuscleIds(List<Long> secondaryMuscleIds) {
		this.secondaryMuscleIds = secondaryMuscleIds;
	}

	public List<String> getSecondaryMuscleNames() {
		return secondaryMuscleNames;
	}

	public void setSecondaryMuscleNames(List<String> secondaryMuscleNames) {
		this.secondaryMuscleNames = secondaryMuscleNames;
	}
}