package com.plantiq.plantiqserver.controllers;

import com.plantiq.plantiqserver.PlantIqServerApplication;
import com.plantiq.plantiqserver.core.Gate;
import com.plantiq.plantiqserver.core.ModelCollection;
import com.plantiq.plantiqserver.core.Sort;
import com.plantiq.plantiqserver.model.PasswordResetToken;
import com.plantiq.plantiqserver.model.User;
import com.plantiq.plantiqserver.rules.ConsumePasswordResetTokenRule;
import com.plantiq.plantiqserver.rules.GetAllRule;
import com.plantiq.plantiqserver.rules.PasswordResetRule;
import com.plantiq.plantiqserver.rules.UpdateUserDetailsRule;
import com.plantiq.plantiqserver.service.EmailService;
import com.plantiq.plantiqserver.service.HashService;
import com.plantiq.plantiqserver.service.TimeService;
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
@RequestMapping("/user")
public class UserController {

        //@GetMapping("/info") is the endpoint that will return the information
        //of the logged-in user
        @GetMapping("/info")
        public ResponseEntity<HashMap<String, Object>> getUserBySession(HttpServletRequest request){
            //Create our response object, this is returned as JSON.
            HashMap<String,Object> response = new HashMap<>();
            //Authenticate the user with the gate service
            if(!Gate.authorized(request)){
                return Gate.abortUnauthorized();
            }
            //The response returning to the front end will contain all the data of the
            //logged-in user.
            response.put("outcome",true);
            response.put("data",User.collection().where("id",Gate.getCurrentUser().getId()).getFirst());

            return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
        }
        //@PatchMapping("/info") is the endpoint that will allow the user to modify
        //their saved personal data. They will be able to change name,surname,email
        //and password.
        @PatchMapping("/info")
        public ResponseEntity<HashMap<String, Object>> updateUserBySession(HttpServletRequest request){
            //Create our response object, this is returned as JSON.
            HashMap<String,Object> response = new HashMap<>();
            //Authenticate the user with the gate service
            if(!Gate.authorized(request)){
                return Gate.abortUnauthorized();
            }
            //Getting the current logged-in user.
            User user = User.collection().where("id",Gate.getCurrentUser().getId()).getFirst();
            //Create our update details user rule, we will validate
            //our request using this rule.
            UpdateUserDetailsRule rule = new UpdateUserDetailsRule();
            //Validate our request and return the errors if present.
            if(!rule.validate(request)){
                return rule.abort();
            }

            //Creating the structure necessary to insert user's details
            //into the database. Since these fields are optional, in
            //order to not modify the entire database replacing values
            //with null, we need to check if these have been submitted
            //in the request.
            HashMap<String,Object> data = new HashMap<>();
            if(request.getParameter("email")!=null && request.getParameter("email")!="")
                data.put("email",request.getParameter("email"));
            if(request.getParameter("firstname")!=null && request.getParameter("firstname")!="")
                data.put("firstname",request.getParameter("firstname"));
            if(request.getParameter("surname")!=null && request.getParameter("surname")!="")
                data.put("surname",request.getParameter("surname"));
            if(request.getParameter("password")!=null && request.getParameter("password")!="")
                data.put("password",HashService.generateSHA1(PlantIqServerApplication.getInstance().getPasswordPepper()+request.getParameter("password")));

            //Updating the entity User with the new data.
            user.update(data);
            //A successful message will be returned once the entity
            //has been updated.
            response.put("outcome",true);
            response.put("message","User details updated");

            return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
        }
        //@GetMapping("/all") is the endpoint which will return all records
        //of the user's entity. It is possible to refine the search by limiting,
        //offsetting and sorting the returned records.
        @GetMapping("/all")
        public ResponseEntity<HashMap<String, Object>> all(HttpServletRequest request){
            //Create our response object, this is returned as JSON.
            HashMap<String,Object> response = new HashMap<>();
            //Authenticate the user with the gate service
            if(!Gate.authorizedAsAdmin(request)){
                return Gate.abortUnauthorized();
            }
            //Create our get all rule, we will validate
            //our request using this rule.
            GetAllRule rule = new GetAllRule();
            //Validate our request and return the errors if present.
            if(!rule.validate(request)){
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
            response.put("list",users.get());
            response.put("outcome",true);
            response.put("message","successfully processed request");

            return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
        }
}
