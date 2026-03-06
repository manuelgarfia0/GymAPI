package com.manuel.gym_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.manuel.gym_api.model.Muscle;

public interface MuscleRepository extends JpaRepository<Muscle, Long> {

}