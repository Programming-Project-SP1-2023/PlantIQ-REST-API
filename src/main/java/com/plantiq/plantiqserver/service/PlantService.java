package com.plantiq.plantiqserver.service;

import com.plantiq.plantiqserver.core.Gate;
import com.plantiq.plantiqserver.model.PlantData;
import com.plantiq.plantiqserver.model.SmartHomeHub;

import java.util.ArrayList;
import java.util.HashMap;

public class PlantService {

    public static ArrayList<HashMap<String, Object>> getAll(){
        ArrayList<HashMap<String,Object>> response = new ArrayList<>();

        ArrayList<String> tracker = new ArrayList<>();

        ArrayList<SmartHomeHub> hubs = SmartHomeHub.collection().where("user_id", Gate.getCurrentUser().getId()).get();

        hubs.forEach((n)->{
            ArrayList<PlantData> plantData = PlantData.collection().where("smarthomehub_id",n.getId()).get();

            plantData.forEach((p)->{
                if(!tracker.contains(p.getSensorId())){
                    tracker.add(p.getSensorId());

                    HashMap<String,Object> plant = new HashMap<>();
                    plant.put("sensor_id",p.getSensorId());
                    plant.put("name","pending");
                    plant.put("smarthomehub_id",p.getSmartHomeHubId());
                    plant.put("postFrequency",n.getPostFrequency());

                    response.add(plant);

                }
            });

        });

        return response;
    }
}
