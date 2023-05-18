package com.plantiq.plantiqserver.rules;

import com.plantiq.plantiqserver.core.Rule;

import java.util.ArrayList;

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
