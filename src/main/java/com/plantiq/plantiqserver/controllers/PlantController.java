package com.plantiq.plantiqserver.controllers;

import com.plantiq.plantiqserver.core.Gate;
import com.plantiq.plantiqserver.model.PlantData;
import com.plantiq.plantiqserver.model.PlantName;
import com.plantiq.plantiqserver.rules.UpdatePlantNameRule;
import com.plantiq.plantiqserver.service.PlantService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

//---------------------------------Controller Class---------------------------------
//The UserController class will be serving all the endpoints that deal user's info.
//Here it will be possible to retrieve the information regarding a single user, a
//group of users and modify their data.
//----------------------------------------------------------------------------------

@RestController
@RequestMapping("/plant")
public class PlantController {

    //@GetMapping("/all") is the endpoint which will return
    //the data of all the plants.
    @GetMapping("/all")
    public ResponseEntity<HashMap<String,Object>> getAllPlants(HttpServletRequest request){
        //Create our response object, this is returned as JSON.
        //This will contain a true outcome and a list of all
        //the plants
        HashMap<String,Object> response = new HashMap<>();
        //Authenticate the user with the gate service
        if(!Gate.authorized(request)){
            return Gate.abortUnauthorized();
        }
        response.put("outcome",true);
        response.put("data", PlantService.getAll());

        return new ResponseEntity<>(response,HttpStatusCode.valueOf(200));
    }

    //
    @GetMapping("/{id}/name")
    public ResponseEntity<HashMap<String,Object>> getPlantName(@PathVariable("id") String id, HttpServletRequest request){
        //Create our response object, this is returned as JSON.
        HashMap<String,Object> response = new HashMap<>();
        //Authenticate the user with the gate service
        if(!Gate.authorized(request)){
            return Gate.abortUnauthorized();
        }
        //Retrieving the name of the plant with the inputted
        //id for the currently logged-in user.
        PlantName plantName = PlantName.collection().where("user_id",Gate.getCurrentUser().getId()).where("sensor_id",id).orderBy("name").getFirst();
        PlantData plantData = PlantData.collection().where("sensor_Id",id).getFirst();
        //Variable that will store the html status code.
        int outcome;
        //If the plant is null, no plant with the given ID was found.
        //A false outcome, a 404 error and an error message will be
        //returned. If a plant with the given id does exist, the
        //outcome will be true, a 200 status code will be returned
        // and the plant name will be added to the response.
        if(plantData == null){
            response.put("outcome",false);
            response.put("message","No record found of plant for the provided id");
            outcome = 404;
        }else{
            response.put("outcome",true);
            //If a plant does have a name, it will be used.
            //If there is no name for that plant, it's ID
            //will be used
            if(plantName == null){
                response.put("name",plantData.getSensorId());
                response.put("message","No display name set, defaulting to sensor_id value");
            }else{
                response.put("name",plantName.getName());
            }
            outcome = 200;
        }

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));
    }
    //@PatchMapping("/{id}/name") is the endpoint used to change the name of a plant.
    @PatchMapping("/{id}/name")
    public ResponseEntity<HashMap<String,Object>> updatePlantName(@PathVariable("id") String id, HttpServletRequest request){
        //Create our response object, this is returned as JSON.
        HashMap<String,Object> response = new HashMap<>();
        //Authenticate the user with the gate service
        if(!Gate.authorized(request)){
            return Gate.abortUnauthorized();
        }
        //Create our update plant name rule, we will validate
        //our request using this rule.
        UpdatePlantNameRule rule = new UpdatePlantNameRule();
        //Validate our request and return the errors if present.
        if(!rule.validate(request)){
            return rule.abort();
        }

        //If statement to ensure that a plant with the given ID does exist.
        //If no record was found, a false outcome will be returned with an
        //error message.
        if(!PlantService.validPlantId(id)){
            response.put("outcome",false);
            response.put("message","No record found of plant for the provided id");
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(404));
        }
        //Retrieving the old name
        PlantName old = PlantName.collection().where("user_id",Gate.getCurrentUser().getId()).where("sensor_id",id).orderBy("name").getFirst();
        //If the plant had a name, its record will be deleted.
        if(old != null){
            old.delete("sensor_id");
        }

        HashMap<String,Object> data = new HashMap<>();
        //Retrieving the new name, the sensor's id and the user's id.
        data.put("name",request.getParameter("name"));
        data.put("user_id",Gate.getCurrentUser().getId());
        data.put("sensor_id",id);
        //Variable that will store the html status code.
        int outcome;
        //Attemp to insert the plant's name into the database.
        //If the record is inserted successfully, a true outcome 
        //and a 200 http status code will be returned. If the 
        //query did not affect the database, a 500 error will be
        //thrown with a message and a false outcome.
        if(PlantName.insert("PlantName",data)){
            outcome = 200;
            response.put("outcome",true);
            response.put("message","Updated plant display name");
        }else{
            outcome = 500;
            response.put("outcome",false);
            response.put("message","Unable to update plant display name, please contact support");
        }

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));
    }
}
