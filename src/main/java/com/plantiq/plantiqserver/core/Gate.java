package com.plantiq.plantiqserver.core;

import com.plantiq.plantiqserver.model.Session;
import com.plantiq.plantiqserver.model.User;
import com.plantiq.plantiqserver.service.TimeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

//-----------------------------------------------------------------//
//                            Gate Class                           //
//-----------------------------------------------------------------//

//Our gate class is different from the rules objects as it only
//concerns itself with header authorization only! Note that this
//gate will treat any lack of a header provided as a fail and return
//the same error message.

public class Gate {
	private static User current;

	//-----------------------------------------------------------------//
	//                        Authorized Method                        //
	//-----------------------------------------------------------------//

	//This method is used to validate an authorization token is valid
	//before processing the rest of the request.
	public static boolean authorized(HttpServletRequest request) {

		//declare out outcome
		boolean outcome = false;

		//validate the header was provided
		if (request.getHeader("Authorization") == null) {
			return false;
		}

		//get our token from the header, if not present this will be null
		String token = request.getHeader("Authorization").substring(7);

		//attempt to load our session object from the database.
		Session session = Session.collection().where("token", token).orderBy("token").getFirst();

		//Check the session is valid and not expired.
		if (session != null && session.getExpiry() > TimeService.now()) {
			outcome = true;
			Gate.current = User.collection().where("id", session.getUserId()).getFirst();
		}

		if (!Gate.current.isActivated()) {
			outcome = false;
		}

		//finally return our outcome.
		return outcome;
	}

	//-----------------------------------------------------------------//
	//                    Authorized Method (isAdmin)                  //
	//-----------------------------------------------------------------//

	//Unlike the prior method is overloaded method accepts a isAdmin flag
	//if present this will validate the authorization of the user to ensure
	//this is a valid session and that the user is a valid admin.

	public static boolean authorizedAsAdmin(HttpServletRequest request) {

		//validate the header was provided
		if (request.getHeader("Authorization") == null) {
			return false;
		}

		//get our token from the header, if not present this will be null
		String token = request.getHeader("Authorization").substring(7);

		//attempt to load our session object from the database
		Session session = Session.collection().where("token", token).orderBy("token").getFirst();

		//Check the session is valid and not expired.
		if (session != null && session.getExpiry() > TimeService.now()) {
			Gate.current = User.collection().where("id", session.getUserId()).getFirst();
			return Gate.current.isAdministrator() && Gate.current.isActivated();
		}

		return false;
	}

	//-----------------------------------------------------------------//
	//                     Abort Unauthorized Method                   //
	//-----------------------------------------------------------------//

	//If called this method will create an unauthorized response entity
	//and return it to the caller with the 401 unauthorized status code.

	public static ResponseEntity<HashMap<String, Object>> abortUnauthorized() {
		HashMap<String, Object> response = new HashMap<>();
		response.put("outcome", false);
		response.put("error", "You are not authorized to access the requested resource");

		return new ResponseEntity<>(response, HttpStatusCode.valueOf(401));
	}
        //-----------------------------------------------------------------//
	//                      Get Current User Method                    //
	//-----------------------------------------------------------------//
	
	//This method returns the user that is currently logged-in.
	public static User getCurrentUser() {
		return Gate.current;
	}
}
