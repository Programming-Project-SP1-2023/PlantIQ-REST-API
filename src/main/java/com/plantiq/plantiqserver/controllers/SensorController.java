package com.plantiq.plantiqserver.controllers;

import com.plantiq.plantiqserver.model.Sensor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sensor")
public class SensorController {

    @GetMapping("/all")
    public List<Sensor> all(){
        return Sensor.collection().get();
    }



}
