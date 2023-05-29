package com.plantiq.plantiqserver.secuirty;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SqlSecurityTest {
	@BeforeAll
	public static void initialize() {
		SqlSecurity.initialize();
	}

	@Test
	public void testSanitize() {
		String input = "SELECT * FROM users WHERE id = 1;";
		String sanitizedInput = SqlSecurity.sanitize(input);
		Assertions.assertEquals("users  id  1;", sanitizedInput);
	}

	@Test
	public void testSanitizeWithNoBannedWords() {
		String input = "This is a safe input without any banned words or symbols.";
		String sanitizedInput = SqlSecurity.sanitize(input);
		Assertions.assertEquals(input, sanitizedInput);
	}

	@Test
	public void testSanitizeWithEmptyInput() {
		String input = "";
		String sanitizedInput = SqlSecurity.sanitize(input);
		Assertions.assertEquals("", sanitizedInput);
	}
}