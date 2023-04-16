package com.plantiq.plantiqserver.controllers;

import com.plantiq.plantiqserver.core.Gate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/smarthub")
public class SmartHubController {

    @PostMapping("/{id}/data")
    public ResponseEntity<HashMap<String, Object>> savePlantData(@PathVariable("id") String id, HttpServletRequest request){

        HashMap<String, Object> response = new HashMap<>();

        if(!Gate.authorized(request)){
            return Gate.abortUnauthorized();
        }


        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/{id}/data")
    public ResponseEntity<HashMap<String, Object>> deletePlantData(@PathVariable("id") String id, HttpServletRequest request){

        HashMap<String, Object> response = new HashMap<>();

        if(!Gate.authorized(request)){
            return Gate.abortUnauthorized();
        }

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));

    }

    @GetMapping("/{id}/rate")
    public ResponseEntity<HashMap<String, Object>> getPlantDataUploadRate(@PathVariable("id") String id, HttpServletRequest request){

        HashMap<String, Object> response = new HashMap<>();

        if(!Gate.authorized(request)){
            return Gate.abortUnauthorized();
        }


        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));

    }

    @GetMapping("/all")
    public ResponseEntity<HashMap<String, Object>> getAll(HttpServletRequest request){

        HashMap<String, Object> response = new HashMap<>();

        if(!Gate.authorized(request)){
            return Gate.abortUnauthorized();
        }

        response.put("outcome",true);

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }


}
