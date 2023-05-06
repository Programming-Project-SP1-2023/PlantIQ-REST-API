package com.plantiq.plantiqserver.rules;

import com.plantiq.plantiqserver.core.Rule;

import java.util.ArrayList;

// -----------------------------------------------------------------------------------|
//                                  LIST OF RULES                                     |
// -----------------------------------------------------------------------------------|
//                                                                                    |
//  Email:                                                                            |
//  A) Required -> Validates the email input is present and not null.                 |
//  B) regex.email -> Validates the input meets the criteria of an email address.     |
//                                                                                    |
//------------------------------------------------------------------------------------|

public class PasswordResetRule extends Rule {
    @Override
    protected void setup() {

        ArrayList<String> email = new ArrayList<>();

        email.add("required");
        email.add("regex:email");

        this.rules.put("email",email);
    }
}
