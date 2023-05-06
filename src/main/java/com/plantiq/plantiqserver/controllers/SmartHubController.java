package com.plantiq.plantiqserver.controllers;

import com.plantiq.plantiqserver.core.*;
import com.plantiq.plantiqserver.model.*;
import com.plantiq.plantiqserver.rules.GetAllRule;
import com.plantiq.plantiqserver.rules.RegisterSmartHubRule;
import com.plantiq.plantiqserver.rules.PostSensorDataRule;
import com.plantiq.plantiqserver.rules.UpdateSmartHubDetailsRule;
import com.plantiq.plantiqserver.service.EmailService;
import com.plantiq.plantiqserver.service.HashService;
import com.plantiq.plantiqserver.service.TimeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/smarthub")
public class SmartHubController {

    @PostMapping("/{id}/data")
    public ResponseEntity<HashMap<String, Object>> savePlantData(@PathVariable("id") String id, HttpServletRequest request){

        HashMap<String, Object> response = new HashMap<>();

        //Validate our id length is with in expected standards
        if(id.length() != 12){
            response.put("error","Smart home hub identifier did not meet expected standard");
            response.put("outcome",false);
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(400));
        }

        AwaitingRegistration awaitingRegistration = AwaitingRegistration.collection().where("id",id).getFirst();

        if(awaitingRegistration != null){
            response.put("outcome",true);
            response.put("message","Awaiting registration");
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
        }

        //Get the smart home hub model object for this id.
        SmartHomeHub smartHomeHub = SmartHomeHub.collection().where("id",id).getFirst();

        //Check the ID is valid and if not set it to await registration.
        if(smartHomeHub == null){
            HashMap<String,Object> data = new HashMap<>();
            data.put("id",id);
            data.put("date", TimeService.nowPlusDays(7));
            AwaitingRegistration.insert("AwaitingRegistration",data);

            response.put("outcome",false);
            response.put("message","Smart Home Hub Awaiting Registration");

            //At this stage we simply want to return.
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(425));
        }

        //If we get to this stage we know the hub is valid and registered, and we then process our data validation.
        PostSensorDataRule rule = new PostSensorDataRule();

        if(!rule.validate(request)){
            return rule.abort();
        }

        HashMap<String,Object> data = new HashMap<>();

        data.put("smarthomehub_Id",smartHomeHub.getId());
        data.put("sensor_Id",request.getParameter("sensor_id"));
        data.put("temperature",request.getParameter("temperature"));
        data.put("humidity",request.getParameter("humidity"));
        data.put("light",request.getParameter("light"));
        data.put("moisture",request.getParameter("moisture"));
        data.put("timestamp",TimeService.now().toString());


        int outcome;
        if(PlantData.insert("PlantData",data)){
            response.put("outcome",true);
            outcome = 200;

        }else{
            response.put("error", "Failed to save plant data, please contact support!");
            response.put("outcome",false);
            outcome = 500;
        }
        HashMap<String,Object> notificationData = new HashMap<>();
        String userID = SmartHomeHub.collection().where("id", id).getFirst().getUser_id();
        notificationData.put("user_id",userID);
        notificationData.put("timestamp",TimeService.now());
//        String plantname=********************FINISH AND REPLACE BELOW

        Range range = Range.collection().where("smarthub_id", id).getFirst();
        String[] ranges = {range.getRangeTemperature(),range.getRangeMoisture(),range.getRangeLight(),range.getRangeHumidity()};


        String firstname = User.collection().where("id",userID).getFirst().getFirstname();
        String email = User.collection().where("id",userID).getFirst().getEmail();
////        -----------------------------------------------------------------------------------------CHANGE PLANT NAME
        //For Loop to ensure value is in range for each field
        String[] fields = {"temperature", "moisture", "light", "humidity"};
        for(int i=0;i<4;i++){
            String field=fields[i];
            String min="";
            String max="";
            try {
                min = ranges[i].split("-")[0];
                max = ranges[i].split("-")[1];
            }catch(NullPointerException e){
                response.put("error", "Ranges are not in the right format");
                response.put("outcome",false);
                outcome = 500;
                return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));
            }
            if(Float.parseFloat(request.getParameter(field))<Float.parseFloat(min) && Float.parseFloat(request.getParameter(field))>Float.parseFloat(max)){
                String value=request.getParameter(field);
                notificationData.put("field",field);
                notificationData.put("value",value);
                EmailService.sendAlert(email,firstname,field,value,"namexample");

                if(Notification.insert("Notification",notificationData)){
                    response.put("outcome",true);
                    outcome = 200;

                }else{
                    response.put("error", "Failed to save notification data, please contact support!");
                    response.put("outcome",false);
                    outcome = 500;
                }

            }
        }

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));
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
    public ResponseEntity<HashMap<String, Object>> getPlantDataUploadRate(@PathVariable("id") String id){

        HashMap<String, Object> response = new HashMap<>();

        //Validate our id length is with in expected standards
        if(id.length() != 12){
            response.put("error","Smart home hub identifier did not meet expected standard");
            response.put("outcome",false);
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(400));
        }

        //Get the current hub requested
        SmartHomeHub hub = SmartHomeHub.collection().where("id",id).getFirst();

        int outcome;
        if(hub == null){

            AwaitingRegistration awaitingRegistration = AwaitingRegistration.collection().where("id",id).getFirst();

            if(awaitingRegistration == null){

                HashMap<String,Object> data = new HashMap<>();
                data.put("id",id);
                data.put("date", TimeService.nowPlusDays(7));
                AwaitingRegistration.insert("AwaitingRegistration",data);
            }

            outcome = 425;
            response.put("outcome",false);
            response.put("message","Smart Home Hub Awaiting Registration");

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

        //Validate our request
        GetAllRule rule = new GetAllRule();
        if(!rule.validate(request)){
            return rule.abort();
        }

        //Add a valid response outcome
        response.put("outcome",true);

        //Create our hubs object.
        ModelCollection<SmartHomeHub> hubs = SmartHomeHub.collection().where("user_id",Gate.getCurrentUser().getId());

        //If virtual is passed then add it.
        if(request.getParameterMap().containsKey("virtual")){
            hubs.where("virtual",request.getParameter("virtual"));
        }

        //Add all the required parameters.
        hubs.limit(Integer.parseInt(request.getParameter("limit")));
        hubs.offset(Integer.parseInt(request.getParameter("offset")));
        hubs.orderBy(request.getParameter("sortBy"));
        hubs.orderType(Sort.valueOf(request.getParameter("sort")));

        ArrayList<SmartHomeHub> output = hubs.get();

        //Check if the user has any hubs by the size and either add them or a message.
        if(output.size() == 0){
            response.put("message","No hubs found matching criteria currently registered with user");
        }else{
            response.put("list",output);
        }

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
        RegisterSmartHubRule rule = new RegisterSmartHubRule();

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
        data.put("postFrequency","3600");

        if(request.getParameterMap().containsKey("virtual")){
            data.put("virtual",request.getParameter("virtual"));
        }else{
            data.put("virtual",false);
        }

        //Get the current object from the awaiting registration table
        AwaitingRegistration smartHubAwaitingRegistration = AwaitingRegistration.collection().where("id",request.getParameter("id")).getFirst();

        //Delete the object from the table by its id.
        smartHubAwaitingRegistration.delete("id");



        //Attempt to insert the data, if true return so, else return false.
        if (!SmartHomeHub.insert("SmartHomeHub",data)) {
            response.put("outcome", false);
            response.put("errors", "Failed to register smart hub, please contact support!");
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(500));
        }

        //If the code reached this line, the two previous error check did not generate any errors
        response.put("outcome", true);
        response.put("message", "Smart Hub registered to account");

        Email email = new Email();
        email.setUser(Gate.getCurrentUser())
                .setHtmlTemplate("/emails/smartHubRegistrationConfirmation.html")
                .setSubject("New Smart Hub Registered")
                .setVariables(data)
                .send();

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }

    @GetMapping("/{id}/data")
    public ResponseEntity<HashMap<String,Object>> getData(@PathVariable("id") String id, HttpServletRequest request){
        HashMap<String, Object> response = new HashMap<>();

        if(!Gate.authorized(request)){
            return Gate.abortUnauthorized();
        }

        int outcome;

        if(SmartHomeHub.collection().where("id",id).where("user_id",Gate.getCurrentUser().getId()).getFirst() == null){
            response.put("outcome",false);
            response.put("error","Smart Home Hub not found");
            outcome = 404;
        }else{
            response.put("outcome",true);
            response.put("data",PlantData.collection().where("smarthomehub_Id",id).get());
            outcome  = 200;

        }

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HashMap<String,Object>> updateHub(@PathVariable("id") String id, HttpServletRequest request){
        HashMap<String, Object> response = new HashMap<>();

        if(!Gate.authorized(request)){
            return Gate.abortUnauthorized();
        }

        SmartHomeHub smartHomeHub = SmartHomeHub.collection().where("id",id).getFirst();

        int outcome;
        if(smartHomeHub == null){
            response.put("outcome",false);
            outcome = 404;
        }else{
            UpdateSmartHubDetailsRule rule = new UpdateSmartHubDetailsRule();

            if(!rule.validate(request)){
                return rule.abort();
            }

            HashMap<String,Object> data = new HashMap<>();
            data.put("name",request.getParameter("name"));
            data.put("postFrequency",request.getParameter("postFrequency"));

            if(smartHomeHub.update(data)){
                outcome = 200;
                response.put("outcome",true);
                response.put("message","Successfully updated!");
            }else{
                outcome = 500;
                response.put("outcome",false);
                response.put("message","Update error, please contact support");
            }
        }

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HashMap<String,Object>> deleteHub(@PathVariable("id") String id, HttpServletRequest request) {
        HashMap<String, Object> response = new HashMap<>();

        if(!Gate.authorized(request)){
            return Gate.abortUnauthorized();
        }

        SmartHomeHub smartHomeHub = SmartHomeHub.collection().where("id",id).where("user_id",Gate.getCurrentUser().getId()).getFirst();

        int outcome;
        if(smartHomeHub == null){

            outcome = 404;
            response.put("outcome",false);
            response.put("error","Smart Home Hub not found, please contact support");
        }else{

            //Get all the plant data for the current smart home hub.
            ArrayList<PlantData> data = PlantData.collection().where("smarthomehub_id",id).get();

            //Delete all the data
            data.forEach(Model::delete);

            //Delete the smart hub
            smartHomeHub.delete();

            outcome = 200;
            response.put("outcome",true);
        }

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));
    }
}
