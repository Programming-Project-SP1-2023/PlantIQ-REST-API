package com.plantiq.plantiqserver.controller.AnimalController;

import com.plantiq.plantiqserver.model.Animal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnimalController {
	@GetMapping("/animal")
	public Animal getAnimal() {
		Animal animal = new Animal("Molly", "Dog", 15);
		return animal;
	}
}
