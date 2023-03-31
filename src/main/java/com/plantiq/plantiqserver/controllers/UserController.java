package com.plantiq.plantiqserver.controllers;

import com.plantiq.plantiqserver.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {


        @GetMapping("/all")
        public List<User> all(){
            return User.collection().get();
        }

        public void login(String email ,String password) {

            ArrayList<User> output = User.collection().where("email","Sil").where("password","123").get();

            if (output.size()==0){
                //No Match, Stay On the login Page
            }

            else{
                // Account exists, Move to HomePage/DashBoard
            }

        }

}
