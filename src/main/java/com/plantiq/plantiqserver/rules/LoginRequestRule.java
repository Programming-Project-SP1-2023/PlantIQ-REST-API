package com.plantiq.plantiqserver.rules;

import com.plantiq.plantiqserver.core.Rule;

import java.util.ArrayList;

public class LoginRequestRule extends Rule {

    @Override
    protected void setup() {

        ArrayList<String> email = new ArrayList<>();

        email.add("required");
        email.add("regex.email");

        ArrayList<String> password = new ArrayList<>();

        password.add("required");
        password.add("min:8");

        this.rules.put("email",email);
        this.rules.put("password",password);

    }
}
