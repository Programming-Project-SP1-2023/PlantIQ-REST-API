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
        HashMap<String,Object> response = new HashMap<>();

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
        //outcome will be true and a 200 status code will be returned.
        if(plantData == null){
            response.put("outcome",false);
            response.put("message","No record found of plant for the provided id");
            outcome = 404;
        }else{
            response.put("outcome",true);
            
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

    @PatchMapping("/{id}/name")
    public ResponseEntity<HashMap<String,Object>> updatePlantName(@PathVariable("id") String id, HttpServletRequest request){
        HashMap<String,Object> response = new HashMap<>();

        if(!Gate.authorized(request)){
            return Gate.abortUnauthorized();
        }

        UpdatePlantNameRule rule = new UpdatePlantNameRule();
        if(!rule.validate(request)){
            return rule.abort();
        }


        if(!PlantService.validPlantId(id)){
            response.put("outcome",false);
            response.put("message","No record found of plant for the provided id");
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(404));
        }

        PlantName old = PlantName.collection().where("user_id",Gate.getCurrentUser().getId()).where("sensor_id",id).orderBy("name").getFirst();

        if(old != null){
            old.delete("sensor_id");
        }

        HashMap<String,Object> data = new HashMap<>();
        data.put("name",request.getParameter("name"));
        data.put("user_id",Gate.getCurrentUser().getId());
        data.put("sensor_id",id);

        int outcome;
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
