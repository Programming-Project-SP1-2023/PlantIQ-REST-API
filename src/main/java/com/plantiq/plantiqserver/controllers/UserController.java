package com.plantiq.plantiqserver.controllers;

import com.plantiq.plantiqserver.core.Gate;
import com.plantiq.plantiqserver.model.PasswordResetToken;
import com.plantiq.plantiqserver.model.User;
import com.plantiq.plantiqserver.rules.ConsumePasswordResetTokenRule;
import com.plantiq.plantiqserver.rules.UpdateUserDetailsRule;
import com.plantiq.plantiqserver.rules.ValidateSessionRule;
import com.plantiq.plantiqserver.service.EmailService;
import com.plantiq.plantiqserver.service.HashService;
import com.plantiq.plantiqserver.service.TimeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

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
            data.put("password",request.getParameter("password"));

            user.update(data);

            response.put("outcome",true);
            response.put("message","User details updated");

            return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
        }

        @GetMapping("/all")
        public List<User> all(){
            return User.collection().get();
        }

        @PostMapping("/reset")
        public HashMap<String, Object> generateResetToken(HttpServletRequest request){
            HashMap<String,Object> response = new HashMap<>();
            ValidateSessionRule rule = new ValidateSessionRule();

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
                EmailService.sendRecoveryEmail(request.getParameter("email"),HashService.generateSHA1(data));
            }else{
                response.put("outcome",false);
                response.put("message","Failed to perform action, please contact support.");
            }

            return response;

        }

        @PatchMapping("/reset/{token}")
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
