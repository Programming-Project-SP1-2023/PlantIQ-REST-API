package com.plantiq.plantiqserver.service;

import com.plantiq.plantiqserver.core.Gate;
import com.plantiq.plantiqserver.model.PlantData;
import com.plantiq.plantiqserver.model.PlantName;
import com.plantiq.plantiqserver.model.SmartHomeHub;

import java.util.ArrayList;
import java.util.HashMap;

//---------------------------------Service Class---------------------------------
//The PlantService class has the scope of retrieving the data of the
// logged-in user plants.
//-------------------------------------------------------------------------------
public class PlantService {

    //This method is used to retrieve all the user's plants, from each
    //of their hubs.
    public static ArrayList<HashMap<String, Object>> getAll() {
        //Create our response object, this is returned as JSON.
        ArrayList<HashMap<String, Object>> response = new ArrayList<>();
        //List used to keep track of plants that have already
        //been added to the response, so that there is no
        //duplication.
        ArrayList<String> tracker = new ArrayList<>();
        //Retrieve all the hubs related to the logged-in user
        ArrayList<SmartHomeHub> hubs = SmartHomeHub.collection().where("user_id", Gate.getCurrentUser().getId()).get();
        //For each loop to go over each of the hubs and
        //retrieve each plant related to that hub.
        hubs.forEach((n) -> {
            //Retrieve all the hubs related to the logged-in user
            ArrayList<PlantData> plantData = PlantData.collection().where("smarthomehub_id", n.getId()).get();
            //For each plant, we will check if has already been
            //added to the response. If not, it will be added
            //with all its data
            plantData.forEach((p) -> {
                //If statement to check if the plant has been already
                //added to the response.
                if (!tracker.contains(p.getSensorId())) {
                    //Adding the id of the plant to the tracker, so that
                    // we don't duplicate it by mistake within the response.
                    tracker.add(p.getSensorId());
                    //Hashmap that will store attributes and values of each plant.
                    //This organisation of data will then be added to the response.
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

    //Method to check if a plant with a specific id exists.
    public static boolean validPlantId(String id, String user_id) {
        //Attempt to retrieve the plant with the given id from the database.
        PlantData plantData = PlantData.collection().where("sensor_id", id).getFirst();
        //If plantData is not null, the plant is valid, and a true
        // response will be returned, while if it is null, the id was
        // not found within the database, and a false response will be returned.
        return plantData != null;

    }
    //Method used to retrieve the name of a plant by giving an ID.
    //If no name was assigned to that ID, the ID will be returned.
    public static String getNameOrId(String sensor_id) {
        //Retrieving the name of a the plant
        PlantName plantName = PlantName.collection().where("sensor_id", sensor_id).orderBy("name").getFirst();
        //If the name was found, it will be returned.
        // while if it is null, the given ID will
        //be returned,
        if (plantName == null) {
            return sensor_id;
        } else {
            return plantName.getName();
        }
    }
}