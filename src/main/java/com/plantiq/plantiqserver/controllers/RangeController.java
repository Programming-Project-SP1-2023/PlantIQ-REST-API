package com.plantiq.plantiqserver.controllers;

import com.plantiq.plantiqserver.core.Gate;
import com.plantiq.plantiqserver.model.Range;
import com.plantiq.plantiqserver.model.SmartHomeHub;
import com.plantiq.plantiqserver.rules.SetRangeRule;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
//---------------------------------Controller Class---------------------------------
//The RangeController class will be serving all the endpoints that deal with the
//healthy ranges of temperature, humidity, moisture and light. 
//----------------------------------------------------------------------------------

@RestController
@RequestMapping("/range")
public class RangeController {
    //@PostMapping("/{id}") is the endpoint used to set a personalised 
    //range the 4 fields.
    @PostMapping("/{id}")
    public ResponseEntity<HashMap<String,Object>> setRange(@PathVariable String id, HttpServletRequest request){
        //Create our response object, this is returned as JSON.
        HashMap<String,Object> response = new HashMap<>();
        //Authenticate the user with the gate service
        if(!Gate.authorized(request)){
            return Gate.abortUnauthorized();
        }
        //Create our set range rule, we will validate
        //our request using this rule.
        SetRangeRule rule = new SetRangeRule();
        //Validate our request and return the errors if present.
        if(!rule.validate(request)){
            return rule.abort();
        }
        //Retrieve the current range for the selected hub.
        Range range = Range.collection().where("smarthub_id",id).getFirst();
        //If there is a range, it will be deleted, leaving space
        //for the new range
        if(range != null){
            range.delete();
        }

        HashMap<String,Object> data = new HashMap<>();
        //Inserting the new values into the hashmap that 
        //will be used to update the database
        data.put("temperature",request.getParameter("temperature"));
        data.put("light",request.getParameter("light"));
        data.put("humidity",request.getParameter("humidity"));
        data.put("moisture",request.getParameter("moisture"));
        data.put("smarthub_id",id);
        //Insertion of the record into the Range database
        Range.insert("Range",data);
        //Return a successful response
        response.put("outcome",true);
        response.put("message","Updated range values for smart hub!");

        return new ResponseEntity<>(response,HttpStatusCode.valueOf(200));
    }
    //@GetMapping("/{id}") is the endpoint used to retrieve the range of an endpoint
    @GetMapping("/{id}")
    public ResponseEntity<HashMap<String,Object>> getRange(@PathVariable String id, HttpServletRequest request){
        //Create our response object, this is returned as JSON.
        HashMap<String,Object> response = new HashMap<>();

        //Validate the user is logged in.
        if(!Gate.authorized(request)){
            return Gate.abortUnauthorized();
        }

        //Check if the ID is valid, else return an error.
        SmartHomeHub hub = SmartHomeHub.collection().where("id",id).getFirst();

        if(hub == null){
            response.put("outcome",false);
            response.put("message","Invalid smart home hub id provided!");
            return new ResponseEntity<>(response,HttpStatusCode.valueOf(401));
        }

        //Load the range or return the default.
        Range range = Range.collection().where("smarthub_id",id).getFirst();
        if(range == null){
            range = Range.getDefault();
        }

        response.put("range",range);
        response.put("outcome",true);

        return new ResponseEntity<>(response,HttpStatusCode.valueOf(200));
    }
}
