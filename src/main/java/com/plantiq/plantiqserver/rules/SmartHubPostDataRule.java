package com.plantiq.plantiqserver.rules;

import com.plantiq.plantiqserver.core.Rule;

import java.util.ArrayList;

public class SmartHubPostDataRule extends Rule {

    @Override
    protected void setup() {

        //Sensor ID
        ArrayList<String> sensorId = new ArrayList<>();
        sensorId.add("required");
        sensorId.add("min:10");
        sensorId.add("max:50");
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
