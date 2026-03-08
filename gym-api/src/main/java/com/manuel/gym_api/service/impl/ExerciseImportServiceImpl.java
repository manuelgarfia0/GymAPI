package com.manuel.gym_api.service.impl;

import org.springframework.stereotype.Service;

import com.manuel.gym_api.integration.WgerClient;
import com.manuel.gym_api.service.ExerciseImportService;

@Service
public class ExerciseImportServiceImpl implements ExerciseImportService {

	private final WgerClient wgerClient;

	public ExerciseImportServiceImpl(WgerClient wgerClient) {
		this.wgerClient = wgerClient;
	}

	@Override
	public void importExercises() {

		String exercisesJson = wgerClient.getExercises();

		System.out.println(exercisesJson);
	}
}