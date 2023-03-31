package com.plantiq.plantiqserver.controllers;

import com.plantiq.plantiqserver.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {


        @GetMapping("/all")
        public List<User> all(){
            return User.collection().get();
        }


}
