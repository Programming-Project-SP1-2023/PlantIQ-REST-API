package com.plantiq.plantiqserver.rules;

import com.plantiq.plantiqserver.core.Rule;

import java.util.ArrayList;

// -----------------------------------------------------------------------------------|
//                                  LIST OF RULES                                     |
// -----------------------------------------------------------------------------------|
//                                                                                    |
//  Sensor ID:                                                                        |
//  A) Required -> Validates the sensorID input is present and not null.              |
//  B) Min/Max:16 -> Validates the sensorID is exactly 16 characters.                 |
//                                                                                    |
//  Temperature:                                                                      |
//  A) Required -> Validates the temperature input is present and not null.           |
//  B) Float:16 -> Validates the temperature is a float.                              |
//                                                                                    |
//  Humidity:                                                                         |
//  A) Required -> Validates the humidity input is present and not null.              |
//  B) Float:16 -> Validates the humidity is a float.                                 |
//                                                                                    |
//  Light:                                                                            |
//  A) Required -> Validates the light input is present and not null.                 |
//  B) Float:16 -> Validates the light is a float.                                    |
//                                                                                    |
//  Moisture:                                                                         |
//  A) Required -> Validates the moisture input is present and not null.              |
//  B) Float:16 -> Validates the moisture is a float.                                 |
//                                                                                    |
//------------------------------------------------------------------------------------|

public class PostSensorDataRule extends Rule {

    @Override
    protected void setup() {

        //Sensor ID
        ArrayList<String> sensorId = new ArrayList<>();
        sensorId.add("required");
        sensorId.add("min:16");
        sensorId.add("max:16");
        this.rules.put("sensor_id",sensorId);

        //Temperature
        ArrayList<String> temperature = new ArrayList<>();
        temperature.add("required");
        temperature.add("float");
        this.rules.put("temperature",temperature);

        //Humidity
        ArrayList<String> humidity = new ArrayList<>();
        humidity.add("required");
        humidity.add("float");
        this.rules.put("humidity",humidity);

        //Light
        ArrayList<String> light = new ArrayList<>();
        light.add("required");
        light.add("float");
        this.rules.put("light",light);

        //Moisture
        ArrayList<String> moisture = new ArrayList<>();
        moisture.add("required");
        moisture.add("float");
        this.rules.put("moisture",moisture);
    }
}
