package com.manuel.gym_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manuel.gym_api.model.Equipment;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

	Optional<Equipment> findByNameIgnoreCase(String name);

}