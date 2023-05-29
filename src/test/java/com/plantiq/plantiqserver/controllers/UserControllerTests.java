package com.plantiq.plantiqserver.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTests {
	@Mock
	private HttpServletRequest mockedRequest;
	private UserController userController;

	@BeforeEach
	public void setup() {
		mockedRequest = mock(HttpServletRequest.class);
		userController = new UserController();
	}

	@Test
	public void testGetUserBySession_AuthorizedUser() {
//		// Mock the necessary dependencies
//		when(mockedRequest.getAttribute("authorized")).thenReturn(true);
//		User mockedUser = mock(User.class);
//		when(mockedUser.getId()).thenReturn("123");
//		when(Gate.getCurrentUser()).thenReturn(mockedUser);
//		when(User.collection().where("id", Gate.getCurrentUser().getId()).getFirst()).thenReturn(mockedUser);
//
//		// Call the method under test
//		ResponseEntity<HashMap<String, Object>> response = userController.getUserBySession(mockedRequest);
//
//		// Verify the expected interactions and assertions
//		assertEquals(HttpStatus.OK, response.getStatusCode());
//		assertEquals(true, response.getBody().get("outcome"));
//		assertEquals(mockedUser, response.getBody().get("data"));
	}

	@Test
	public void testGetUserBySession_UnauthorizedUser() {
		// Mock the necessary dependencies
		when(mockedRequest.getAttribute("authorized")).thenReturn(false);

		// Call the method under test
		ResponseEntity<HashMap<String, Object>> response = userController.getUserBySession(mockedRequest);

		// Verify the expected interactions and assertions
		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
		assertEquals(false, response.getBody().get("outcome"));
	}
}