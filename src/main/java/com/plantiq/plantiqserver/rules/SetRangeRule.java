package com.plantiq.plantiqserver.rules;

import com.plantiq.plantiqserver.core.Rule;

import java.util.ArrayList;
// -----------------------------------------------------------------------------------|
//                                  LIST OF RULES                                     |
// -----------------------------------------------------------------------------------|
//                                                                                    |
//  Temperature:                                                                      |
//  A) Required -> Validates the temperature input is present and not null.           |
//  B) Float[1,100] -> Validates the temperature is a float and its value             |
//                     is between 1 and 100.                                          |
//                                                                                    |
//  Humidity:                                                                         |
//  A) Required -> Validates the humidity input is present and not null.              | 
//  B) Float[1,100] -> Validates the humidity is a float and its value                |
//                     is between 1 and 100.                                          |
//                                                                                    |
//  Light:                                                                            |
//  A) Required -> Validates the light input is present and not null.                 |
//  B) Float[1,24] -> Validates the light is a float and its value                    |
//                     is between 1 and 24.|                                          |
//                                                                                    |
//  Moisture:                                                                         |
//  A) Required -> Validates the moisture input is present and not null.              |
//  B) Float[1,100] -> Validates the moisture is a float and its value                |
//                     is between 1 and 100.                                          |
//                                                                                    |
//------------------------------------------------------------------------------------|


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
