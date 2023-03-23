package com.plantiq.plantiqserver.controller.AnimalController;

import com.plantiq.plantiqserver.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

// Use to interact with the database contains all the CRUD methods
public interface AnimalRepository extends JpaRepository<Animal, Long> {
	// Get animal by name
	@Query("SELECT a FROM Animal a WHERE a.name = ?1")
	public Animal findByName(String name);
}

