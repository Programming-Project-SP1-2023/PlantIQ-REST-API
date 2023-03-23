package com.plantiq.plantiqserver.controller.AnimalController;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("dev")
class AnimalControllerTest {
	@Autowired
	private AnimalController animalController;

	@Test
	public void contextLoads() {
		assertThat(animalController).isNotNull();
	}
}