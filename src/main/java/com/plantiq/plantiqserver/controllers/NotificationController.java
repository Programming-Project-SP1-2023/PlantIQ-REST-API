package com.plantiq.plantiqserver.controllers;

import com.plantiq.plantiqserver.core.Gate;
import com.plantiq.plantiqserver.model.Notification;

import com.plantiq.plantiqserver.model.Range;
import com.plantiq.plantiqserver.rules.NotificationRule;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;


@RestController
@RequestMapping("/notification")
public class NotificationController {

    @GetMapping("/all")
    public ResponseEntity<HashMap<String,Object>>  all(HttpServletRequest request){
        HashMap<String, Object> response = new HashMap<>();
        if(!Gate.authorized(request)){
            return Gate.abortUnauthorized();
        }
        response.put("outcome",true);
        response.put("notifications", Notification.collection().where("user_id",Gate.getCurrentUser().getId()).get());
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }
    @DeleteMapping("/all")
    public ResponseEntity<HashMap<String,Object>>  clear(HttpServletRequest request){
        HashMap<String, Object> response = new HashMap<>();
        //Authenticate the user with the gate service
        if(!Gate.authorized(request)){
            return Gate.abortUnauthorized();
        }
        ArrayList<Notification> notifications =Notification.collection().where("user_id",Gate.getCurrentUser().getId()).get();
        if(Notification.deleteAll("Notification","user_id",Gate.getCurrentUser().getId())){
            response.put("outcome",true);
            response.put("message","Notifications deleted");
        }else{
            response.put("outcome",false);
            response.put("error","Could not delete notification");
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(500));
        }

        response.put("notifications", Notification.collection().where("user_id",Gate.getCurrentUser().getId()).get());
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HashMap<String,Object>>  dismiss(HttpServletRequest request){
        HashMap<String, Object> response = new HashMap<>();
        //Authenticate the user with the gate service
        if(!Gate.authorized(request)){
            return Gate.abortUnauthorized();
        }

        //Search for the notification that needs to be dismissed
        Notification notification =Notification.collection().where("user_id",Gate.getCurrentUser().getId()).where("id",request.getParameter("id")).getFirst();
        if(notification.delete()){
                response.put("outcome",true);
                response.put("message","Notification deleted");
        }else{
            response.put("outcome",false);
            response.put("error","Could not delete notification");

            return new ResponseEntity<>(response, HttpStatusCode.valueOf(500));
        }

        response.put("notifications", Notification.collection().where("user_id",Gate.getCurrentUser().getId()).get());
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }



    @GetMapping("/limits")
    public ResponseEntity<HashMap<String,Object>> getLimits(HttpServletRequest request){
        HashMap<String, Object> response = new HashMap<>();

        ///Authenticate the user with the gate service
        if(!Gate.authorized(request)){
            return Gate.abortUnauthorized();
        }

        NotificationRule rule = new NotificationRule();
        //Validate our request rule
        if(!rule.validate(request)){
            return rule.abort();
        }

//        ---------------------------------------------------------CHECK PARAMETERS-------------------------------------
        Range range = Range.collection().where("smarthub_id",request.getParameter("smarthub_id")).getFirst();
        int outcome;
        if(range==null) {
            response.put("outcome",false);
            response.put("error","Ranges for Smart Home Hub not found");
            outcome = 404;
        }else{
            HashMap<String, String> data = new HashMap<>();
            data.put("temperature", range.getRangeTemperature());
            data.put("humidity", range.getRangeHumidity());
            data.put("light", range.getRangeLight());
            data.put("moisture", range.getRangeMoisture());

            response.put("outcome",true);
            response.put("ranges",data);
            outcome = 200;
        }
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));
    }
    @PatchMapping("/limits")
    public ResponseEntity<HashMap<String,Object>> updateLimits(HttpServletRequest request){

        //Authenticate the user with the gate service
        if(!Gate.authorized(request)){
            return Gate.abortUnauthorized();
        }

        NotificationRule rule = new NotificationRule();
        //Validate our request rule
        if(!rule.validate(request)){
            return rule.abort();
        }

        HashMap<String, Object> response = new HashMap<>();
        HashMap<String, Object> data = new HashMap<>();
        String smarthubID = request.getParameter("smarthub_id");
        Range range = Range.collection().where("smarthub_id",smarthubID).getFirst();
        //        ---------------------------------------------------------CHECK PARAMETERS-------------------------------------
        data.put("temperature", request.getParameter("temperature"));
        data.put("humidity", request.getParameter("humidity"));
        data.put("light", request.getParameter("light"));
        data.put("moisture", request.getParameter("moisture"));
        data.put("smarthub_id",smarthubID);
        int outcome;
        if(range.update(data)){
            response.put("outcome",true);
            response.put("ranges",data);
            outcome=200;
        }else{
            response.put("outcome",false);
            response.put("error","No records were effected by change");
//            ----------------------------------------------------------------Check status code
            outcome=400;
        }
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));
    }
    @PostMapping("/limits")
    public ResponseEntity<HashMap<String,Object>> insertLimits(HttpServletRequest request){
        if(!Gate.authorized(request)){
            return Gate.abortUnauthorized();
        }
        HashMap<String, Object> response = new HashMap<>();
        HashMap<String, Object> data = new HashMap<>();
        String smarthubID = request.getParameter("smarthub_id");
        //        ---------------------------------------------------------CHECK PARAMETERS-------------------------------------
        data.put("temperature", request.getParameter("temperature"));
        data.put("humidity", request.getParameter("humidity"));
        data.put("light", request.getParameter("light"));
        data.put("moisture", request.getParameter("moisture"));
        data.put("smarthub_id",smarthubID);
        int outcome;
        if(Range.insert("Range",data)){
            response.put("outcome",true);
            response.put("ranges",data);
            outcome=200;
        }else{
            response.put("outcome",false);
            response.put("error","No records were effected by change");
//            ----------------------------------------------------------------Check status code
            outcome=400;
        }
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));
    }
    @DeleteMapping("/limits")
    public ResponseEntity<HashMap<String,Object>> deleteLimits(HttpServletRequest request){
        if(!Gate.authorized(request)){
            return Gate.abortUnauthorized();
        }

        HashMap<String, Object> response = new HashMap<>();
        HashMap<String, Object> data = new HashMap<>();
        String smarthubID = request.getParameter("smarthub_id");
        data.put("temperature", Range.DEFAULT_TEMPERATURE_RANGE);
        data.put("humidity", Range.DEFAULT_HUMIDITY_RANGE);
        data.put("light", Range.DEFAULT_LIGHT_RANGE);
        data.put("moisture", Range.DEFAULT_MOISTURE_RANGE);
        data.put("smarthub_id",request.getParameter(smarthubID));
        int outcome;
        if(Range.collection().where("smarthub_id",smarthubID).getFirst().update(data)){
            response.put("outcome",true);
            response.put("ranges",data);
            outcome=200;
        }else {
            response.put("outcome", false);
            response.put("error", "No records were effected by change");
//            ----------------------------------------------------------------Check status code
            outcome = 400;
        }
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));
    }


}
