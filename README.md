# PlantIQ API

## Overview

Spring boot application that exposes a REST API to interact with the PlantIQ database.
Dev Profile runs on H2 in memory database.
Default Profile will be setup to Azure SQL Server
Uses Java JDBC to connect to the database through a Repository interface.

## Setup

1. Clone the repository
2. Run `mvn clean install` to build the application or in the maven tab in your IDE of choice run `clean install`
3. Select Profile Dev to run the application on H2 in memory database or Default to run on Azure SQL Server (TODO)
4. Run the application
5. Enjoy!

## API Documentation

### Endpoints

H2 Console
-

    - http://localhost:8080/api/v1/h2-console

Animal
-

    - GET /api/v1/animal/all
    - GET /api/v1/animal/{id}
    - GET /api/v1/animal?name={name}&type={type}&age={age}

