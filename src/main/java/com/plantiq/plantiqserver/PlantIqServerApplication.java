package com.plantiq.plantiqserver;


import com.plantiq.plantiqserver.model.Sensor;
import com.plantiq.plantiqserver.model.SmartHomeHub;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;


@SpringBootApplication
public class PlantIqServerApplication {


	public static void main(String[] args) {


		ArrayList<SmartHomeHub> results = SmartHomeHub.collection().get();

		results.forEach((n)->{
			System.out.println(n.getName());
		});

		ArrayList<Sensor> results2 = Sensor.collection().get();
		results2.forEach((n)->{
			System.out.println(n.getSensorType());
		});

		//System.exit(0);

		SpringApplication.run(PlantIqServerApplication.class, args);
	}
}
