package com.plantiq.plantiqserver.controllers;

import com.plantiq.plantiqserver.core.Gate;
import com.plantiq.plantiqserver.rules.SetRangeRule;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/range")
public class RangeController {

    @PostMapping("/{id}")
    public ResponseEntity<HashMap<String,Object>> setRange(@PathVariable String id, HttpServletRequest request){
        HashMap<String,Object> response = new HashMap<>();

        if(!Gate.authorized(request)){
            return Gate.abortUnauthorized();
        }

        SetRangeRule rule = new SetRangeRule();

        if(!rule.validate(request)){
            return rule.abort();
        }

        response.put("test","Hello world!");

        return new ResponseEntity<>(response,HttpStatusCode.valueOf(200));
    }
}
