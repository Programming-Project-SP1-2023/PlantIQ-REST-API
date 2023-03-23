package com.plantiq.plantiqserver.controller.AnimalController;

import com.plantiq.plantiqserver.model.Animal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnimalControllerTest {
	@BeforeEach
	void setUp() {
		// Create 3x Animals to test with
		Animal animal1 = new Animal("Bobby The Cat", "Dog", 15);
		Animal animal2 = new Animal("Terry", "Dog", 2);
		Animal animal3 = new Animal("Kevin", "Cat", 5);
	}

	@Test
	void getAnimal() {
	}

	@Test
	void getAnimalById() {
	}

	// Test to add the three animals to the H2 database
	@Test
	void updateAnimal() {

	}
}