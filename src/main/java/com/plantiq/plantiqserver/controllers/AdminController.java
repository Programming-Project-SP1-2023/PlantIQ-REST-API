package com.plantiq.plantiqserver.controllers;

import com.plantiq.plantiqserver.PlantIqServerApplication;
import com.plantiq.plantiqserver.core.Gate;
import com.plantiq.plantiqserver.core.ModelCollection;
import com.plantiq.plantiqserver.core.Sort;
import com.plantiq.plantiqserver.model.PlantData;
import com.plantiq.plantiqserver.model.PlantName;
import com.plantiq.plantiqserver.model.SmartHomeHub;
import com.plantiq.plantiqserver.model.User;
import com.plantiq.plantiqserver.rules.GetAllRule;
import com.plantiq.plantiqserver.service.HashService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/admin")
public class AdminController {


    @PostMapping("/user/activate/{id}")
    public ResponseEntity<HashMap<String,Object>> activateAccount(@PathVariable("id") String id, HttpServletRequest request){

        HashMap<String,Object> response = new HashMap<>();

        if(!Gate.authorizedAsAdmin(request)){
            return Gate.abortUnauthorized();
        }

        User user = User.collection().where("id",id).getFirst();

        if(user == null){
            response.put("outcome",false);
            response.put("message","User account not found");
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(404));
        }


        int outcome = 200;

        if(user.isActivated()){
            response.put("outcome",true);
            response.put("message","User account is already activated");
        }else{
            HashMap<String,Object> data = new HashMap<>();
            data.put("isActivated","1");

            if(user.update(data)){
                response.put("outcome",true);
                response.put("message","Successfully activated user account");
            }else{
                outcome = 500;
                response.put("message","Failed to activate user account, please contact support");
            }
        }

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));
    }

    //@GetMapping("/all") is the endpoint which will return all records
    //of the user's entity. It is possible to refine the search by limiting,
    //offsetting and sorting the returned records.
    @GetMapping("/user/all")
    public ResponseEntity<HashMap<String, Object>> getAllUsers(HttpServletRequest request) {
        //Create our response object, this is returned as JSON.
        HashMap<String, Object> response = new HashMap<>();
        //Authenticate the user with the gate service
        if (!Gate.authorizedAsAdmin(request)) {
            return Gate.abortUnauthorized();
        }
        //Create our get all rule, we will validate
        //our request using this rule.
        GetAllRule rule = new GetAllRule();
        //Validate our request and return the errors if present.
        if (!rule.validate(request)) {
            return rule.abort();
        }

        //Creating the user's modelCollection
        ModelCollection<User> users = User.collection();

        //Add all the required parameters.
        users.limit(Integer.parseInt(request.getParameter("limit")));
        users.offset(Integer.parseInt(request.getParameter("offset")));
        users.orderBy(request.getParameter("sortBy"));
        users.orderType(Sort.valueOf(request.getParameter("sort")));

        //As result of this operation, a list of users will be returned,
        //with a successful message and outcome.
        response.put("list", users.get());
        response.put("outcome", true);
        response.put("message", "successfully processed request");

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<HashMap<String,Object>> deleteUser(HttpServletRequest request,@PathVariable("id") String id){

        HashMap<String, Object> response = new HashMap<>();

        if(!Gate.authorizedAsAdmin(request)){
            return Gate.abortUnauthorized();
        }

        User user = User.collection().where("id",id).getFirst();

        if(user == null){
            response.put("outcome",false);
            response.put("message","User not found");
            return new ResponseEntity<>(response,HttpStatusCode.valueOf(404));
        }

        ArrayList<SmartHomeHub> hubs = SmartHomeHub.collection().where("user_id",user.getId()).get();
        hubs.forEach((n)->{
            PlantData.deleteAll("PlantData","smarthomehub_id",n.getId());
            n.delete();
       });

        PlantName.deleteAll("PlantName","user_id",id);

        user.delete();
        response.put("outcome",true);
        response.put("message","User account and all data deleted");
        int outcome = 200;

        return new ResponseEntity<>(response,HttpStatusCode.valueOf(outcome));
    }

    @GetMapping("/user/{id}/smarthomehub/all")
    public ResponseEntity<HashMap<String,Object>> getSmartHubsForUser(HttpServletRequest request, @PathVariable String id){

        HashMap<String,Object> response = new HashMap<>();

        if(!Gate.authorizedAsAdmin(request)){
            return Gate.abortUnauthorized();
        }

        User user = User.collection().where("id",id).getFirst();

        if(user == null){
            response.put("outcome",false);
            response.put("message","User not found");
            return new ResponseEntity<>(response,HttpStatusCode.valueOf(404));
        }

        response.put("outcome",true);
        response.put("list",SmartHomeHub.collection().where("user_id",id).get());

        return new ResponseEntity<>(response,HttpStatusCode.valueOf(200));
    }
    @DeleteMapping("/user/{id}/smarthomehub/{hub_id}")
    public ResponseEntity<HashMap<String,Object>> deleteSmartHub(HttpServletRequest request, @PathVariable("id") String id, @PathVariable String hub_id){
        HashMap<String,Object> response = new HashMap<>();

        if(!Gate.authorizedAsAdmin(request)){
            return Gate.abortUnauthorized();
        }

        User user = User.collection().where("id",id).getFirst();

        if(user == null){
            response.put("outcome",false);
            response.put("message","User not found");
            return new ResponseEntity<>(response,HttpStatusCode.valueOf(404));
        }

        SmartHomeHub hub = SmartHomeHub.collection().where("user_id",id).where("id",hub_id).getFirst();

        if(hub == null){
            response.put("outcome",false);
            response.put("message","Smart home hub not found");
            return new ResponseEntity<>(response,HttpStatusCode.valueOf(404));
        }

        PlantData.deleteAll("PlantData","smarthomehub_id",hub.getId());
        hub.delete();

        response.put("outcome",true);
        response.put("message","Smart home hub deleted");

        return new ResponseEntity<>(response,HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/user/{id}/smarthomehub/{hub_id}/data")
    public ResponseEntity<HashMap<String,Object>> deleteSmartHubData(HttpServletRequest request, @PathVariable("id") String id, @PathVariable String hub_id){
        HashMap<String,Object> response = new HashMap<>();

        if(!Gate.authorizedAsAdmin(request)){
            return Gate.abortUnauthorized();
        }

        User user = User.collection().where("id",id).getFirst();

        if(user == null){
            response.put("outcome",false);
            response.put("message","User not found");
            return new ResponseEntity<>(response,HttpStatusCode.valueOf(404));
        }

        SmartHomeHub hub = SmartHomeHub.collection().where("user_id",id).where("id",hub_id).getFirst();

        if(hub == null){
            response.put("outcome",false);
            response.put("message","Smart home hub not found");
            return new ResponseEntity<>(response,HttpStatusCode.valueOf(404));
        }

        PlantData.deleteAll("PlantData","smarthomehub_id",hub.getId());

        response.put("outcome",true);
        response.put("message","Smart home hub deleted");

        return new ResponseEntity<>(response,HttpStatusCode.valueOf(200));
    }

    @PatchMapping("/user/{id}/promote")
    public ResponseEntity<HashMap<String,Object>> promoteAccountToAdmin(@PathVariable("id") String id, HttpServletRequest request){

        HashMap<String,Object> response = new HashMap<>();

        if(!Gate.authorizedAsAdmin(request)){
            return Gate.abortUnauthorized();
        }

        User user = User.collection().where("id",id).getFirst();

        if(user == null){
            response.put("outcome",false);
            response.put("message","User account not found");
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(404));
        }

        int outcome = 200;

        if(user.isAdministrator()){
            response.put("outcome",true);
            response.put("message","User account is already an administrator");
        }else{
            HashMap<String,Object> data = new HashMap<>();
            data.put("isAdministrator","1");

            if(user.update(data)){
                response.put("outcome",true);
                response.put("message","Successfully promoted user to administrator");
            }else{
                outcome = 500;
                response.put("message","Failed to promote user, please contact support");
            }
        }

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));
    }


    @PatchMapping("/user/{id}/demote")
    public ResponseEntity<HashMap<String,Object>> demoteAdminAccount(@PathVariable("id") String id, HttpServletRequest request){

        HashMap<String,Object> response = new HashMap<>();

        if(!Gate.authorizedAsAdmin(request)){
            return Gate.abortUnauthorized();
        }

        User user = User.collection().where("id",id).getFirst();

        if(user == null){
            response.put("outcome",false);
            response.put("message","User account not found");
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(404));
        }


        int outcome = 200;

        if(!user.isAdministrator()){
            response.put("outcome",true);
            response.put("message","User account is not currently a administrator");
        }else{
            HashMap<String,Object> data = new HashMap<>();
            data.put("isAdministrator","0");

            if(user.update(data)){
                response.put("outcome",true);
                response.put("message","Successfully demoted administrator to user");
            }else{
                outcome = 500;
                response.put("message","Failed to demote user, please contact support");
            }
        }

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));
    }

}
