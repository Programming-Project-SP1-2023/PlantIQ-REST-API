package com.plantiq.plantiqserver.controllers;

import com.plantiq.plantiqserver.core.Gate;
import com.plantiq.plantiqserver.model.AwaitingRegistration;
import com.plantiq.plantiqserver.model.SmartHomeHub;
import com.plantiq.plantiqserver.rules.RegisterSmartHubRequestRule;
import com.plantiq.plantiqserver.service.HashService;
import com.plantiq.plantiqserver.service.TimeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.PUT, RequestMethod.OPTIONS})
@RequestMapping("/smarthub")
public class SmartHubController {

    @PostMapping("/{id}/data")
    public ResponseEntity<HashMap<String, Object>> savePlantData(@PathVariable("id") String id, HttpServletRequest request){

        HashMap<String, Object> response = new HashMap<>();

        //Validate our id length is with in expected standards
        if(id.length() < 17 && id.length() > 12){
            response.put("error","Smart home hub identifier did not meet expected standard");
            response.put("outcome",false);
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(400));
        }

        //Get the smart home hub model object for this id.
        SmartHomeHub smartHomeHub = SmartHomeHub.collection().where("id",id).getFirst();

        //Check the ID is valid and if not set it to await registration.
        if(smartHomeHub == null && AwaitingRegistration.collection().where("id",id).getFirst() == null){
            HashMap<String,Object> data = new HashMap<>();
            data.put("id",id);
            data.put("date", TimeService.nowPlusDays(7));
            AwaitingRegistration.insert("AwaitingRegistration",data);
        }

        //Placeholder!
        response.put("error","Endpoint awaiting further setup");

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/{id}/data")
    public ResponseEntity<HashMap<String, Object>> deletePlantData(@PathVariable("id") String id, HttpServletRequest request){

        HashMap<String, Object> response = new HashMap<>();

        if(!Gate.authorized(request)){
            return Gate.abortUnauthorized();
        }

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));

    }

    @GetMapping("/{id}/rate")
    public ResponseEntity<HashMap<String, Object>> getPlantDataUploadRate(@PathVariable("id") String id, HttpServletRequest request){

        HashMap<String, Object> response = new HashMap<>();

        //Authenticate the user with the gate service
        if(!Gate.authorized(request)){
            return Gate.abortUnauthorized();
        }

        //Get the current hub requested
        SmartHomeHub hub = SmartHomeHub.collection().where("id",id).where("user_id",Gate.getCurrentUser().getId()).getFirst();

        int outcome;
        if(hub == null){
            outcome = 404;
            response.put("outcome",false);
            response.put("error","Smart Home Hub not found, please contact support");
        }else{
            outcome = 200;
            response.put("outcome",true);
            response.put("rate",hub.getPostFrequency());
        }


        return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));

    }

    @GetMapping("/all")
    public ResponseEntity<HashMap<String, Object>> getAll(HttpServletRequest request){

        HashMap<String, Object> response = new HashMap<>();

        //Authenticate the user with the gate service
        if(!Gate.authorized(request)){
            return Gate.abortUnauthorized();
        }

        //Get the list of hubs for this current user from the database
        ArrayList<SmartHomeHub> hubs = SmartHomeHub.collection().where("user_id",Gate.getCurrentUser().getId()).get();

        //Check if the user has any hubs by the size and either add them or a message.
        if(hubs.size() == 0){
            response.put("message","No hubs currently registered with user");
        }else{
            response.put("list",hubs);
        }

        //Add a valid response outcome
        response.put("outcome",true);

        //Lastly return the response.
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }

    @PostMapping("/register")
    public ResponseEntity<HashMap<String, Object>> register(HttpServletRequest request){

        HashMap<String, Object> response = new HashMap<>();

        //Authenticate the user with the gate service
        if(!Gate.authorized(request)){
            return Gate.abortUnauthorized();
        }

        //Create our request rule
        RegisterSmartHubRequestRule rule = new RegisterSmartHubRequestRule();

        //Validate our request rule
        if(!rule.validate(request)){
            return rule.abort();
        }

        //Create our new model data hashmap
        HashMap<String,Object> data = new HashMap<>();

        data.put("id",request.getParameter("id"));
        data.put("deviceSpecificPassword", HashService.generateSHA1(request.getParameter("id")+TimeService.now()));
        data.put("name",request.getParameter("name"));
        data.put("user_id",Gate.getCurrentUser().getId());
        data.put("lastPosted",TimeService.now());
        data.put("postFrequency","");

        //Get the current object from the awaiting registration table
        AwaitingRegistration smartHubAwaitingRegistration = AwaitingRegistration.collection().where("id",request.getParameter("id")).getFirst();

        //Delete the object from the table by its id.
        smartHubAwaitingRegistration.delete("id");

        //Declare our status outcome
        int outcome;

        //Attempt to insert the data, if true return so, else return false.
        if (SmartHomeHub.insert("SmartHomeHub",data)) {
            response.put("outcome", true);
            response.put("message", "Smart Hub registered to account");
            outcome = 200;
        } else {
            response.put("outcome", false);
            response.put("errors", "Failed to register smart hub, please contact support!");
            outcome = 500;
        }

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));
    }



}
