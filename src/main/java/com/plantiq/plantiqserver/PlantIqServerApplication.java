package com.plantiq.plantiqserver;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class PlantIqServerApplication {

	@Value("${database.password}")
	private String databaseConnectionLink;

	@Value("${password.pepper}")
	private String passwordPepper;

	@Value("${email.password}")
	private String emailPassword;

	@Value("${email.username}")
	private String emailUsername;

	@Value("${email.host}")
	private String emailHost;

	@Value("${email.port}")
	private int emailPort;

	@Value("${email.displayName}")
	private String emailDisplayName;


	private static PlantIqServerApplication instance;

	public static void main(String[] args) {

		SpringApplication.run(PlantIqServerApplication.class, args);
	}

	public PlantIqServerApplication(){

		PlantIqServerApplication.instance = this;
	}

	public String getEmailUsername(){
		return this.emailUsername;
	}

	public String getEmailPassword(){
		return this.emailPassword;
	}

	public int getEmailPort(){
		return this.emailPort;
	}

	public String getEmailHost(){
		return this.emailHost;
	}

	public String getEmailDisplayName(){
		return this.emailDisplayName;
	}

	public String getPasswordPepper(){
		return this.passwordPepper;
	}

	public String getDatabaseConnectionLink(){
		return this.databaseConnectionLink;
	}

	public static PlantIqServerApplication getInstance(){
		return PlantIqServerApplication.instance;
	}
}
