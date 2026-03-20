package com.manuel.gym_api;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.manuel.gym_api.service.ExerciseImportService;

@Component
public class DataInitializer implements ApplicationRunner {

	private final ExerciseImportService exerciseImportService;

	public DataInitializer(ExerciseImportService exerciseImportService) {
		this.exerciseImportService = exerciseImportService;
	}

	@Override
	public void run(ApplicationArguments args) {
		exerciseImportService.importExercises();
	}

}