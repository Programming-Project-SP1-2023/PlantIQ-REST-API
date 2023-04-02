package com.plantiq.plantiqserver.controllers;

import com.plantiq.plantiqserver.model.Session;
import com.plantiq.plantiqserver.model.User;
import com.plantiq.plantiqserver.rules.LoginRequestRule;
import com.plantiq.plantiqserver.rules.SessionValidateRequestRule;
import com.plantiq.plantiqserver.service.SessionService;
import com.plantiq.plantiqserver.service.TimeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @PostMapping("/login")
    public HashMap<String,Object> login(HttpServletRequest request) {

        //Create our response object, this is returned as JSON.
        HashMap<String, Object> response = new HashMap<>();

        //Create our login request rule, we will validate our request
        //using this rule.
        LoginRequestRule rule = new LoginRequestRule();

        //Validate our request and return the errors if present.
        if(!rule.validate(request)){
            response.put("outcome",false);
            response.put("errors",rule.getErrors());
            return response;
        }

        //Else if we reach this stage of the code then proceed to
        //lookup the user from the database.
        User user = User.collection().where("email",request.getParameter("email")).where("password",request.getParameter("password")).getFirst();

        if(user != null){
            response.put("outcome","true");
            response.put("session", SessionService.create(user.getId()));
        }else{
            response.put("outcome","false");
            response.put("errors","Login failed, please check your email and password");
        }

        return response;
    }

    @PostMapping("/logout")
    public HashMap<String, String> logout(){



        return null;
    }

    @PostMapping("/validate")
    public HashMap<String, Object> validate(HttpServletRequest request){

        HashMap<String,Object> response = new HashMap<>();
        SessionValidateRequestRule rule = new SessionValidateRequestRule();

        if(!rule.validate(request)){
            response.put("outcome",false);
            response.put("errors",rule.getErrors());
            return response;
        }

        Session session = Session.collection().where("token",request.getParameter("token")).loadFromCache().getFirst();

        if(session.getExpiry() < TimeService.now()){
            session.delete("token");
            response.put("outcome", false);
        }else{
            response.put("outcome", true);
        }


        return response;
    }
}
