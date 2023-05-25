package com.plantiq.plantiqserver.controllers;

import com.plantiq.plantiqserver.core.Gate;
import com.plantiq.plantiqserver.model.Notification;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        int outcome;

        if (Notification.deleteAll("Notification", "user_id", Gate.getCurrentUser().getId())) {
            response.put("outcome", true);
            response.put("message", "Notifications deleted");
            outcome = 200;
        } else {
            response.put("outcome", false);
            response.put("error", "Could not delete notifications, please contact support");
            outcome = 500;
        }
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));
    }

    //@DeleteMapping("/{id}") is the endpoint that will allow the user to delete
    //a single notification.
    @DeleteMapping("/{id}")
    public ResponseEntity<HashMap<String, Object>> dismiss(HttpServletRequest request, @PathVariable String id) {
        //Create our response object, this is returned as JSON.
        HashMap<String, Object> response = new HashMap<>();
        //Authenticate the user with the gate service
        if (!Gate.authorized(request)) {
            return Gate.abortUnauthorized();
        }

        //Search for the notification that needs to be dismissed.
        Notification notification = Notification.collection().where("user_id", Gate.getCurrentUser().getId()).where("id", id).getFirst();
        //If an invalid ID was provided, resulting in a null value, a 500
        //error will be returned and a message stating "Could not find
        // notification". If the notification is valid an attempt of
        //deletion will be performed.

        int outcome;
        if (notification != null) {
            //If the deletion is successful, a true outcome and a
            //successful message will be returned, otherwise an error
            //message and status code will be returned
            outcome = 200;
            notification.delete();
            response.put("outcome", true);
            response.put("message", "Notification successfully deleted");

        } else {
            response.put("outcome", false);
            response.put("error", "Notification not found!");
            outcome = 404;
        }

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));
    }
}
