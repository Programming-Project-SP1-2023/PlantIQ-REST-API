package com.plantiq.plantiqserver;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class PlantIqServerApplication {

	public static final String passwordPepper = "Steak&Chips!";
	public static final String emailPassword = "";
	public static final String databasePassword = "";

	public static void main(String[] args) {

		SpringApplication.run(PlantIqServerApplication.class, args);
	}
}
