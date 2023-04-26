package com.plantiq.plantiqserver.rules;

import com.plantiq.plantiqserver.core.Rule;

import java.util.ArrayList;

public class UpdateSmartHubDetailsRule extends Rule {
    @Override
    protected void setup() {

        ArrayList<String> name = new ArrayList<>();
        name.add("required");
        name.add("min:3");

        this.rules.put("name",name);

        ArrayList<String> postFrequency = new ArrayList<>();
        postFrequency.add("required");
        postFrequency.add("integer");

        this.rules.put("postFrequency",postFrequency);

    }
}
