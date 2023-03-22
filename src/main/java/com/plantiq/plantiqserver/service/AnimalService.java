package com.plantiq.plantiqserver.service;

import com.plantiq.plantiqserver.model.Animal;
import com.plantiq.plantiqserver.model.AnimalRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
