package com.manuel.gym_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manuel.gym_api.model.RoutineExercise;

public interface RoutineExerciseRepository extends JpaRepository<RoutineExercise, Long> {
	List<RoutineExercise> findByRoutineIdOrderByOrderIndexAsc(Long routineId);
}