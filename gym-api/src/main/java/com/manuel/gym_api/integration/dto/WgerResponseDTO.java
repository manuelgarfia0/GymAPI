package com.manuel.gym_api.integration.dto;

import java.util.List;

public class WgerResponseDTO {
	private List<WgerExerciseDTO> results;

	public List<WgerExerciseDTO> getResults() {
		return results;
	}

	public void setResults(List<WgerExerciseDTO> results) {
		this.results = results;
	}
}