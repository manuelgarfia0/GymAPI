package com.manuel.gym_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manuel.gym_api.model.Exercise;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
}