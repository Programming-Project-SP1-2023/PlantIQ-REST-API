package com.plantiq.plantiqserver.rules;

import com.plantiq.plantiqserver.core.Rule;

import java.util.ArrayList;

// -----------------------------------------------------------------------------------|
//                                  LIST OF RULES                                     |
// -----------------------------------------------------------------------------------|
//                                                                                    |
//  Name:                                                                             |
//  A) min:3 -> Validates the name is at least 3 characters long.                 |
//  B) max:25 -> Validates the name is no larger than 25 characters long.         |
//                                                                                    |
//------------------------------------------------------------------------------------|

public class UpdatePlantNameRule extends Rule {
    @Override
    protected void setup() {
        ArrayList<String> name = new ArrayList<>();
        name.add("required");
        name.add("min:3");
        name.add("max:25");

        this.rules.put("name",name);
    }
}
