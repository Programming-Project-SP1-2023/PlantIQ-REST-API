package com.plantiq.plantiqserver.rules;

import com.plantiq.plantiqserver.core.Rule;

import java.util.ArrayList;

public class SetRangeRule extends Rule {
    @Override
    protected void setup() {
        ArrayList<String> temperature = new ArrayList<>();
        temperature.add("required");
        temperature.add("range:float[1,100]");
        this.rules.put("temperature",temperature);

        ArrayList<String> light = new ArrayList<>();
        light.add("required");
        light.add("range:float[1,24]");
        this.rules.put("light",light);

        ArrayList<String> moisture = new ArrayList<>();
        moisture.add("required");
        moisture.add("range:float[1,100]");
        this.rules.put("moisture",moisture);

        ArrayList<String> humidity = new ArrayList<>();
        humidity.add("required");
        humidity.add("range:float[1,100]");
        this.rules.put("humidity",humidity);
    }
}
