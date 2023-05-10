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
//---------------------------------Controller Class---------------------------------
//The SmartHubController class will be serving all the endpoints that deal with
//data of SmartHubs and Plants
//----------------------------------------------------------------------------------


@RestController
@RequestMapping("/smarthub")
public class SmartHubController {
    //@PostMapping("/{id}/data") is the endpoint that serves three main functionalities.
    //It adds unregistered smarthubs to the awaiting list, it adds plant data for
    //registered smarthubs and creates notifications when the plant values are out of range.
    @PostMapping("/{id}/data")
    public ResponseEntity<HashMap<String, Object>> savePlantData(@PathVariable("id") String id, HttpServletRequest request) {
        //Create our response object, this is returned as JSON.
        HashMap<String, Object> response = new HashMap<>();
        //Validate our id length is with in expected standards.
        //If not, return a false outcome and an error message
        if (id.length() != 12) {
            response.put("error", "Smart home hub identifier did not meet expected standard");
            response.put("outcome", false);
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(400));
        }
        //Retrieving the awaiting registration that has the matching id as the passed parameter.
        AwaitingRegistration awaitingRegistration = AwaitingRegistration.collection().where("id", id).getFirst();
        //If there is an awaiting registration, a message
        //will be return stating that the smarthub is waiting on registration.
        if (awaitingRegistration != null) {
            response.put("outcome", true);
            response.put("message", "Awaiting registration");
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
        }

        //Get the smart home hub model object for this id.
        SmartHomeHub smartHomeHub = SmartHomeHub.collection().where("id", id).getFirst();

        //Check the ID is valid and if not set it to await registration.
        //If the retrieved smarthub is null, it means that there is no smarthub registered with that id.
        // This id smarthub will then be moved to the awaiting queue. An appropriate message will be
        //returned with the 425 html status code.
        if (smartHomeHub == null) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("id", id);
            data.put("date", TimeService.nowPlusDays(7));
            AwaitingRegistration.insert("AwaitingRegistration", data);

            response.put("outcome", false);
            response.put("message", "Smart Home Hub Awaiting Registration");

            return new ResponseEntity<>(response, HttpStatusCode.valueOf(425));
        }

        //If we get to this stage we know the hub is valid and registered,
        // and we then process our data validation.

        //Create our post sensor data rule, we will validate our request
        //using this rule.
        PostSensorDataRule rule = new PostSensorDataRule();
        //Validate our request and return the errors if present.
        if (!rule.validate(request)) {
            return rule.abort();
        }

        //Hashmap that will store attributes and values for the plant data that
        //is going to be inserted into the database.
        HashMap<String, Object> data = new HashMap<>();
        data.put("smarthomehub_Id", smartHomeHub.getId());
        data.put("sensor_Id", request.getParameter("sensor_id"));
        data.put("temperature", request.getParameter("temperature"));
        data.put("humidity", request.getParameter("humidity"));
        data.put("light", request.getParameter("light"));
        data.put("moisture", request.getParameter("moisture"));
        data.put("timestamp", TimeService.now().toString());

        //Variable that will store the html status code
        int outcome;
        //If inserting data does affect the database as expected, a 200 html
        //status code will be returned and the outcome will be set to true.
        //If no rows of the database were affected, an error message will be
        // returned and a html status code of 500 will be returned.
        if (PlantData.insert("PlantData", data)) {
            response.put("outcome", true);
            outcome = 200;

        } else {
            response.put("error", "Failed to save plant data, please contact support!");
            response.put("outcome", false);
            outcome = 500;
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));
        }
        //Hashmap that will store attributes and values for the notification data that
        //is going to be inserted into the database.
        HashMap<String, Object> notificationData = new HashMap<>();
        //Getting the user_id of the owner of the smarthub.
        String userID = SmartHomeHub.collection().where("id", id).getFirst().getUser_id();
        notificationData.put("user_id", userID);
        notificationData.put("timestamp", TimeService.now());
        //Retrieving the range associated to the smarthub.
        Range range = Range.collection().where("smarthub_id", id).getFirst();
        //Splitting the ranges for the appropriate fields
        String[] ranges = {range.getRangeTemperature(), range.getRangeMoisture(), range.getRangeLight(), range.getRangeHumidity()};
        String[] fields = {"temperature", "moisture", "light", "humidity"};
        //For Loop to ensure value is in range for each field.
        //If not in range a notification will be created.
        for (int i = 0; i < 4; i++) {
            String field = fields[i];
            String min = "";
            String max = "";
            //Splitting the data into the correct variables.
            // If an error is thrown, an error stating that
            // the ranges were not saved in the correct format
            //in the database will bhe returned.
            try {
                min = ranges[i].split("-")[0];
                max = ranges[i].split("-")[1];
            } catch (NullPointerException e) {
                response.put("error", "Ranges are not in the right format");
                response.put("outcome", false);
                outcome = 500;
                return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));
            }
            //If the value is not in range, a notification will be created
            //containing all the necessary data.
            if (Float.parseFloat(request.getParameter(field)) < Float.parseFloat(min) && Float.parseFloat(request.getParameter(field)) > Float.parseFloat(max)) {
                //Retrieving last notification for the specifield field and user
                Notification lastNotification = Notification.collection().where("field", field).orderBy("timestamp").orderType(Sort.DESC).getFirst();
                //In order to not spam emails to the user, we have set a cooldown
                //of 24 hours. This if statement to check if 24 hours have passed
                //from the last notification for that field. If more than 24 hours
                //have passed, a new notification can be sent.
                if (TimeService.nowMinusDays(1) > lastNotification.getTimestamp()) {
                    String value = request.getParameter(field);
                    notificationData.put("field", field);
                    notificationData.put("value", value);
                    notificationData.put("user_id", userID);
                    notificationData.put("timestamp", TimeService.now());
                    notificationData.put("message", "");
                    //If inserting the notification does affect the database as expected,
                    //the html statuc code will be set to 200 and the outcome will be true.
                    //If not,an error message will be returned and the status code will be 500.
                    if (Notification.insert("Notification", notificationData)) {
                        Email email = new Email();
                        email.setUser(User.collection().where("user_id", userID).getFirst())
                                .setHtmlTemplate("/emails/outOfRangeNotification.html")
                                .setSubject("Plant out of Range")
                                .setVariables(data)
                                .send();
                        response.put("outcome", true);
                        outcome = 200;

                    } else {
                        response.put("error", "Failed to save notification data, please contact support!");
                        response.put("outcome", false);
                        outcome = 500;
                    }

                }
            }
        }

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));
    }

    @DeleteMapping("/{id}/data")
    public ResponseEntity<HashMap<String, Object>> deletePlantData(@PathVariable("id") String id, HttpServletRequest request) {

        HashMap<String, Object> response = new HashMap<>();

        if (!Gate.authorized(request)) {
            return Gate.abortUnauthorized();
        }

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));

    }

    //@GetMapping("/{id}/rate") is the endpoint used to retrieve the post frequency of a hub.
    //If the hub isn't valid, this will be added to the awaiting registration list.
    @GetMapping("/{id}/rate")
    public ResponseEntity<HashMap<String, Object>> getPlantDataUploadRate(@PathVariable("id") String id) {
        //Create our response object, this is returned as JSON.
        HashMap<String, Object> response = new HashMap<>();

        //Validate our id length is with in expected standards.
        //If not, return a false outcome and an error message
        if (id.length() != 12) {
            response.put("error", "Smart home hub identifier did not meet expected standard");
            response.put("outcome", false);
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(400));
        }

        //Get the smarthub with the matching id
        SmartHomeHub hub = SmartHomeHub.collection().where("id", id).getFirst();
        //Variable that will store the html status code
        int outcome;
        //If the smarthub is null, the smarthub isn't registered yet.
        //In this case, the smarthub will need to pass the registration process first.
        if (hub == null) {
            //Retrieving the awaiting registration of the inputted smarthub.
            AwaitingRegistration awaitingRegistration = AwaitingRegistration.collection().where("id", id).getFirst();
            //If the awaiting registration isn't null, the smarthub just need
            //to hit the /register endpoint before functioning. If the awaiting
            //registration instead is null, the smarthub will be added to the
            //awaiting list.
            if (awaitingRegistration == null) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("id", id);
                data.put("date", TimeService.nowPlusDays(7));
                AwaitingRegistration.insert("AwaitingRegistration", data);
            }
            //In both cases, /register will need to be hit before the smarthub
            //can be used. The returning html status code will be 425 and the
            //returning message will state that the hub is awaiting registration.
            outcome = 425;
            response.put("outcome", false);
            response.put("message", "Smart Home Hub Awaiting Registration");

        } else {
            //If the smarthub is not null, the post frequency will be
            //returned with a true outcome
            outcome = 200;
            response.put("outcome", true);
            response.put("rate", hub.getPostFrequency());
        }


        return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));

    }

    //@GetMapping("/all") is the endpoint that will return all the smarthubs of a single user.
    @GetMapping("/all")
    public ResponseEntity<HashMap<String, Object>> getAll(HttpServletRequest request) {
        //Create our response object, this is returned as JSON.
        HashMap<String, Object> response = new HashMap<>();

        //Authenticate the user with the gate service
        if (!Gate.authorized(request)) {
            return Gate.abortUnauthorized();
        }

        //Create our get all rule. We will validate our request
        //using this rule.
        GetAllRule rule = new GetAllRule();
        //Validate our request and return the errors if present.
        if (!rule.validate(request)) {
            return rule.abort();
        }

        //Add a valid response outcome
        response.put("outcome", true);

        //Create our hubs object.
        ModelCollection<SmartHomeHub> hubs = SmartHomeHub.collection().where("user_id", Gate.getCurrentUser().getId());

        //If virtual is passed then add it.
        if (request.getParameterMap().containsKey("virtual")) {
            hubs.where("virtual", request.getParameter("virtual"));
        }

        //Add all the required parameters.
        hubs.limit(Integer.parseInt(request.getParameter("limit")));
        hubs.offset(Integer.parseInt(request.getParameter("offset")));
        hubs.orderBy(request.getParameter("sortBy"));
        hubs.orderType(Sort.valueOf(request.getParameter("sort")));
        //Retrieve all the smarthubs with the matching parameters.
        ArrayList<SmartHomeHub> output = hubs.get();

        //Check if the user has any hubs by the size and either add them or a message.
        if (output.size() == 0) {
            response.put("message", "No hubs found matching criteria currently registered with user");
        } else {
            response.put("list", output);
        }

        //Lastly return the response.
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }

    //@PostMapping("/register") is the endpoint where the smarthub will leave
    //the awaiting list and become a valid smarthub. This will then send a email
    //to the user confirming the creation of a hub.
    @PostMapping("/register")
    public ResponseEntity<HashMap<String, Object>> register(HttpServletRequest request) {
        //Create our response object, this is returned as JSON.
        HashMap<String, Object> response = new HashMap<>();

        //Authenticate the user with the gate service
        if (!Gate.authorized(request)) {
            return Gate.abortUnauthorized();
        }

        //Create our register smarthub rule, we will validate our
        //request using this rule.
        RegisterSmartHubRule rule = new RegisterSmartHubRule();

        //Validate our request rule
        if (!rule.validate(request)) {
            return rule.abort();
        }

        //Create our new model data hashmap
        HashMap<String, Object> data = new HashMap<>();
        data.put("id", request.getParameter("id"));
        data.put("deviceSpecificPassword", HashService.generateSHA1(request.getParameter("id") + TimeService.now()));
        data.put("name", request.getParameter("name"));
        data.put("user_id", Gate.getCurrentUser().getId());
        data.put("lastPosted", TimeService.now());
        data.put("postFrequency", "3600");

        //If we are trying to register a virtual smarthub, this will need
        //to be passed as a parameter and added to the hashmap data, so
        //that it can be inserted into the database.
        if (request.getParameterMap().containsKey("virtual")) {
            data.put("virtual", request.getParameter("virtual"));
        } else {
            data.put("virtual", false);
        }

        //Get the current object from the awaiting registration table
        AwaitingRegistration smartHubAwaitingRegistration = AwaitingRegistration.collection().where("id", request.getParameter("id")).getFirst();

        //Delete the object from the table by its id.
        smartHubAwaitingRegistration.delete("id");


        //Attempt to insert the smarthub into the database,
        //If successful keep the outcome true, else return false,
        //and an error message
        if (!SmartHomeHub.insert("SmartHomeHub", data)) {
            response.put("outcome", false);
            response.put("errors", "Failed to register smart hub, please contact support!");
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(500));
        }
        //Attempt to insert the default ranges into the database,
        //If successful keep the outcome true, else return false,
        //and an error message
        if (!Range.insertDefaults(request.getParameter("id"))) {
            response.put("outcome", false);
            response.put("errors", "Failed to perform action, please contact support");
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(500));
        }

        //If the code reached this line, the two previous error check did not generate any errors
        response.put("outcome", true);
        response.put("message", "Smart Hub registered to account");
        //Send the user a confirmation email regarding the creation of a smarthub.
        Email email = new Email();
        email.setUser(Gate.getCurrentUser())
                .setHtmlTemplate("/emails/smartHubRegistrationConfirmation.html")
                .setSubject("New Smart Hub Registered")
                .setVariables(data)
                .send();

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }

    //@GetMapping("/{id}/data") is the endpoint used to retrieve all the
    //plant data of a specific smarthub.
    @GetMapping("/{id}/data")
    public ResponseEntity<HashMap<String, Object>> getData(@PathVariable("id") String id, HttpServletRequest request) {
        //Create our response object, this is returned as JSON.
        HashMap<String, Object> response = new HashMap<>();

        //Authenticate the user with the gate service
        if (!Gate.authorized(request)) {
            return Gate.abortUnauthorized();
        }
        //Variable that will store the html status code.
        int outcome;

        //If the smarthub is not found set the status code to 404 and
        //return an error message, while if a smarthub was found,
        // return a 200 status code and the plantdata relating to the
        //smarthub.
        if (SmartHomeHub.collection().where("id", id).where("user_id", Gate.getCurrentUser().getId()).getFirst() == null) {
            response.put("outcome", false);
            response.put("error", "Smart Home Hub not found");
            outcome = 404;
        } else {
            response.put("outcome", true);
            response.put("data", PlantData.collection().where("smarthomehub_Id", id).get());
            outcome = 200;

        }

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));
    }

    //@PatchMapping("/{id}") this endpoint will be used to update the post
    //frequency for a smarthub.
    @PatchMapping("/{id}")
    public ResponseEntity<HashMap<String, Object>> updateHub(@PathVariable("id") String id, HttpServletRequest request) {
        //Create our response object, this is returned as JSON.
        HashMap<String, Object> response = new HashMap<>();
        //Authenticate the user with the gate service
        if (!Gate.authorized(request)) {
            return Gate.abortUnauthorized();
        }
        //Retrieve the smarthub object with the matching id
        SmartHomeHub smartHomeHub = SmartHomeHub.collection().where("id", id).getFirst();
        //Variable that will store the html status code.
        int outcome;
        //If the smarthub is null, return a 404 error, else update the data.
        if (smartHomeHub == null) {
            response.put("outcome", false);
            outcome = 404;
        } else {
            //Create our update smarthub details rule. We will
            // validate our request using this rule.
            UpdateSmartHubDetailsRule rule = new UpdateSmartHubDetailsRule();
            //Validate our request and return the errors if present.
            if (!rule.validate(request)) {
                return rule.abort();
            }
            //Create the hashmap that will store the data used to
            //updated the entity in the database.
            HashMap<String, Object> data = new HashMap<>();
            data.put("name", request.getParameter("name"));
            data.put("postFrequency", request.getParameter("postFrequency"));
            //If updating the table was successful, return a 200 status code
            //and a successful message, else return a 500 status code and an
            //error message
            if (smartHomeHub.update(data)) {
                outcome = 200;
                response.put("outcome", true);
                response.put("message", "Successfully updated!");
            } else {
                outcome = 500;
                response.put("outcome", false);
                response.put("message", "Update error, please contact support");
            }
        }

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));
    }

    //@DeleteMapping("/{id}") is the endpoint used to delete the smarthub and all
    //the plant data related to that specific smarthun
    @DeleteMapping("/{id}")
    public ResponseEntity<HashMap<String, Object>> deleteHub(@PathVariable("id") String id, HttpServletRequest request) {
        //Create our response object, this is returned as JSON.
        HashMap<String, Object> response = new HashMap<>();
        //Authenticate the user with the gate service
        if (!Gate.authorized(request)) {
            return Gate.abortUnauthorized();
        }
        //Retrieve the correct smarthub
        SmartHomeHub smartHomeHub = SmartHomeHub.collection().where("id", id).where("user_id", Gate.getCurrentUser().getId()).getFirst();
        //Variable that will store the html status code.
        int outcome;
        //if the smarthub was not found, return a 404 message and a error message,
        //while if it was found delete all the data and set the status code to 200.
        if (smartHomeHub == null) {
            outcome = 404;
            response.put("outcome", false);
            response.put("error", "Smart Home Hub not found, please contact support");
        } else {

            //Delete all the plant data related to the specific smarrthub.
            PlantData.deleteAll("PlantData", "smarthub_id", id);

            //Delete the smart hub
            smartHomeHub.delete();

            outcome = 200;
            response.put("outcome", true);
        }

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(outcome));
    }
}
