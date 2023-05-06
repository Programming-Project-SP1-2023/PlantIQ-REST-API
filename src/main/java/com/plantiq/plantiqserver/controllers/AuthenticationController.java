package com.plantiq.plantiqserver.controllers;

import com.plantiq.plantiqserver.PlantIqServerApplication;
import com.plantiq.plantiqserver.core.Email;
import com.plantiq.plantiqserver.model.PasswordResetToken;
import com.plantiq.plantiqserver.model.Session;
import com.plantiq.plantiqserver.model.User;
import com.plantiq.plantiqserver.rules.*;
import com.plantiq.plantiqserver.service.HashService;
import com.plantiq.plantiqserver.service.SessionService;
import com.plantiq.plantiqserver.service.TimeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	@PostMapping("/login")
	public ResponseEntity<HashMap<String, Object>> login(HttpServletRequest request) {

		//Create our response object, this is returned as JSON.
		HashMap<String, Object> response = new HashMap<>();

		//Create our login request rule, we will validate our request
		//using this rule.
		LoginUserRule rule = new LoginUserRule();

		//Validate our request and return the errors if present.
		if (!rule.validate(request)) {
			return rule.abort();
		}

		//Else if we reach this stage of the code then proceed to
		//lookup the user from the database.
		User user = User.collection().where("email", request.getParameter("email")).where("password", HashService.generateSHA1(PlantIqServerApplication.passwordPepper+request.getParameter("password"))).getFirst();
		int status;

		if (user != null && user.isActivated()) {
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
		ValidateSessionRule rule = new ValidateSessionRule();

		if (!rule.validate(request)) {
			return rule.abort();
		}

		Session session = Session.collection().where("token", request.getParameter("token")).orderBy("token").getFirst();
		session.delete("token");

		response.put("outcome", true);

		return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
	}

	@GetMapping("/validate/{token}")
	public ResponseEntity<HashMap<String, Object>> validate(@PathVariable("token") String token) {

		HashMap<String, Object> response = new HashMap<>();

		Session session = Session.collection().where("token", token).orderBy("token").getFirst();

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
		RegisterUserRule rule = new RegisterUserRule();

		if (!rule.validate(request)) {
			return rule.abort();
		}

		HashMap<String, Object> data = new HashMap<>();

		data.put("id", HashService.generateSHA1(TimeService.now().toString() + request.getParameter("email")));
		data.put("email", request.getParameter("email"));
		data.put("firstname", request.getParameter("firstname"));
		data.put("surname", request.getParameter("surname"));
		data.put("password", HashService.generateSHA1(PlantIqServerApplication.passwordPepper+request.getParameter("password")));
		data.put("isAdministrator", "0");
		data.put("registrationDate", TimeService.now().toString());
		data.put("isActivated","0");

		int outcome;

		if (User.insert("User", data)) {
			response.put("outcome", true);
			response.put("message", "User successfully registered and awaiting activation");
			outcome = 200;

			Email email = new Email();

			email.setUser(new User(data))
					.setHtmlTemplate("/emails/userRegistrationConfirmation.html")
					.setSubject("Activate Account")
					.setVariables(data)
					.send();

		} else {
			response.put("outcome", false);
			response.put("errors", "Failed to register user, please contact support!");
			outcome = 500;
		}

		return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));
	}

	@GetMapping("/activate/{token}")
	public ResponseEntity<HashMap<String, Object>> activateAccount(@PathVariable("token") String token){
		HashMap<String, Object> response = new HashMap<>();

		User user = User.collection().where("id",token).where("isActivated","0").getFirst();

		if(user == null){

			response.put("outcome",false);
			response.put("message","Account not found or already activated");

			return new ResponseEntity<>(response,HttpStatusCode.valueOf(404));
		}

		HashMap<String,Object> data = new HashMap<>();
		data.put("isActivated","1");

		int outcome;
		if(user.update(data)){
			response.put("message","Account successfully activated, please login");
			response.put("outcome",true);
			outcome = 200;
		}else{
			response.put("message","Failed to activate account, please contact support");
			response.put("outcome",true);
			outcome = 500;
		}

		return new ResponseEntity<>(response,HttpStatusCode.valueOf(outcome));
	}

	@PostMapping("/reset")
	public ResponseEntity<HashMap<String, Object>> generateResetToken(HttpServletRequest request){
		HashMap<String,Object> response = new HashMap<>();

		PasswordResetRule rule = new PasswordResetRule();

		if(!rule.validate(request)){
			return rule.abort();
		}

		User user = User.collection().where("email",request.getParameter("email")).getFirst();

		if(user == null){
			response.put("outcome",true);
			response.put("message","If an email address exists for that account you will receive an email shortly.");
			return new ResponseEntity<>(response,HttpStatusCode.valueOf(200));
		}

		String data = request.getParameter("email")+TimeService.now();

		HashMap<String, Object> token= new HashMap<>();

		token.put("token",HashService.generateSHA1(data));
		token.put("email",user.getEmail());
		token.put("expirationDate",TimeService.nowPlusDays(1).toString());
		token.put("createdDate",TimeService.now().toString());

		int outcome;
		if(PasswordResetToken.insert("passwordResetToken",token)){

			response.put("outcome",true);
			outcome = 200;
			response.put("message","If an email address exists for that account you will receive an email shortly.");

			//Send email
			Email email = new Email();
			email.setSubject("Account Recovery")
					.setUser(user)
					.setVariables(token)
					.setHtmlTemplate("/emails/passwordResetConfirmation.html")
					.send();

		}else{
			response.put("outcome",false);
			outcome = 500;
			response.put("message","Failed to perform action, please contact support.");
		}

		return new ResponseEntity<>(response,HttpStatusCode.valueOf(outcome));
	}

	@GetMapping("/reset/{token}")
	public ResponseEntity<HashMap<String,Object>> validateResetToken(@PathVariable("token") String token){
		HashMap<String,Object> response = new HashMap<>();

		PasswordResetToken token1 = PasswordResetToken.collection().where("token",token).orderBy("token").getFirst();

		int outcome;
		if(token1 == null){
			response.put("message","Password reset token not found or expired");
			response.put("outcome",false);
			outcome = 404;
		}else{
			if(TimeService.now() > token1.expirationDate()){
				token1.delete("token");
				response.put("message","Password reset token not found or expired");
				response.put("outcome",false);
				outcome = 404;
			}else{
				response.put("message","Valid password reset token");
				response.put("outcome",true);
				outcome = 200;
			}

		}
		return new ResponseEntity<>(response,HttpStatusCode.valueOf(outcome));
	}

	@PatchMapping("/reset")
	public ResponseEntity<HashMap<String,Object>> consumePasswordResetToken(HttpServletRequest request){
		HashMap<String,Object> response = new HashMap<>();

		//Validate our HTTP request
		ConsumePasswordResetTokenRule rule = new ConsumePasswordResetTokenRule();

		if(!rule.validate(request)){
			return rule.abort();
		}

		//Get our password reset token from the database
		PasswordResetToken passwordResetToken = PasswordResetToken.collection().where("token",request.getParameter("token")).orderBy("token").getFirst();

		int outcome;

		if(passwordResetToken == null){
			outcome = 404;
			response.put("outcome",false);
			response.put("error","Password reset token not found or expired");
			return new ResponseEntity<>(response,HttpStatusCode.valueOf(outcome));
		}
		
		//Check the token expiration date is valid and not expired
		if(TimeService.now() > passwordResetToken.expirationDate()){

			passwordResetToken.delete("token");
			outcome = 404;
			response.put("outcome",false);
			response.put("error","Password reset token not found or expired");
			return new ResponseEntity<>(response,HttpStatusCode.valueOf(outcome));
		}
		
		//else if we reach this stage we reset the password

		User user = User.collection().where("email",passwordResetToken.email()).getFirst();

		HashMap<String, Object> data = new HashMap<>();
		data.put("password",HashService.generateSHA1(PlantIqServerApplication.passwordPepper+request.getParameter("password")));

		if(user.update(data)){
			outcome = 200;
			response.put("outcome",true);
			response.put("message","Password reset");
			passwordResetToken.delete("token");
		}else{
			outcome = 500;
			response.put("outcome",false);
			response.put("message","Failed to reset password, please contact support");
		}

		return new ResponseEntity<>(response,HttpStatusCode.valueOf(outcome));
	}
}
