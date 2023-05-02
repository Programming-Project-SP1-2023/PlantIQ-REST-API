package com.plantiq.plantiqserver.rules;

import com.plantiq.plantiqserver.core.Rule;

import java.util.ArrayList;
// -----------------------------------------------------------------------------------|
//                                  LIST OF RULES                                     |
// -----------------------------------------------------------------------------------|
//                                                                                    |
//  SmartHub_ID:                                                                      |
//  A) Required -> Validates the email input is present and not null.                 |
//  B) Min -> Must have at least 12 characters.                                       |
//  C) Max -> Must not have more than 50 characters                                   |
//------------------------------------------------------------------------------------|

public class NotificationRule extends Rule {

    @Override
    protected void setup() {
        ArrayList<String> smarthub_id = new ArrayList<>();

        smarthub_id.add("required");
        smarthub_id.add("min:12");
        smarthub_id.add("max:50");

        this.rules.put("smarthub_id",smarthub_id);


    }
}
