package com.plantiq.plantiqserver.controller.AnimalController;

import com.plantiq.plantiqserver.model.Animal;
import com.plantiq.plantiqserver.service.AnimalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/animal")
public class AnimalController {
	AnimalService animalService;

	public AnimalController(AnimalService animalService) {
		this.animalService = animalService;
	}

	/**
	 * Get all animals from the service -> repository.
	 *
	 * @return List of animals from the repository Iterable
	 */
	@GetMapping("/all")
	public List<Animal> getAnimal() {
		return (List<Animal>) animalService.getAll();
	}

	/**
	 * Get an animal by id from the service -> repository.
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public Animal getAnimalById(@PathVariable String id) {
		Optional<Animal> animal = animalService.get(Long.parseLong(id));
		return animal.orElse(null);
	}

	/**
	 * Create a new animal from json data in the request body.
	 *
	 * @param animal
	 * @return
	 */
	@PutMapping("/")
	public ResponseEntity<Animal> updateAnimal(@RequestBody Animal animal) {
		Animal updatedAnimal = animalService.save(animal);
		return ResponseEntity.ok(updatedAnimal);
	}
}
