package com.plantiq.plantiqserver.controllers;

import com.plantiq.plantiqserver.model.PasswordResetToken;
import com.plantiq.plantiqserver.model.User;
import com.plantiq.plantiqserver.rules.ConsumePasswordResetTokenRule;
import com.plantiq.plantiqserver.rules.SessionValidateRequestRule;
import com.plantiq.plantiqserver.service.HashService;
import com.plantiq.plantiqserver.service.TimeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.PUT, RequestMethod.OPTIONS})
@RequestMapping("/user")
public class UserController {


        @GetMapping("/all")
        public List<User> all(){
            return User.collection().get();
        }

        @PostMapping("/reset")
        public HashMap<String, Object> generateResetToken(HttpServletRequest request){
            HashMap<String,Object> response = new HashMap<>();
            SessionValidateRequestRule rule = new SessionValidateRequestRule();

            if(!rule.validate(request)){
                response.put("outcome", false);
                response.put("errors", rule.getErrors());
                return response;
            }
            String data=request.getParameter("email")+TimeService.now();

            HashMap<String, Object> token= new HashMap<>();

            token.put("token",HashService.generateSHA1(data));
            token.put("email",request.getParameter("email"));
            token.put("expirationDate",TimeService.nowPlusDays(1).toString());
            token.put("createdDate",TimeService.now().toString());

            if(PasswordResetToken.insert("passwordResetToken",token)){
                response.put("outcome",true);
                response.put("message","If an email address exists for that account you will receive an email shortly.");
            }else{
                response.put("outcome",false);
                response.put("message","Failed to perform action, please contact support.");
            }

            return response;

        }

        @PatchMapping("/reset")
        public HashMap<String,Object> consumePasswordResetToken(HttpServletRequest request){
            HashMap<String,Object> response = new HashMap<>();

            //Validate our HTTP request
            ConsumePasswordResetTokenRule rule = new ConsumePasswordResetTokenRule();

            if(!rule.validate(request)){
                response.put("outcome", false);
                response.put("errors", rule.getErrors());
                return response;
            }

            //Get our password reset token from the database
            PasswordResetToken passwordResetToken = PasswordResetToken.collection().where("token",request.getParameter("token")).getFirst();

            //Check the token expiration date is valid and not expired
            if(passwordResetToken.expirationDate() < TimeService.now()){

                passwordResetToken.delete("token");
                response.put("outcome",false);
                response.put("error","token invalid or expired");

                return response;
            }

            User user = User.collection().where("email",passwordResetToken.email()).getFirst();


            return response;
        }
}
