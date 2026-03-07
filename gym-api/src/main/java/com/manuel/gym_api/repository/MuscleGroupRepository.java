package com.manuel.gym_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.manuel.gym_api.model.MuscleGroup;

public interface MuscleGroupRepository extends JpaRepository<MuscleGroup, Long> {

}