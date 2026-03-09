package com.manuel.gym_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manuel.gym_api.model.Routine;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
	List<Routine> findByUserId(Long userId);
}