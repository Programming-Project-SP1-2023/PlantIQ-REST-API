package com.plantiq.plantiqserver.service;

import org.junit.jupiter.api.Test;

class SessionServiceTest {
	@Test
	void create() {
		String hash = SessionService.create("test");
		assert hash != null;
	}
}