package com.plantiq.plantiqserver.rules;

import com.plantiq.plantiqserver.core.Rule;

import java.util.ArrayList;

// -----------------------------------------------------------------------------------|
//                                  LIST OF RULES                                     |
// -----------------------------------------------------------------------------------|
//                                                                                    |
//  ID:                                                                               |
//  A) Required -> Validates the id input is present and not null.                    |
//  B) Min/Max:12 -> Validates the ID is exactly 12 characters.                       |
//  C) !unique:AwaitingRegistration.id -> To register a smarthub, its ID must match   |
//              with the one in the AwaitingRegistration.                             |
//                                                                                    |
//  Name:                                                                             |
//  A) Required -> Validates the name input is present and not null.                  |
//  C) Min -> Validates the name is not less than 5 characters.                       |
//  D) Max -> Validates the name is not greater than 50 characters.                   |
//                                                                                    |
//------------------------------------------------------------------------------------|

public class RegisterSmartHubRule extends Rule {

    @Override
    protected void setup() {

        ArrayList<String> id = new ArrayList<>();

        id.add("required");
        id.add("min:12");
        id.add("max:12");
        id.add("!unique:AwaitingRegistration.id");

        this.rules.put("id",id);

        ArrayList<String> name = new ArrayList<>();

        name.add("required");
        name.add("min:5");
        name.add("max:50");

        this.rules.put("name",name);

    }

}
