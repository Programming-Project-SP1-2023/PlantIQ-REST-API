package com.plantiq.plantiqserver.model;

import org.springframework.data.jpa.repository.JpaRepository;

// Use to interact with the database contains all the CRUD methods
public interface AnimalRepository extends JpaRepository<Animal, Long> {
}
