package com.plantiq.plantiqserver.rules;

import com.plantiq.plantiqserver.core.Rule;

import java.util.ArrayList;
// -----------------------------------------------------------------------------------|
//                                  LIST OF RULES                                     |
// -----------------------------------------------------------------------------------|
//                                                                                    |
//  A) Required -> Validates that the token is present and not null.                  |
//  B) Min/Max:40 -> Validates the name is exactly 40 characters long.                |
//  C) !unique:Session.token -> Validates that the token is not unique but has a match|
//                              within the database.                                  |
//                                                                                    |
//------------------------------------------------------------------------------------|

public class ValidateSessionRule extends Rule {
    @Override
    protected void setup() {

        ArrayList<String> token = new ArrayList<>();

        token.add("required");
        token.add("min:40");
        token.add("max:40");
        token.add("!unique:Session.token");

        this.rules.put("token",token);

    }
}
