package com.manuel.gym_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.manuel.gym_api.model.Exercise;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

	boolean existsByNameIgnoreCase(String name);

	List<Exercise> findByNameContainingIgnoreCase(String name);
}