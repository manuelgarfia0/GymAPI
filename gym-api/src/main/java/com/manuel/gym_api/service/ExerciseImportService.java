package com.manuel.gym_api.service;

import org.springframework.stereotype.Service;

import com.manuel.gym_api.integration.WgerClient;

@Service
public class ExerciseImportService {

	private final WgerClient wgerClient;

	public ExerciseImportService(WgerClient wgerClient) {
		this.wgerClient = wgerClient;
	}

	public void importExercises() {

		String exercisesJson = wgerClient.getExercises();

		System.out.println(exercisesJson);
	}
}