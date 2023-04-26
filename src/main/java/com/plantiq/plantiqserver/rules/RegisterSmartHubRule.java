package com.plantiq.plantiqserver.rules;

import com.plantiq.plantiqserver.core.Rule;

import java.util.ArrayList;

public class RegisterSmartHubRule extends Rule {

    @Override
    protected void setup() {

        ArrayList<String> id = new ArrayList<>();

        id.add("required");
        id.add("min:12");
        id.add("max:50");
        id.add("!unique:AwaitingRegistration.id");

        this.rules.put("id",id);

        ArrayList<String> name = new ArrayList<>();

        name.add("required");
        name.add("min:5");
        name.add("max:50");

        this.rules.put("name",name);

    }

}
