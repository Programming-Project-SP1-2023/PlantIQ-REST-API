package com.plantiq.plantiqserver.rules;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class RegisterUserRuleTest {
	private RegisterUserRule registerUserRule;

	@BeforeEach
	public void setup() {
		registerUserRule = new RegisterUserRule();
		registerUserRule.setup();
	}

	@Test
	public void testEmailRules() {
		List<String> emailRules = registerUserRule.getRules().get("email");
		Assertions.assertNotNull(emailRules);
		Assertions.assertEquals(3, emailRules.size());
		Assertions.assertTrue(emailRules.contains("required"));
		Assertions.assertTrue(emailRules.contains("regex:email"));
		Assertions.assertTrue(emailRules.contains("unique:User.email"));
	}

	@Test
	public void testFirstnameRules() {
		List<String> firstnameRules = registerUserRule.getRules().get("firstname");
		Assertions.assertNotNull(firstnameRules);
		Assertions.assertEquals(3, firstnameRules.size());
		Assertions.assertTrue(firstnameRules.contains("required"));
		Assertions.assertTrue(firstnameRules.contains("min:3"));
		Assertions.assertTrue(firstnameRules.contains("max:25"));
	}

	@Test
	public void testSurnameRules() {
		List<String> surnameRules = registerUserRule.getRules().get("surname");
		Assertions.assertNotNull(surnameRules);
		Assertions.assertEquals(3, surnameRules.size());
		Assertions.assertTrue(surnameRules.contains("required"));
		Assertions.assertTrue(surnameRules.contains("min:2"));
		Assertions.assertTrue(surnameRules.contains("max:25"));
	}

	@Test
	public void testPasswordRules() {
		List<String> passwordRules = registerUserRule.getRules().get("password");
		Assertions.assertNotNull(passwordRules);
		Assertions.assertEquals(3, passwordRules.size());
		Assertions.assertTrue(passwordRules.contains("required"));
		Assertions.assertTrue(passwordRules.contains("min:8"));
		Assertions.assertTrue(passwordRules.contains("max:50"));
	}
}