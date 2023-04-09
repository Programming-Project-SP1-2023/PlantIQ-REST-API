package com.plantiq.plantiqserver.rules;

import com.plantiq.plantiqserver.core.Rule;

import java.util.ArrayList;

public class ConsumePasswordResetTokenRule extends Rule {

    public void setup(){

        ArrayList<String> token = new ArrayList<>();

        token.add("required");
        token.add("min:40");
        token.add("max:40");
        token.add("!unique:PasswordResetToken.token");

        this.rules.put("token",token);

        ArrayList<String> password = new ArrayList<>();

        password.add("required");
        password.add("min:8");
        password.add("max:50");

        this.rules.put("password",password);

    }

}
