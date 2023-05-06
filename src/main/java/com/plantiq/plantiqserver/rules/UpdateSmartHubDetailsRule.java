package com.plantiq.plantiqserver.rules;

import com.plantiq.plantiqserver.core.Rule;

import java.util.ArrayList;

// -----------------------------------------------------------------------------------|
//                                  LIST OF RULES                                     |
// -----------------------------------------------------------------------------------|
//                                                                                    |
//  Name:                                                                             |
//  A) Required -> Validates the firstname input is present and not null.             |
//  B) min:3 -> Validates the name is at least 3 characters long.                     |
//                                                                                    |
//  Post Frequency:                                                                   |
//  A) Required -> Validates the postFrequency input is present and not null.         |
//  B) Integer -> Validates the postFrequency input is an integer.                    |
//                                                                                    |
//------------------------------------------------------------------------------------|

public class UpdateSmartHubDetailsRule extends Rule {
    @Override
    protected void setup() {

        ArrayList<String> name = new ArrayList<>();
        name.add("required");
        name.add("min:3");

        this.rules.put("name",name);

        ArrayList<String> postFrequency = new ArrayList<>();
        postFrequency.add("required");
        postFrequency.add("integer");

        this.rules.put("postFrequency",postFrequency);

    }
}
