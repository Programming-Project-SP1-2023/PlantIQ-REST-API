package com.plantiq.plantiqserver.rules;

import com.plantiq.plantiqserver.core.Rule;

import java.util.ArrayList;
import java.util.HashMap;

// -----------------------------------------------------------------------------------|
//                                  LIST OF RULES                                     |
// -----------------------------------------------------------------------------------|
//                                                                                    |
//  Email:                                                                            |
//  A) Required -> Validates the email input is present and not null.                 |
//  B) regex.email -> Validates the input meets the criteria of an email address.     |
//                                                                                    |
//  Password:                                                                         |
//  A) Required -> Validates the email input is present and not null.                 |
//  B) min:8 -> Validates the password is at least 8 characters long.                 |
//  C) max:25 -> Validates the password is no larger than 25 characters long.         |
//                                                                                    |
//------------------------------------------------------------------------------------|

public class LoginUserRule extends Rule {
	@Override
	protected void setup() {

		ArrayList<String> email = new ArrayList<>();

		email.add("required");
		email.add("regex:email");

		ArrayList<String> password = new ArrayList<>();

		password.add("required");
		password.add("min:8");
		password.add("max:25");

		this.rules.put("email", email);
		this.rules.put("password", password);
	}

	public HashMap<String, ArrayList<String>> getRules() {
		return this.rules;
	}
}
