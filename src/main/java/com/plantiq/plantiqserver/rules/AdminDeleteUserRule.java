package com.plantiq.plantiqserver.rules;

import com.plantiq.plantiqserver.core.Rule;

import java.util.ArrayList;

// -----------------------------------------------------------------------------------|
//                                  LIST OF RULES                                     |
// -----------------------------------------------------------------------------------|
//                                                                                    |
//  Password:                                                                         |
//  A) min:8 -> Validates the password is at least 8 characters long.                 |
//  B) max:25 -> Validates the password is no larger than 50 characters long.         |
//                                                                                    |
//------------------------------------------------------------------------------------|


public class AdminDeleteUserRule extends Rule {
    @Override
    protected void setup() {

        ArrayList<String> password = new ArrayList<>();
        password.add("required");
        password.add("min:8");
        password.add("max:25");
        this.rules.put("password",password);

    }
}
