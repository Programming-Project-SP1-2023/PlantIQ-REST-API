# PlantIQ REST API (Backend)
<p align="center">
<img src="https://plantiq.azurewebsites.net/static/media/plantDashboard.171a0a4ed4dffdac28f3.png">
</p>
<h3 align="center">
  <b>Documentation:</b><br><br>
  <a href="https://github.com/Programming-Project-SP1-2023/Backend-REST-API/blob/main/docs/Rules.md">Rules</a> |
    <a href="https://github.com/Programming-Project-SP1-2023/PlantIQ-REST-API/blob/main/docs/Email.md">Email</a> |
      <a href="https://github.com/Programming-Project-SP1-2023/PlantIQ-REST-API/blob/main/docs/Gate.md">Gate & Authentication</a> |
        <a href="https://github.com/Programming-Project-SP1-2023/PlantIQ-REST-API/blob/main/docs/HashService.md">Hashing</a> |
          <a href="https://github.com/Programming-Project-SP1-2023/PlantIQ-REST-API/blob/main/docs/PlantService.md">PlantService</a> |
          <a href="https://github.com/Programming-Project-SP1-2023/PlantIQ-REST-API/blob/main/docs/SessionService.md">Sessions</a> |
            <a href="https://github.com/Programming-Project-SP1-2023/PlantIQ-REST-API/blob/main/docs/SqlSecurity.md">SQL Sanitization</a> |
            <a href="https://github.com/Programming-Project-SP1-2023/PlantIQ-REST-API/blob/main/docs/TimeService.md">TimeService</a> |
  <a href="https://github.com/Programming-Project-SP1-2023/Backend-REST-API/blob/main/docs/ModelCollection.md">Model Collection</a>
</h3>

## Overview

Spring boot application that exposes a REST API to interact with the PlantIQ database.
Dev Profile runs on H2 in memory database.
Default Profile will be setup to Azure SQL Server
Uses Java JDBC to connect to the database through a Repository interface.
