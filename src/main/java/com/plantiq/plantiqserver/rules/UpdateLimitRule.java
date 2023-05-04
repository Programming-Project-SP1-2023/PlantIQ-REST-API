package com.plantiq.plantiqserver.rules;

import com.plantiq.plantiqserver.core.Rule;

import java.util.ArrayList;

// -----------------------------------------------------------------------------------|
//                                  LIST OF RULES                                     |
// -----------------------------------------------------------------------------------|
//                                                                                    |
//  Temperature/Humidity/Moisture/Light                                               |
//  A) range:float[0,100] -> Must be a float between 0 and 100                        |
//  B) regex.email -> Validates the input meets the criteria of an email address.     |
//                                                                                    |
//------------------------------------------------------------------------------------|

public class UpdateLimitRule extends Rule {

    @Override
    protected void setup() {

        ArrayList<String> temperature= new ArrayList<>();
        ArrayList<String> humidity= new ArrayList<>();
        ArrayList<String> moisture= new ArrayList<>();
        ArrayList<String> light= new ArrayList<>();

        temperature.add("range:float[0,100]");
        humidity.add("range:float[0,100]");
        moisture.add("range:float[0,100]");
        light.add("range:float[0,100]");
    }
}
