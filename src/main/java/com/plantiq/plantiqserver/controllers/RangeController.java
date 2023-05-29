package com.plantiq.plantiqserver.controllers;

import com.plantiq.plantiqserver.core.Gate;
import com.plantiq.plantiqserver.model.Range;
import com.plantiq.plantiqserver.model.SmartHomeHub;
import com.plantiq.plantiqserver.rules.SetRangeRule;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        Range range = Range.collection().where("smarthub_id",id).getFirst();

        if(range != null){
            range.delete();
        }

        HashMap<String,Object> data = new HashMap<>();

        data.put("temperature",request.getParameter("temperature"));
        data.put("light",request.getParameter("light"));
        data.put("humidity",request.getParameter("humidity"));
        data.put("moisture",request.getParameter("moisture"));
        data.put("smarthub_id",id);

        Range.insert("Range",data);

        response.put("outcome",true);
        response.put("message","Updated range values for smart hub!");

        return new ResponseEntity<>(response,HttpStatusCode.valueOf(200));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HashMap<String,Object>> getRange(@PathVariable String id, HttpServletRequest request){
        HashMap<String,Object> response = new HashMap<>();

        //Validate the user is logged in.
        if(!Gate.authorized(request)){
            return Gate.abortUnauthorized();
        }

        //Check if the ID is valid, else return an error.
        SmartHomeHub hub = SmartHomeHub.collection().where("id",id).getFirst();

        if(hub == null){
            response.put("outcome",false);
            response.put("message","Invalid smart home hub id provided!");
            return new ResponseEntity<>(response,HttpStatusCode.valueOf(401));
        }

        //Load the range or return the default.
        Range range = Range.collection().where("smarthub_id",id).getFirst();

        if(range == null){
            range = Range.getDefault();
        }

        response.put("range",range);
        response.put("outcome",true);

        return new ResponseEntity<>(response,HttpStatusCode.valueOf(200));
    }
}
