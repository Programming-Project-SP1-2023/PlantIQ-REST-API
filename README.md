# PlantIQ REST API (Backend)
<p align="center">
<img src="https://plantiq.azurewebsites.net/static/media/plantiq.e10024f2f1e779ff205f.png" style="width:256px">
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

The PlantIQ backend provides the core functionality to both the frontend and smart home hubs and sensors, it serves as the central glue point that brings the experiance together. This application is written using Java 17 and utlizes Spring Boot to provide its core functionality for interacting with web requests. This app is designed to run on Microsoft Azure web services and should be used only as a backend for clients to connect to.

## Java Version 
This Application should be run on Java version 17 or higher.

## Accessing the backend?
To access the backend for testing or development purposes we recomend the use the HTTP client Insomnia <a href="https://insomnia.rest/">https://insomnia.rest/</a>

## How do I install this application on Microsoft Azure?
Microsoft's azure platform provides a number of streamlined ways to upload and run 
your Java Spring Boot application, for this example i will show you how to upload it
via sFTP and set the jar.

### Step 1) 
Login to Microsoft Azure and select create a new resource.

### Step 2)
Select "Web App"

### Step 3)
Name your new web app, in the case of this project we would use PlantIQ REST API but in the
example screenshot I use MySpringApp.

Now it's important to select the most appropriate Java version, our project is compatible with Java 17 and that 
is the most recent provided so select that from the drop-down list.

Lastly select the region that is most appropriate for you.

### Step 4)
Review the setting you have selected and click create.

### Step 5)
Microsoft Azure may take a few minutes to deploy your application, wait untill it confirms its completed.

### Step 6)
Once deployed click into the Application, you will see an overview from this screen.

We can now connect via FTP, on the left hand side menu select "Deployment Center"

### Step 7)
Once the deployment center opens select FTP details from its menu, copy these details into your FTP client of choice,
Our recommendation is File Zilla https://filezilla-project.org/.

### Step 8)
Once connected you will see the current folder is called wwwroot and the hostingstart.html file present.
This folder is where we upload our runnable builds to, to perform an upload drag the file in and File Zilla will upload it.

### Step 9)
Once the file is uploaded close File Zilla and from the left and menu select "Configuration" then from its menu select "General Settings".

This will now provide you with the java startup command, here we need to java the following, 

replacing MyJar.jar with the jar name including the extension,
```
java -jar /home/site/wwwroot/MyJar.jar
```

### Step 10)
Now we have uploaded our jar and set it as the start-up command, navigate back to the overview screen and click the restart button
Azure will now use the jar and serve your spring boot application.

If at any point you wish to track the restart and troubleshoot errors simply use the live log provided the "Log Stream" 
option on the left hand side menu.
