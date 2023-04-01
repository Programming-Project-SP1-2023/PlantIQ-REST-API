package com.plantiq.plantiqserver.rules;

import com.plantiq.plantiqserver.core.Rule;

import java.util.ArrayList;
//Class used to determine the Rules of a Session

// -----------------------------------------------------------------------------------|
//                                  LIST OF RULES                                     |
// -----------------------------------------------------------------------------------|
//                                                                                    |
//  A) IT IS REQUIRED                                                                 |
//  B) LENGTH OF THE TOKEN MUST BE OF 40 CHARACTERS, NOT MORE NOR LESS                |
//  C) TOKEN MUST NOT BE UNIQUE, MEANING THAT IT MUST MATCH A HASH IN THE DATABASE    |
//                                                                                    |
//------------------------------------------------------------------------------------|

public class SessionValidateRequestRule extends Rule {
    @Override
    protected void setup() {

        ArrayList<String> token = new ArrayList<>();

        token.add("required");
        token.add("min:40");
        token.add("max:40");
        token.add("!unique:session.token");

        this.rules.put("token",token);

    }
}
