package com.plantiq.plantiqserver.controller.AnimalController;

import com.plantiq.plantiqserver.model.Animal;
import com.plantiq.plantiqserver.service.AnimalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class AnimalController {
	AnimalService animalService;

	public AnimalController(AnimalService animalService) {
		this.animalService = animalService;
	}

	@GetMapping("/animal/all")
	public List<Animal> getAnimal() {
		List<Animal> animals = (List<Animal>) animalService.getAll();
		return animals;
	}

	@GetMapping("/animal/{id}")
	public Animal getAnimalById(@PathVariable String id) {
		Optional<Animal> animal = animalService.get(Long.parseLong(id));
		if (animal.isPresent()) {
			return animal.get();
		}
		return null;
	}

	//	user send name and type and age to the server
	@PutMapping("/animal")
	public ResponseEntity<Animal> updateAnimal(@RequestBody Animal animal) {
		Animal updatedAnimal = animalService.save(animal);
		return ResponseEntity.ok(updatedAnimal);
	}
}
