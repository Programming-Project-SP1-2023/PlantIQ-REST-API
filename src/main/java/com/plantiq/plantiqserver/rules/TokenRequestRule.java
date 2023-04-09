package com.plantiq.plantiqserver.rules;

import com.plantiq.plantiqserver.core.Rule;

import java.util.ArrayList;

public class TokenRequestRule extends Rule {

    @Override
    protected void setup() {

        ArrayList<String> email = new ArrayList<>();

        email.add("required");
        email.add("regex:email");
        email.add("!unique:User.email");

        this.rules.put("email",email);
    }


}
