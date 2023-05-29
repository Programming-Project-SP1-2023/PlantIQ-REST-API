package com.plantiq.plantiqserver.service;

import org.junit.jupiter.api.Test;

import java.util.Date;

class TimeServiceTest {
	@Test
	void nowPlusDays() {
		Long now = TimeService.now();
		Long nowPlusOneDay = TimeService.nowPlusDays(1);
		assert (nowPlusOneDay - now) == 86400;
	}

	@Test
	void nowMinusDays() {
		// Arrange
		int offset = 3; // Number of days to subtract
		long currentTimeInSeconds = TimeService.now();
		long expectedTimeInSeconds = currentTimeInSeconds - offset * (24 * 3600);

		// Act
		long actualTimeInSeconds = TimeService.nowMinusDays(offset);

		// Assert
		assert (expectedTimeInSeconds == actualTimeInSeconds);
	}

	@Test
	void now() {
		// Arrange
		long expectedTimeInSeconds = System.currentTimeMillis() / 1000L;
		// Act
		long millis = System.currentTimeMillis();
		Date date = new Date(millis);
		// Assert
		assert (expectedTimeInSeconds == date.getTime() / 1000L);
	}
}