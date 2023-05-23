package com.plantiq.plantiqserver.service;

import org.junit.jupiter.api.Test;

class HashServiceTest {
	@Test
	void testGenerateSHA1() {
		// Arrange
		String inputData = "Cats are cool";
		String expectedHash = "714fed480b7e3d5fcde01aa509cf69ca38519311";

		// Act
		String actualHash = HashService.generateSHA1(inputData);

		// Assert
		assert (expectedHash.equals(actualHash));
	}
}