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

@RestController
@RequestMapping("/user")
public class UserController {


        @GetMapping("/info")
        public ResponseEntity<HashMap<String, Object>> getUserBySession(HttpServletRequest request){
            HashMap<String,Object> response = new HashMap<>();

            if(!Gate.authorized(request)){
                return Gate.abortUnauthorized();
            }

            response.put("outcome",true);
            response.put("data",User.collection().where("id",Gate.getCurrentUser().getId()).getFirst());

            return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
        }

        @PatchMapping("/info")
        public ResponseEntity<HashMap<String, Object>> updateUserBySession(HttpServletRequest request){
            HashMap<String,Object> response = new HashMap<>();

            if(!Gate.authorized(request)){
                return Gate.abortUnauthorized();
            }

            UpdateUserDetailsRule rule = new UpdateUserDetailsRule();

            if(!rule.validate(request)){
                return rule.abort();
            }

            User user = User.collection().where("id",Gate.getCurrentUser().getId()).getFirst();

            HashMap<String,Object> data = new HashMap<>();
            data.put("email",request.getParameter("email"));
            data.put("firstname",request.getParameter("firstname"));
            data.put("surname",request.getParameter("surname"));
            data.put("password",HashService.generateSHA1(PlantIqServerApplication.passwordPepper+request.getParameter("password")));

            user.update(data);

            response.put("outcome",true);
            response.put("message","User details updated");

            return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
        }

        @GetMapping("/all")
        public ResponseEntity<HashMap<String, Object>> all(HttpServletRequest request){
            HashMap<String,Object> response = new HashMap<>();

            if(!Gate.authorizedAsAdmin(request)){
                return Gate.abortUnauthorized();
            }

            GetAllRule rule = new GetAllRule();
            if(!rule.validate(request)){
                return rule.abort();
            }

            ModelCollection<User> users = User.collection();

            //Add all the required parameters.
            users.limit(Integer.parseInt(request.getParameter("limit")));
            users.offset(Integer.parseInt(request.getParameter("offset")));
            users.orderBy(request.getParameter("sortBy"));
            users.orderType(Sort.valueOf(request.getParameter("sort")));

            response.put("list",users.get());
            response.put("outcome",true);
            response.put("message","successfully processed request");

            return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
        }
}
