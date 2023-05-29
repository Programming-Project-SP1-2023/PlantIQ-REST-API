package com.plantiq.plantiqserver.rules;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class LoginUserRuleTest {
	private LoginUserRule loginUserRule;

	@BeforeEach
	public void setup() {
		loginUserRule = new LoginUserRule();
		loginUserRule.setup();
	}

	@Test
	public void testEmailRules() {
		List<String> emailRules = loginUserRule.getRules().get("email");
		Assertions.assertNotNull(emailRules);
		Assertions.assertEquals(2, emailRules.size());
		Assertions.assertTrue(emailRules.contains("required"));
		Assertions.assertTrue(emailRules.contains("regex:email"));
	}

	@Test
	public void testPasswordRules() {
		List<String> passwordRules = loginUserRule.getRules().get("password");
		Assertions.assertNotNull(passwordRules);
		Assertions.assertEquals(3, passwordRules.size());
		Assertions.assertTrue(passwordRules.contains("required"));
		Assertions.assertTrue(passwordRules.contains("min:8"));
		Assertions.assertTrue(passwordRules.contains("max:25"));
	}
}