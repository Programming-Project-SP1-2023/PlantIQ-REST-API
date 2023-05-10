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
//---------------------------------Controller Class---------------------------------
//The NotificationController class will be serving all the endpoints that deal
//with notifications and ranges that trigger the notifications.
//----------------------------------------------------------------------------------

@RestController
@RequestMapping("/notification")
public class NotificationController {
    //When the plants values are out of range, a notification will
    // be sent to the user. @GetMapping("/all") is the endpoint that
    //will display all the notifications related to that user.
    @GetMapping("/all")
    public ResponseEntity<HashMap<String, Object>> all(HttpServletRequest request) {
        //Create our response object, this is returned as JSON.
        HashMap<String, Object> response = new HashMap<>();
        //Authenticate the user with the gate service
        if (!Gate.authorized(request)) {
            return Gate.abortUnauthorized();
        }
        response.put("outcome", true);
        //Insert into the response all the notifications that are
        //related to the logged-in user.
        response.put("notifications", Notification.collection().where("user_id", Gate.getCurrentUser().getId()).get());
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }

    //@DeleteMapping("/all") is the endpoint that will delete all the
    //notifications related to the logged-in user.
    @DeleteMapping("/all")
    public ResponseEntity<HashMap<String, Object>> clear(HttpServletRequest request) {
        //Create our response object, this is returned as JSON.
        HashMap<String, Object> response = new HashMap<>();
        //Authenticate the user with the gate service
        if (!Gate.authorized(request)) {
            return Gate.abortUnauthorized();
        }
        //Attempt to delete each record in the notification entity which has the
        //user_id matching the id of the logged-in user. If the deletion is successful,
        //a true outcome and a successful message will be returned. If the operation is
        //not successful, a 500 error will be returned with an error statement.
        if (Notification.deleteAll("Notification", "user_id", Gate.getCurrentUser().getId())) {
            response.put("outcome", true);
            response.put("message", "Notifications deleted");
        } else {
            response.put("outcome", false);
            response.put("error", "Could not delete notification");
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(500));
        }
        response.put("notifications", Notification.collection().where("user_id", Gate.getCurrentUser().getId()).get());
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }

    //@DeleteMapping("/{id}") is the endpoint that will allow the user to delete
    //a single notification.
    @DeleteMapping("/{id}")
    public ResponseEntity<HashMap<String, Object>> dismiss(HttpServletRequest request) {
        //Create our response object, this is returned as JSON.
        HashMap<String, Object> response = new HashMap<>();
        //Authenticate the user with the gate service
        if (!Gate.authorized(request)) {
            return Gate.abortUnauthorized();
        }

        //Search for the notification that needs to be dismissed.
        Notification notification = Notification.collection().where("user_id", Gate.getCurrentUser().getId()).where("id", request.getParameter("id")).getFirst();
        //If an invalid ID was provided, resulting in a null value, a 500
        //error will be returned and a message stating "Could not find
        // notification". If the notification is valid an attempt of
        //deletion will be performed.
        if (notification != null) {
            //If the deletion is successful, a true outcome and a
            //successful message will be returned, otherwise an error
            //message and status code will be returned
            if (notification.delete()) {
                response.put("outcome", true);
                response.put("message", "Notification deleted");
            } else {
                response.put("outcome", false);
                response.put("error", "Could not delete notification");

                return new ResponseEntity<>(response, HttpStatusCode.valueOf(500));
            }
        } else {
            response.put("outcome", false);
            response.put("error", "Could not find notification");

            return new ResponseEntity<>(response, HttpStatusCode.valueOf(500));
        }
        //As part of the response, all the remaining notifications
        //relating to that user will be returned.
        response.put("notifications", Notification.collection().where("user_id", Gate.getCurrentUser().getId()).get());
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }

    //@GetMapping("/limits") is the endpoints used to retrieve the set
    //ranges for each field (temperature,humidity,light,moisture) of a
    //specific smarthub.
    @GetMapping("/limits")
    public ResponseEntity<HashMap<String, Object>> getLimits(HttpServletRequest request) {
        //Create our response object, this is returned as JSON.
        HashMap<String, Object> response = new HashMap<>();

        //Authenticate the user with the gate service
        if (!Gate.authorized(request)) {
            return Gate.abortUnauthorized();
        }
        //Create our notification rule. We will validate our
        //request using this rule.
        NotificationRule rule = new NotificationRule();
        //Validate our request and return the errors if present.
        if (!rule.validate(request)) {
            return rule.abort();
        }

        //Retrieving the ranges record from the Range entity for
        //the correct smarthub.
        Range range = Range.collection().where("smarthub_id", request.getParameter("smarthub_id")).getFirst();
        //Variable that will store the html status code.
        int outcome;
        //If no record has been found for the inputted smarthub,
        //a message will be returned with a 404 error. If a
        //record was found, it will retrieve the data for each
        //of the 4 fields and return it through the response.
        if (range == null) {
            response.put("outcome", false);
            response.put("error", "Ranges for Smart Home Hub not found");
            outcome = 404;
        } else {
            //Creation of hashmap used to send the ranges through
            //the response.
            HashMap<String, String> data = new HashMap<>();
            data.put("temperature", range.getRangeTemperature());
            data.put("humidity", range.getRangeHumidity());
            data.put("light", range.getRangeLight());
            data.put("moisture", range.getRangeMoisture());

            response.put("outcome", true);
            response.put("ranges", data);
            outcome = 200;
        }
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));
    }

    //@PatchMapping("/limits") is the endpoints used to change the set
    //ranges for each field (temperature,humidity,light,moisture) of a
    //specific smarthub.
    @PatchMapping("/limits")
    public ResponseEntity<HashMap<String, Object>> updateLimits(HttpServletRequest request) {
        //Create our response object, this is returned as JSON.
        HashMap<String, Object> response = new HashMap<>();
        //Authenticate the user with the gate service
        if (!Gate.authorized(request)) {
            return Gate.abortUnauthorized();
        }
        //Create our notification rule. We will validate our
        //request using this rule.
        NotificationRule rule = new NotificationRule();
        //Validate our request and return the errors if present.
        if (!rule.validate(request)) {
            return rule.abort();
        }

        //Creation of hashmap that is necessary to store
        //the changes that will need to be pushed to the
        //database.
        HashMap<String, Object> data = new HashMap<>();
        //Getting the smarthub_id
        String smarthubID = request.getParameter("smarthub_id");
        //Variable that will store the html status code
        int outcome;
        //Retrieving the ranges for the selected smarthub
        Range range = Range.collection().where("smarthub_id", smarthubID).getFirst();
        //If the ranges for the smarthub are not found, an error will be
        //returned, otherwise the insertion of the new ranges will occur.
        if (range != null) {
            response.put("outcome", false);
            response.put("error", "No record was found");
            outcome = 400;
        } else {
            data.put("temperature", request.getParameter("temperature"));
            data.put("humidity", request.getParameter("humidity"));
            data.put("light", request.getParameter("light"));
            data.put("moisture", request.getParameter("moisture"));
            data.put("smarthub_id", smarthubID);

            //If updating the ranges returned a true value,
            // the outcome will be set to true, and the ranges
            //will be returned into the response. If the operation
            //wasn't successful, the outcome will be set to false
            //and an error message will be returned.
            if (range.update(data)) {
                response.put("outcome", true);
                response.put("ranges", data);
                outcome = 200;
            } else {
                response.put("outcome", false);
                response.put("error", "No records were effected by change");
                outcome = 400;
            }
        }
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));
    }
    //@PostMapping("/limits") is the endpoint used to insert ranges values
    //for each on of the 4 fields.
    @PostMapping("/limits")
    public ResponseEntity<HashMap<String, Object>> insertLimits(HttpServletRequest request) {
        //Create our response object, this is returned as JSON.
        HashMap<String, Object> response = new HashMap<>();

        if (!Gate.authorized(request)) {
            return Gate.abortUnauthorized();
        }
        //Create our notification rule. We will validate our
        //request using this rule.
        NotificationRule rule = new NotificationRule();
        //Validate our request and return the errors if present.
        if (!rule.validate(request)) {
            return rule.abort();
        }
        //Creation of hashmap that is necessary to store
        //the values that will need to be inserted into
        //the Range entity.
        HashMap<String, Object> data = new HashMap<>();
        //Getting the smarthub_id
        String smarthubID = request.getParameter("smarthub_id");
        data.put("temperature", request.getParameter("temperature"));
        data.put("humidity", request.getParameter("humidity"));
        data.put("light", request.getParameter("light"));
        data.put("moisture", request.getParameter("moisture"));
        data.put("smarthub_id", smarthubID);
        //Variable that will store the html status code
        int outcome;
        //if the insertion did add records to the table, a true outcome
        //will be returned with the inserted ranges. If the insertion
        // did not affect any row of the database, a false outcome and
        // an error message will be returned.
        if (Range.insert("Range", data)) {
            response.put("outcome", true);
            response.put("ranges", data);
            outcome = 200;
        } else {
            response.put("outcome", false);
            response.put("error", "No records were effected by change");
            outcome = 400;
        }
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));
    }
    //@DeleteMapping("/limits") is the endpoint that resets the personalised
    //ranges back to the default.
    @DeleteMapping("/limits")
    public ResponseEntity<HashMap<String, Object>> deleteLimits(HttpServletRequest request) {
        //Authenticate the user with the gate service/
        if (!Gate.authorized(request)) {
            return Gate.abortUnauthorized();
        }
        //Create our notification rule. We will validate our
        //request using this rule.
        NotificationRule rule = new NotificationRule();
        //Validate our request and return the errors if present.
        if (!rule.validate(request)) {
            return rule.abort();
        }
        //Create our response object, this is returned as JSON.
        HashMap<String, Object> response = new HashMap<>();
        //Creation of hashmap that is necessary to store
        //the changes that will need to be pushed to the
        //database.
        HashMap<String, Object> data = new HashMap<>();
        //Getting the smarthub_id.
        String smarthubID = request.getParameter("smarthub_id");
        data.put("temperature", Range.DEFAULT_TEMPERATURE_RANGE);
        data.put("humidity", Range.DEFAULT_HUMIDITY_RANGE);
        data.put("light", Range.DEFAULT_LIGHT_RANGE);
        data.put("moisture", Range.DEFAULT_MOISTURE_RANGE);
        data.put("smarthub_id", request.getParameter(smarthubID));
        //Variable used to store the html status code.
        int outcome;
        //If the operation was successful, a true outcome will be returned
        //with the new ranges. While, if the query did not affect any row
        //of the database, an error message and a false outcome will be returned
        if (Range.collection().where("smarthub_id", smarthubID).getFirst().update(data)) {
            response.put("outcome", true);
            response.put("ranges", data);
            outcome = 200;
        } else {
            response.put("outcome", false);
            response.put("error", "No records were effected by change");
            outcome = 400;
        }
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));
    }


}
