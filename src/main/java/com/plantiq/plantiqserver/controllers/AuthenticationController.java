package com.plantiq.plantiqserver.controllers;

import com.plantiq.plantiqserver.PlantIqServerApplication;
import com.plantiq.plantiqserver.core.Email;
import com.plantiq.plantiqserver.model.Notification;
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
//---------------------------------Controller Class---------------------------------
//The AuthenticationController class will be serving all the endpoints that deal
//with the authentication of the user.
//----------------------------------------------------------------------------------

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	// The @PostMapping("/login") will allow the user to login into their account.
	// In order to perform this action, the user will need to provide their email and password,
	//which are served to this method as parameters of the html request.
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
		User user = User.collection().where("email", request.getParameter("email")).where("password", HashService.generateSHA1(PlantIqServerApplication.getInstance().getPasswordPepper()+request.getParameter("password"))).getFirst();
		int status;
		//If else statement to check if the user is not null and has an activated account
		//If they are eligible for a session, this will be created and the html status code
		// will be set to 200. If not, an error will be returned and the html status code
		//will be 401
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
	// The @DeleteMapping("/logout") will allow the user to logout from their account.
	// This will lead to the deletion of the session code they were using to stay logged in.
	@DeleteMapping("/logout")
	public ResponseEntity<HashMap<String, Object>> logout(HttpServletRequest request) {
		//Create our response object, this is returned as JSON.
		HashMap<String, Object> response = new HashMap<>();
		//Create our validate session rule. We will validate our request
		//using this rule.
		ValidateSessionRule rule = new ValidateSessionRule();
		//Validate our request and return the errors if present.
		if (!rule.validate(request)) {
			return rule.abort();
		}
		//Search for session which has the same token as the one of the user that is trying to log out.
		Session session = Session.collection().where("token", request.getParameter("token")).orderBy("token").getFirst();
		//Once the session has been found, this will be deleted, logging out the user.
		session.delete("token");
		response.put("outcome", true);

		return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
	}

	//@GetMapping("/validate/{token}") is where the session is validated. If the session is valid the
	//user will be able to continue navigating, otherwise an error will be thrown.
	@GetMapping("/validate/{token}")
	public ResponseEntity<HashMap<String, Object>> validate(@PathVariable("token") String token) {
		//Create our response object, this is returned as JSON.
		HashMap<String, Object> response = new HashMap<>();

		//Retrieving the session from the database where the token matches
		//the token the user is trying to use to access the website.
		Session session = Session.collection().where("token", token).orderBy("token").getFirst();
		//If no session was found, the token must be invalid.
		//A false outcome and an error message will be returned
		if (session == null) {
			response.put("outcome", false);
			response.put("errors", "Invalid token provided!");
			return new ResponseEntity<>(response, HttpStatusCode.valueOf(401));
		}
		//Variable that will store the html status code
		int outcome;
		//If the session found is expired, this session will be deleted from the database.
		//An error message and a false outcome will then be returned and the status code
		//will be set to 401. If the session is not expired, a true outcome will be returned,
		//and the html status code will be 200.
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

	//The @PostMapping("/register") will allow the user to create an account for PlantIQ.
	// In order to perform this action, the user will need to provide their email, password,
	//firstname and surname. These will be passed to the method within the html request, where
	//they will be validated and stored into the database. Finally, it will send the user an email
	//containing an activation link, which must be clicked to successfully activate the account.
	//which are served to this method as parameters of the html request.
	@PostMapping("/register")
	public  ResponseEntity<HashMap<String, Object>> register(HttpServletRequest request) {
		//Create our response object, this is returned as JSON.
		HashMap<String, Object> response = new HashMap<>();
		//Create our register user rule. We will validate our request
		//using this rule.
		RegisterUserRule rule = new RegisterUserRule();
		//Validate our request and return the errors if present.
		if (!rule.validate(request)) {
			return rule.abort();
		}
		//Hashmap that will store attributes and values for the account that
		//is going to be created. This organisation of data will then be used
		//to create a record in the database, successfully creating the account.
		HashMap<String, Object> data = new HashMap<>();
		data.put("id", HashService.generateSHA1(TimeService.now().toString() + request.getParameter("email")));
		data.put("email", request.getParameter("email"));
		data.put("firstname", request.getParameter("firstname"));
		data.put("surname", request.getParameter("surname"));
		data.put("password", HashService.generateSHA1(PlantIqServerApplication.getInstance().getPasswordPepper()+request.getParameter("password")));
		data.put("isAdministrator", "0");
		data.put("registrationDate", TimeService.now().toString());
		data.put("isActivated","0");
		//Variable that will store the html status code
		int outcome;
		//If statement to check if the user record has been successfully
		//added to the database. If so, the response will contain a
		//successful message and will set the html status code to 200.
		//If the query did not affect any the database, the html status
		//code will be set to 500 and an error message will be sent back
		//to the front end.
		if (User.insert("User", data)) {
			response.put("outcome", true);
			response.put("message", "User successfully registered and awaiting activation");
			outcome = 200;

			//The record is now inserted into the database. The final step
			//for the user to successfully use their account is to validate
			//it by clicking the confirmation email. Here we are creating the
			//email with the structure of a user registration confirmation
			//template and sending it to the user.
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

	//@GetMapping("/activate/{token}") is the endpoint the user will hit when they click the activation
	//link they received by email. Within this method the account's record in the database will be updated
	//changing the status of the "isActivated" attribute. The necessary parameter that will need to be present
	//in the html request is the activation token, which is the user's id.
	@GetMapping("/activate/{token}")
	public ResponseEntity<HashMap<String, Object>> activateAccount(@PathVariable("token") String token){
		//Create our response object, this is returned as JSON.
		HashMap<String, Object> response = new HashMap<>();
		//Retrieving the disabled account that has the matching token
		User user = User.collection().where("id",token).where("isActivated","0").getFirst();
		//There could be two reasons for 'user' to be null: the account has already been activated
		//no user with that ID exists within the database. If this is the situation, the method returns
		//a 404 error.
		if(user == null){

			response.put("outcome",false);
			response.put("message","Account not found or already activated");

			return new ResponseEntity<>(response,HttpStatusCode.valueOf(404));
		}
		//If the code reaches this stage, an account has been found with the
		//inputted values. To activate the account, the record needs to be
		//updated in the database. Following is the necessary field and value
		//that requires adjustment.
		HashMap<String,Object> data = new HashMap<>();
		data.put("isActivated","1");
		//Variable that will store the html status code
		int outcome;
		//If the record is correctly updated a successful response will be returned,
		//while a 500 error will be returned if no rows was affected by the query.
		if(user.update(data)){
			response.put("message","Account successfully activated, please login");
			Notification.create(user,"Account successfully activated");
			response.put("outcome",true);
			outcome = 200;
		}else{
			response.put("message","Failed to activate account, please contact support");
			response.put("outcome",true);
			outcome = 500;
		}

		return new ResponseEntity<>(response,HttpStatusCode.valueOf(outcome));
	}

	//The @GetMapping("/activate/{token}") will be called when the user tries to reset their forgotten
	//password. To function, the request will need to contain the parameter "email", which will be
	//used to send the user a password recovery link.
	@PostMapping("/reset")
	public ResponseEntity<HashMap<String, Object>> generateResetToken(HttpServletRequest request){
		//Create our response object, this is returned as JSON.
		HashMap<String,Object> response = new HashMap<>();
		//Create our password reset rule. We will validate our
		//request using this rule.
		PasswordResetRule rule = new PasswordResetRule();
		//Validate our request and return the errors if present.
		if(!rule.validate(request)){
			return rule.abort();
		}
		//Retrieving the user account from the database
		User user = User.collection().where("email",request.getParameter("email")).getFirst();
		//If no user was found with the given email the method will be ended with
		//the return of a message stating that if the email is valid they will be
		//receiving an email. The same message will be returned when there is a match
		//in the database, however there are more actions that come with that.
		if(user == null){
			response.put("outcome",true);
			response.put("message","If an email address exists for that account you will receive an email shortly.");
			return new ResponseEntity<>(response,HttpStatusCode.valueOf(200));
		}
		//Creation of data that will be hashed to obtain the token
		String data = request.getParameter("email")+TimeService.now();

		//The password reset token has various attributes in the database
		//The following hashmap is created to organise the data that will
		//be inserted into the entity.
		HashMap<String, Object> token= new HashMap<>();
		token.put("token",HashService.generateSHA1(data));
		token.put("email",user.getEmail());
		//Password Reset Tokens expire after one day.
		token.put("expirationDate",TimeService.nowPlusDays(1).toString());
		token.put("createdDate",TimeService.now().toString());

		//Variable that will store the html status code.
		int outcome;

		//If the PassowordResetToken data has been successfully inserted into the database,
		//a message stating that if the email is valid they will be receiving an email
		//will be returned, the html status code will be set to 200 and an email will be
		//sent to the user. If no rows have been affected by the query, the html status code
		//will be 500 and an error message will be returned

		if(PasswordResetToken.insert("passwordResetToken",token)){

			response.put("outcome",true);
			outcome = 200;
			response.put("message","If an email address exists for that account you will receive an email shortly.");
			Notification.create(user,"Password reset link sent to your email address!");

			//The record is now inserted into the database. The final step
			//for the user to reset the password of their account is follow
			//the link they will be receiving by email. Here we are creating the
			//email with the structure of a password reset confirmation
			//template and sending it to the user.
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

	//@GetMapping("/reset/{token}") is the endpoint the user will hit when they click the reset password link
	//they received by email. The parameter of this method is the reset token, that is needed to retrieve
	//the password reset token object from the database.
	@GetMapping("/reset/{token}")
	public ResponseEntity<HashMap<String,Object>> validateResetToken(@PathVariable("token") String token){
		//Create our response object, this is returned as JSON.
		HashMap<String,Object> response = new HashMap<>();

		//Retrieve the password reset token object from that database that has the matching token.
		PasswordResetToken token1 = PasswordResetToken.collection().where("token",token).orderBy("token").getFirst();
		//Variable that will store the html status code.
		int outcome;
		//If the token was not found, a 404 error will be returned to the front end
		//with an appropriate message.
		if(token1 == null){
			response.put("message","Password reset token not found or expired");
			response.put("outcome",false);
			outcome = 404;
		}else{
			//If the token was found within the database, an expiry check
			//must be performed to ensure the user is not using an expired token.
			//If the token is expired, this will be deleted from the database,
			//the same 404 error will eb returned. Instead, if the token is valid,
			//the html status code will be set to 200 and a successful message will
			//be returned.
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

	//@PatchMapping("/reset") is the endpoint the user will hit once they are submitting their new password.
	//Within this method, the database will be updated with the new password, so at the next login the user
	//can log in using their new password.
	@PatchMapping("/reset")
	public ResponseEntity<HashMap<String,Object>> consumePasswordResetToken(HttpServletRequest request){
		//Create our response object, this is returned as JSON.
		HashMap<String,Object> response = new HashMap<>();

		//Create our consumption password reset rule. We will
		// validate our request using this rule.
		ConsumePasswordResetTokenRule rule = new ConsumePasswordResetTokenRule();
		//Validate our request and return the errors if present.
		if(!rule.validate(request)){
			return rule.abort();
		}

		//Get our password reset token from the database
		PasswordResetToken passwordResetToken = PasswordResetToken.collection().where("token",request.getParameter("token")).orderBy("token").getFirst();
		//Variable that will store the html status code.
		int outcome;
		//If the token was not found, a 404 error will be returned to the front end
		//with an appropriate message.
		if(passwordResetToken == null){
			outcome = 404;
			response.put("outcome",false);
			response.put("error","Password reset token not found or expired");
			return new ResponseEntity<>(response,HttpStatusCode.valueOf(outcome));
		}

		//If the token was found within the database, an expiry check
		//must be performed to ensure the user is not using an expired token.
		//If the token is expired, this will be deleted from the database,
		//the same 404 error will eb returned.
		if(TimeService.now() > passwordResetToken.expirationDate()){

			passwordResetToken.delete("token");
			outcome = 404;
			response.put("outcome",false);
			response.put("error","Password reset token not found or expired");
			return new ResponseEntity<>(response,HttpStatusCode.valueOf(outcome));
		}
		
		//If we reach this stage, it means the token was found, and it is valid.

		//Getting the user which is changing their password.
		User user = User.collection().where("email",passwordResetToken.email()).getFirst();
		//Creation of the structure the data must follow to enter the database.
		//The password is saved after the addition of pepper being hashed.
		HashMap<String, Object> data = new HashMap<>();
		data.put("password",HashService.generateSHA1(PlantIqServerApplication.getInstance().getPasswordPepper()+request.getParameter("password")));
		//If the query successfully updates the record, and 200 html
		//status code will be set, and a confirmation message will be
		//returned. If no rows were affected, the html status code will
		//500 and an error message will be returned.
		if(user.update(data)){
			outcome = 200;
			response.put("outcome",true);
			response.put("message","Password reset");
			passwordResetToken.delete("token");
			Notification.create(user,"Password successfully reset");
		}else{
			outcome = 500;
			response.put("outcome",false);
			response.put("message","Failed to reset password, please contact support");
		}

		return new ResponseEntity<>(response,HttpStatusCode.valueOf(outcome));
	}
}
