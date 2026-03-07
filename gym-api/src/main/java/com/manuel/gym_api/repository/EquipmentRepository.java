package com.manuel.gym_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.manuel.gym_api.model.Equipment;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

}