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

//---------------------------------Controller Class---------------------------------
//The AdminController class will be serving all the endpoints used to reproduce the
//dedicate features for PlantIQ administrators.
//----------------------------------------------------------------------------------


@RestController
@RequestMapping("/admin")
public class AdminController {

    //@PostMapping("/user/activate/{id}") is the endpoint that will allow admins
    //to manually activate user's account, skipping the default procedure of activating
    //accounts through email confirmation.
    @PostMapping("/user/activate/{id}")
    public ResponseEntity<HashMap<String,Object>> activateAccount(@PathVariable("id") String id, HttpServletRequest request){
        //Create our response object, this is returned as JSON.
        HashMap<String,Object> response = new HashMap<>();
        //Authenticate the user with the gate service
        if(!Gate.authorizedAsAdmin(request)){
            return Gate.abortUnauthorized();
        }
        //Retrieve the user object of the account that is going to be activated
        User user = User.collection().where("id",id).getFirst();
        //If no user was found, the id provided does not exist.
        //In this case the outcome will be false and a 404
        //error will be thrown.
        if(user == null){
            response.put("outcome",false);
            response.put("message","User account not found");
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(404));
        }

        //Variable that will store the html status code.
        int outcome = 200;

        //If statement that will check if the user's account
        //is already active. In this case a true outcome will
        //be returned with a message stating that the account
        //has been already activate. If the account has not been
        //already activate, an attempt to activate the account
        //will be performed.
        if(user.isActivated()){
            response.put("outcome",true);
            response.put("message","User account is already activated");
        }else{
            HashMap<String,Object> data = new HashMap<>();
            data.put("isActivated","1");
            //If the attempt of activating the account does affect
            //the database, a true outcome and a message stating
            //the successful operation will be returned. If no row
            //was affected within the database, a 500 http status
            //code will be returned with and error message.
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
    //@DeleteMapping("/user/{id}") is the endpoint that allows admins to erase user's accounts.
    //With the deletion of the account, also the smarthomehubs and plants relating to that
    //account will be deleted.
    @DeleteMapping("/user/{id}")
    public ResponseEntity<HashMap<String,Object>> deleteUser(HttpServletRequest request,@PathVariable("id") String id){
        //Create our response object, this is returned as JSON.
        HashMap<String, Object> response = new HashMap<>();

        //Authenticate the user with the gate service
        if(!Gate.authorizedAsAdmin(request)){
            return Gate.abortUnauthorized();
        }

        //Retrieve the user object of the account that is going to be deleted
        User user = User.collection().where("id",id).getFirst();
        //If no user was found, the id provided does not exist.
        //In this case the outcome will be false and a 404
        //error will be thrown.
        if(user == null){
            response.put("outcome",false);
            response.put("message","User not found");
            return new ResponseEntity<>(response,HttpStatusCode.valueOf(404));
        }

        //Since the account does exist, the smarthub and all their plants
        //will need to be retrieved and deleted. Following is the retrieval
        //of the list of smarthomehubs relating to the user.
        ArrayList<SmartHomeHub> hubs = SmartHomeHub.collection().where("user_id",user.getId()).get();
        //For each smarthomehub present in the list, delete all the plants
        //related to it. Finally, delete the hub.
        hubs.forEach((n)->{
            PlantData.deleteAll("PlantData","smarthomehub_id",n.getId());
            n.delete();
       });
        //Deleting all record for plantNames that relate to the used
        //getting deleted.
        PlantName.deleteAll("PlantName","user_id",id);
        //Delete the user
        user.delete();
        //Once the user and all the hubs and plants related to them have been
        //deleted, return a true outcome and a message confirming the removal.
        response.put("outcome",true);
        response.put("message","User account and all data deleted");

        return new ResponseEntity<>(response,HttpStatusCode.valueOf(200));
    }

    //@GetMapping("/user/{id}/smarthomehub/all") is the endpoint used by admins to get all the
    //smarthubs relating to a single user.
    @GetMapping("/user/{id}/smarthomehub/all")
    public ResponseEntity<HashMap<String,Object>> getSmartHubsForUser(HttpServletRequest request, @PathVariable String id){
        //Create our response object, this is returned as JSON.
        HashMap<String,Object> response = new HashMap<>();

        //Authenticate the user with the gate service
        if(!Gate.authorizedAsAdmin(request)){
            return Gate.abortUnauthorized();
        }

        //Retrieve the user object of the account who's smarthomehubs are going
        //to be displayed
        User user = User.collection().where("id",id).getFirst();

        //If no user was found, the id provided does not exist.
        //In this case the outcome will be false and a 404
        //error will be thrown.
        if(user == null){
            response.put("outcome",false);
            response.put("message","User not found");
            return new ResponseEntity<>(response,HttpStatusCode.valueOf(404));
        }
        //If the program reaches this stage, a user was found.
        //A true outcome and a list of the user's smarthomehub
        //will be returned.
        response.put("outcome",true);
        response.put("list",SmartHomeHub.collection().where("user_id",id).get());

        return new ResponseEntity<>(response,HttpStatusCode.valueOf(200));
    }

    //@DeleteMapping("/user/{id}/smarthomehub/{hub_id}") is the endpoint used by admins to
    //delete a single smarthomehub for any user.
    @DeleteMapping("/user/{id}/smarthomehub/{hub_id}")
    public ResponseEntity<HashMap<String,Object>> deleteSmartHub(HttpServletRequest request, @PathVariable("id") String id, @PathVariable String hub_id){
        //Create our response object, this is returned as JSON.
        HashMap<String,Object> response = new HashMap<>();

        //Authenticate the user with the gate service
        if(!Gate.authorizedAsAdmin(request)){
            return Gate.abortUnauthorized();
        }

        //Retrieve the user object of the account who's smarthomehubs is going to be deleted.
        User user = User.collection().where("id",id).getFirst();
        //If no user was found, the id provided does not exist.
        //In this case the outcome will be false and a 404
        //error will be thrown.
        if(user == null){
            response.put("outcome",false);
            response.put("message","User not found");
            return new ResponseEntity<>(response,HttpStatusCode.valueOf(404));
        }
        //Retrieve smarthomehub that is going to be deleted.
        SmartHomeHub hub = SmartHomeHub.collection().where("user_id",id).where("id",hub_id).getFirst();
        //If no hub was found, the id provided does not exist.
        //In this case the outcome will be false and a 404
        //error will be thrown.
        if(hub == null){
            response.put("outcome",false);
            response.put("message","Smart home hub not found");
            return new ResponseEntity<>(response,HttpStatusCode.valueOf(404));
        }
        //Now that user and hub have been retrieved, all the plants relating to
        //the hub that is going to be deleted must also be deleted
        PlantData.deleteAll("PlantData","smarthomehub_id",hub.getId());
        //Finally, delete the Hub
        hub.delete();
        //Plants and hub are now deleted, so a true outcome will be thrown with a
        //message confirming the removal.
        response.put("outcome",true);
        response.put("message","Smart home hub deleted");

        return new ResponseEntity<>(response,HttpStatusCode.valueOf(200));
    }

    //@DeleteMapping("/user/{id}/smarthomehub/{hub_id}/data") is the endpoint that allows the admins to delete
    //----------------------------------------COMPLETE THIS1-------------------------------------------------------------------
    @DeleteMapping("/user/{id}/smarthomehub/{hub_id}/data")
    public ResponseEntity<HashMap<String,Object>> deleteSmartHubData(HttpServletRequest request, @PathVariable("id") String id, @PathVariable String hub_id){
        //Create our response object, this is returned as JSON.
        HashMap<String,Object> response = new HashMap<>();
        //Authenticate the user with the gate service
        if(!Gate.authorizedAsAdmin(request)){
            return Gate.abortUnauthorized();
        }
        //Retrieve the user object of the account who's smarthomehubs's data is going to be deleted.
        User user = User.collection().where("id",id).getFirst();

        //If no user was found, the id provided does not exist.
        //In this case the outcome will be false and a 404
        //error will be thrown.
        if(user == null){
            response.put("outcome",false);
            response.put("message","User not found");
            return new ResponseEntity<>(response,HttpStatusCode.valueOf(404));
        }
        //Retrieve smarthomehub who's data is going to be deleted.
        SmartHomeHub hub = SmartHomeHub.collection().where("user_id",id).where("id",hub_id).getFirst();

        //If no hub was found, the id provided does not exist.
        //In this case the outcome will be false and a 404
        //error will be thrown.
        if(hub == null){
            response.put("outcome",false);
            response.put("message","Smart home hub not found");
            return new ResponseEntity<>(response,HttpStatusCode.valueOf(404));
        }
        //---------------------------Complete this2-------------------------------
        PlantData.deleteAll("PlantData","smarthomehub_id",hub.getId());

        response.put("outcome",true);
        response.put("message","Smart home hub deleted");

        return new ResponseEntity<>(response,HttpStatusCode.valueOf(200));
    }
    //@PatchMapping("/user/{id}/promote") is the endpoint used by admins to promote
    //a normal user to the role of admin.
    @PatchMapping("/user/{id}/promote")
    public ResponseEntity<HashMap<String,Object>> promoteAccountToAdmin(@PathVariable("id") String id, HttpServletRequest request){
        //Create our response object, this is returned as JSON.
        HashMap<String,Object> response = new HashMap<>();
        //Authenticate the user with the gate service
        if(!Gate.authorizedAsAdmin(request)){
            return Gate.abortUnauthorized();
        }
        //Retrieve the user object that is going to be promoted.
        User user = User.collection().where("id",id).getFirst();

        //If no user was found, the id provided does not exist.
        //In this case the outcome will be false and a 404
        //error will be thrown.
        if(user == null){
            response.put("outcome",false);
            response.put("message","User account not found");
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(404));
        }

        //Variable that will store the html status code.
        int outcome = 200;

        //If statement that checks if the user is currently a normal user
        //or ad admin. If the user is an admin, a true outcome will be returned
        // and flanked by a message stating that the user is already an admin.
        //If the user is a normal user instead, an attempt to upadte the
        //user's role will be done.
        if(user.isAdministrator()){
            response.put("outcome",true);
            response.put("message","User account is already an administrator");
        }else{
            HashMap<String,Object> data = new HashMap<>();
            data.put("isAdministrator","1");
            //If the user's role is updated correctly, a true outcome with a
            //message confirming the promotion will be returned, while a 500
            //html status code will be returned with an error message if no
            //row within the database was affected by the promotion query.
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
    //@PatchMapping("/user/{id}/demote") is the endpoint used by admins to demote
    //an admin to a normal user.
    @PatchMapping("/user/{id}/demote")
    public ResponseEntity<HashMap<String,Object>> demoteAdminAccount(@PathVariable("id") String id, HttpServletRequest request){
        //Create our response object, this is returned as JSON.
        HashMap<String,Object> response = new HashMap<>();

        //Authenticate the user with the gate service
        if(!Gate.authorizedAsAdmin(request)){
            return Gate.abortUnauthorized();
        }

        //Retrieve the user object that is going to be demoted.
        User user = User.collection().where("id",id).getFirst();

        //If no user was found, the id provided does not exist.
        //In this case the outcome will be false and a 404
        //error will be thrown.
        if(user == null){
            response.put("outcome",false);
            response.put("message","User account not found");
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(404));
        }

        //Variable that will store the html status code.
        int outcome = 200;

        //If statement that checks if the user is currently a normal user
        //or ad admin. If the user is not an admin, a true outcome will be returned
        //and flanked by a message stating that the user is not an admin.
        //If the user is an admin instead, an attempt to demote the user's
        //role will be done.
        if(!user.isAdministrator()){
            response.put("outcome",true);
            response.put("message","User account is not currently a administrator");
        }else{
            HashMap<String,Object> data = new HashMap<>();
            data.put("isAdministrator","0");
            //If the user's role is updated correctly, a true outcome with a
            //message confirming the demotion will be returned, while a 500
            //html status code will be returned with an error message if no
            //row within the database was affected by the promotion query.
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
