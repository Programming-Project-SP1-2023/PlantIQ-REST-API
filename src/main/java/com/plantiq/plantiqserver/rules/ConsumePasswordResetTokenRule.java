package com.plantiq.plantiqserver.rules;

import com.plantiq.plantiqserver.core.Rule;

import java.util.ArrayList;

// -----------------------------------------------------------------------------------|
//                                  LIST OF RULES                                     |
// -----------------------------------------------------------------------------------|
//                                                                                    |
//  Token:                                                                            |
//  A) Required -> Validates the email input is present and not null.                 |
//  B) min:40/max:40 -> Validates the password exactly 40 characters long             |
//  B) !unique:PasswordResetToken.token -> Validates that the token is not uniques,   |
//                                  meaning that it matches a token in the database   |
//                                                                                    |
//  Password:                                                                         |
//  A) Required -> Validates the email input is present and not null.                 |
//  B) min:8 -> Validates the password is at least 8 characters long.                 |
//  C) max:50 -> Validates the password is no larger than 50 characters long.         |
//                                                                                    |
//------------------------------------------------------------------------------------|


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
