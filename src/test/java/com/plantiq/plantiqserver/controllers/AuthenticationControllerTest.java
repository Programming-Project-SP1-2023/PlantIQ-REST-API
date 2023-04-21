package com.plantiq.plantiqserver.controllers;

import org.junit.jupiter.api.BeforeEach;

class AuthenticationControllerTest {
	@BeforeEach
	void setUp() {

	}

//	@Test
//	void login() {
//		HttpServletRequest data = new MockHttpServletRequest();
//		data.setAttribute("email", "briancusack78@gmail.com");
//		data.setAttribute("password", "Admin1234!");
//		// post request to /login
//		AuthenticationController controller = new AuthenticationController();
//		HashMap<String, Object> res = controller.login(data);
//		// assert that the response is successful
//		assert res.get("success").equals(true);
//	}
//
//	/**
//	 * Test the registration endpoint.
//	 * User should be able to register with a valid email and password.
//	 */
//	@Test
//	void register() {
//		HttpServletRequest data = new MockHttpServletRequest();
//		data.setAttribute("firstname", "Bill123");
//		data.setAttribute("surname", "Smith");
//		data.setAttribute("email", "bill@gmail.com");
//		data.setAttribute("password", "password1234");
//		// post request to /register
//		AuthenticationController controller = new AuthenticationController();
//		HashMap<String, Object> res = controller.register(data);
//		// assert that the response is successful
//		assert res.get("success").equals(true);
//	}
}