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
//  C) unique:User.email -> Validates that no other account is registered             |
//                          with this email                                           |
//                                                                                    |
//  Firstname:                                                                        |
//  A) Required -> Validates the firstname input is present and not null.             |
//  B) min:3 -> Validates the firstname is at least 3 characters long.                |
//  C) max:25 -> Validates the firstname is no larger than 25 characters long.        |
//                                                                                    |
//  Surname                                                                           |
//  A) Required -> Validates the surname input is present and not null.               |
//  B) min:2 -> Validates the surname is at least 2 characters long.                  |
//  C) max:25 -> Validates the surname is no larger than 25 characters long.          |
//                                                                                    |
//  Password:                                                                         |
//  A) Required -> Validates the email input is present and not null.                 |
//  B) min:8 -> Validates the password is at least 8 characters long.                 |
//  C) max:50 -> Validates the password is no larger than 50 characters long.         |
//                                                                                    |
//------------------------------------------------------------------------------------|

public class RegisterUserRule extends Rule {
	@Override
	protected void setup() {
		ArrayList<String> email = new ArrayList<>();
		email.add("required");
		email.add("regex:email");
		email.add("unique:User.email");
		this.rules.put("email", email);

		ArrayList<String> firstname = new ArrayList<>();
		firstname.add("required");
		firstname.add("min:3");
		firstname.add("max:25");
		this.rules.put("firstname", firstname);

		ArrayList<String> surname = new ArrayList<>();
		surname.add("required");
		surname.add("min:2");
		surname.add("max:25");
		this.rules.put("surname", surname);

		ArrayList<String> password = new ArrayList<>();
		password.add("required");
		password.add("min:8");
		password.add("max:50");

		this.rules.put("password", password);
	}

	public HashMap<String, ArrayList<String>> getRules() {
		return this.rules;
	}
}
