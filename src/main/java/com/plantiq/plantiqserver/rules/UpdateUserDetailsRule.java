package com.plantiq.plantiqserver.rules;

import com.plantiq.plantiqserver.core.Rule;

import java.util.ArrayList;

// -----------------------------------------------------------------------------------|
//                                  LIST OF RULES                                     |
// -----------------------------------------------------------------------------------|
//                                                                                    |
//  ? TAG -> Makes the input optional. If not blank, it must follow the rule.         |
//                                                                                    |
//  Email:                                                                            |
//  A) regex.email -> Validates the input meets the criteria of an email address.     |
//  B) unique:User.email -> Validates that no other account is registered             |
//                          with this email                                           |
//                                                                                    |
//  Firstname:                                                                        |
//  A) min:3 -> Validates the firstname is at least 3 characters long.                |
//  B) max:25 -> Validates the firstname is no larger than 25 characters long.        |
//                                                                                    |
//  Surname                                                                           |
//  A) min:2 -> Validates the surname is at least 2 characters long.                  |
//  B) max:25 -> Validates the surname is no larger than 25 characters long.          |
//                                                                                    |
//  Password:                                                                         |
//  A) min:8 -> Validates the password is at least 8 characters long.                 |
//  B) max:50 -> Validates the password is no larger than 50 characters long.         |
//                                                                                    |
//------------------------------------------------------------------------------------|

public class UpdateUserDetailsRule extends Rule {

    @Override
    protected void setup() {

        ArrayList<String> email = new ArrayList<>();

        email.add("?regex:email");
        email.add("?@unique:User.email");

        this.rules.put("email",email);

        ArrayList<String> firstname = new ArrayList<>();
        firstname.add("?min:3");
        firstname.add("?max:25");

        this.rules.put("firstname",firstname);

        ArrayList<String> surname = new ArrayList<>();
        surname.add("?min:2");
        surname.add("?max:25");

        this.rules.put("surname",surname);

        ArrayList<String> password = new ArrayList<>();
        password.add("?min:8");
        password.add("?max:50");


        this.rules.put("password",password);
    }
}
