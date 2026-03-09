package com.manuel.gym_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.manuel.gym_api.model.WorkoutSet;

@Repository
public interface WorkoutSetRepository extends JpaRepository<WorkoutSet, Long> {
}