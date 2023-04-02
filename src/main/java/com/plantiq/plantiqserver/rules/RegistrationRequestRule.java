package com.plantiq.plantiqserver.rules;

import com.plantiq.plantiqserver.core.Rule;

import java.util.ArrayList;

public class RegistrationRequestRule extends Rule {
    @Override
    protected void setup() {
        ArrayList<String> email = new ArrayList<>();
        email.add("required");
        email.add("regex:email");
        email.add("unique:User.email");
        this.rules.put("email",email);

        ArrayList<String> firstname = new ArrayList<>();
        firstname.add("required");
        firstname.add("min:3");
        firstname.add("max:25");
        this.rules.put("firstname",firstname);

        ArrayList<String> surname = new ArrayList<>();
        surname.add("required");
        surname.add("min:2");
        surname.add("max:25");
        this.rules.put("surname",surname);

        ArrayList<String> password = new ArrayList<>();
        password.add("required");
        password.add("min:8");
        password.add("max:50");

        this.rules.put("password",password);
    }
}
