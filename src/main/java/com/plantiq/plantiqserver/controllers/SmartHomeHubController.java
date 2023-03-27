package com.plantiq.plantiqserver.controllers;

import com.plantiq.plantiqserver.model.SmartHomeHub;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/smarthomehub")
public class SmartHomeHubController {

    @GetMapping("/all")
    public List<SmartHomeHub> all(HttpServletRequest request){

        if(request.getParameterMap().containsKey("limit")){
            return SmartHomeHub.collection().limit(Integer.parseInt(request.getParameter("limit"))).get();
        }else{
            return SmartHomeHub.collection().get();
        }

    }

}
