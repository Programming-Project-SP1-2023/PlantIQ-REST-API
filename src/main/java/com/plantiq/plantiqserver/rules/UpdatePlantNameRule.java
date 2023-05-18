package com.plantiq.plantiqserver.rules;

import com.plantiq.plantiqserver.core.Rule;

import java.util.ArrayList;

public class UpdatePlantNameRule extends Rule {
    @Override
    protected void setup() {
        ArrayList<String> name = new ArrayList<>();
        name.add("required");
        name.add("min:3");
        name.add("max:25");

        this.rules.put("name",name);
    }
}
