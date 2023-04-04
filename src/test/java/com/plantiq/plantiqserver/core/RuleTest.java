package com.plantiq.plantiqserver.core;

import com.plantiq.plantiqserver.rules.RegistrationRequestRule;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.HashMap;

class RuleTest {
	@Test
	void validate() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		// set parameters
		request.addParameter("firstname", "Brian");
		request.addParameter("surname", "Cusack");
		request.addParameter("email", "bcusack78@gmail.com");
		request.addParameter("password", "Admin1234!");

		RegistrationRequestRule rule = new RegistrationRequestRule();
		rule.validate(request);
		assert (rule.getErrors().size() == 0);
	}

	@Test
	void getErrors() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		// set parameters
		request.addParameter("firstname", "Brian");
		request.addParameter("surname", "");
		request.addParameter("email", "");
		request.addParameter("password", "Admin1234!");

		RegistrationRequestRule rule = new RegistrationRequestRule();
		rule.validate(request);
		HashMap<String, Object> errors = rule.getErrors();
		assert (errors.size() == 2);
	}
}