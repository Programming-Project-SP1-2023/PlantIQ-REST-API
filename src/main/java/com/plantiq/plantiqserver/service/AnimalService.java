package com.plantiq.plantiqserver.service;

import com.plantiq.plantiqserver.controller.AnimalController.AnimalRepository;
import com.plantiq.plantiqserver.model.Animal;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Use to offload business logic from the controller.
 * Interacts with the repository to get data from the database.
 */
@Service
public class AnimalService {
	private final AnimalRepository animalRepository;

	public AnimalService(AnimalRepository animalRepository) {
		this.animalRepository = animalRepository;
	}

	public Optional<Animal> get(Long id) {
		return animalRepository.findById(id);
	}

	public Animal save(Animal animal) {
		return animalRepository.save(animal);
	}

	public void delete(Long id) {
		animalRepository.deleteById(id);
	}

	public Iterable<Animal> getAll() {
		return animalRepository.findAll();
	}
}
