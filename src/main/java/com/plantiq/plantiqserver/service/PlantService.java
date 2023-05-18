package com.plantiq.plantiqserver.service;

import com.plantiq.plantiqserver.core.Gate;
import com.plantiq.plantiqserver.model.PlantData;
import com.plantiq.plantiqserver.model.PlantName;
import com.plantiq.plantiqserver.model.SmartHomeHub;

import java.util.ArrayList;
import java.util.HashMap;

public class PlantService {

    public static ArrayList<HashMap<String, Object>> getAll() {
        ArrayList<HashMap<String, Object>> response = new ArrayList<>();

        ArrayList<String> tracker = new ArrayList<>();

        ArrayList<SmartHomeHub> hubs = SmartHomeHub.collection().where("user_id", Gate.getCurrentUser().getId()).get();

        hubs.forEach((n) -> {
            ArrayList<PlantData> plantData = PlantData.collection().where("smarthomehub_id", n.getId()).get();

            plantData.forEach((p) -> {
                if (!tracker.contains(p.getSensorId())) {
                    tracker.add(p.getSensorId());

                    HashMap<String, Object> plant = new HashMap<>();
                    plant.put("sensor_id", p.getSensorId());
                    plant.put("name", PlantService.getNameOrId(p.getSensorId()));
                    plant.put("smarthomehub_id", p.getSmartHomeHubId());
                    plant.put("postFrequency", n.getPostFrequency());

                    response.add(plant);

                }
            });

        });

        return response;
    }

    public static boolean validPlantId(String id, String user_id) {

        PlantData plantData = PlantData.collection().where("sensor_id", id).getFirst();

        return plantData != null;

    }

    public static String getNameOrId(String sensor_id) {
        PlantName plantName = PlantName.collection().where("sensor_id", sensor_id).orderBy("name").getFirst();

        if (plantName == null) {
            return sensor_id;
        } else {
            return plantName.getName();
        }
    }
}