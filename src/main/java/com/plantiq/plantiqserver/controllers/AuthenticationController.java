package com.plantiq.plantiqserver.controllers;

import com.plantiq.plantiqserver.model.Session;
import com.plantiq.plantiqserver.model.User;
import com.plantiq.plantiqserver.rules.LoginRequestRule;
import com.plantiq.plantiqserver.rules.RegistrationRequestRule;
import com.plantiq.plantiqserver.rules.SessionValidateRequestRule;
import com.plantiq.plantiqserver.service.HashService;
import com.plantiq.plantiqserver.service.SessionService;
import com.plantiq.plantiqserver.service.TimeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/auth")
public class AuthenticationController {
	@PostMapping("/login")
	public ResponseEntity<HashMap<String, Object>> login(HttpServletRequest request) {

		//Create our response object, this is returned as JSON.
		HashMap<String, Object> response = new HashMap<>();

		//Create our login request rule, we will validate our request
		//using this rule.
		LoginRequestRule rule = new LoginRequestRule();

		//Validate our request and return the errors if present.
		if (!rule.validate(request)) {
			return rule.abort();
		}

		//Else if we reach this stage of the code then proceed to
		//lookup the user from the database.
		User user = User.collection().where("email", request.getParameter("email")).where("password", request.getParameter("password")).getFirst();
		int status;

		if (user != null) {
			response.put("outcome", "true");
			response.put("session", SessionService.create(user.getId()));
			status = 200;
		} else {
			response.put("outcome", "false");
			response.put("errors", "Login failed, please check your email and password");
			status = 401;
		}

		return new ResponseEntity<>(response, HttpStatusCode.valueOf(status));
	}

	@DeleteMapping("/logout")
	public ResponseEntity<HashMap<String, Object>> logout(HttpServletRequest request) {

		HashMap<String, Object> response = new HashMap<>();
		SessionValidateRequestRule rule = new SessionValidateRequestRule();

		if (!rule.validate(request)) {
			return rule.abort();
		}

		Session session = Session.collection().where("token", request.getParameter("token")).getFirst();
		session.delete("token");

		response.put("outcome", true);

		return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
	}

	@GetMapping("/validate/{token}")
	public ResponseEntity<HashMap<String, Object>> validate(@PathVariable("token") String token) {

		HashMap<String, Object> response = new HashMap<>();

		Session session = Session.collection().where("token", token).getFirst();

		if (session == null) {
			response.put("outcome", false);
			response.put("errors", "Invalid token provided!");
			return new ResponseEntity<>(response, HttpStatusCode.valueOf(401));
		}
		int outcome;

		if (session.getExpiry() < TimeService.now()) {
			session.delete("token");
			response.put("outcome", false);
			outcome = 401;
		} else {
			response.put("outcome", true);
			outcome = 200;
		}

		return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));
	}

	@PostMapping("/register")
	public  ResponseEntity<HashMap<String, Object>> register(HttpServletRequest request) {
		HashMap<String, Object> response = new HashMap<>();
		RegistrationRequestRule rule = new RegistrationRequestRule();

		if (!rule.validate(request)) {
			return rule.abort();
		}

		HashMap<String, Object> data = new HashMap<>();

		data.put("id", HashService.generateSHA1(TimeService.now().toString() + request.getParameter("email")));
		data.put("email", request.getParameter("email"));
		data.put("firstname", request.getParameter("firstname"));
		data.put("surname", request.getParameter("surname"));
		data.put("password", request.getParameter("password"));
		data.put("isAdministrator", "0");
		data.put("registrationDate", TimeService.now().toString());

		int outcome;

		if (User.insert("User", data)) {
			response.put("outcome", true);
			response.put("message", "User successfully registered");
			outcome = 200;
		} else {
			response.put("outcome", false);
			response.put("errors", "Failed to register user, please contact support!");
			outcome = 500;
		}

		return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));
	}
}
