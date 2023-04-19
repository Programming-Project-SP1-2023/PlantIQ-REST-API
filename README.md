# PlantIQ API

<h3 align="center">
  <b>Documentation:</b><br><br>
  <a href="https://github.com/Programming-Project-SP1-2023/Backend-REST-API/blob/main/docs/Rules.md">Rules</a> |
  <a href="https://github.com/Programming-Project-SP1-2023/Backend-REST-API/blob/main/docs/ModelCollection.md">Model Collection</a>
</h3>

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

## Work Flow

1. Pull the latest code from the repository branch
2. maven clean install
3. Run the application in dev profile

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

