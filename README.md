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

<img width="480" alt="step 1" src="https://github.com/Programming-Project-SP1-2023/PlantIQ-REST-API/assets/79836947/c116201e-71a5-4954-83b7-828dc8f29137">

### Step 2)
Select "Web App"

<img width="480" alt="step 2" src="https://github.com/Programming-Project-SP1-2023/PlantIQ-REST-API/assets/79836947/61936ebd-fe40-41ba-965e-1ab65596c540">

### Step 3)
Name your new web app, in the case of this project we would use PlantIQ REST API but in the
example screenshot I use MySpringApp.

Now it's important to select the most appropriate Java version, our project is compatible with Java 17 and that 
is the most recent provided so select that from the drop-down list.

Lastly select the region that is most appropriate for you.

<img width="480" alt="Step 3" src="https://github.com/Programming-Project-SP1-2023/PlantIQ-REST-API/assets/79836947/d0c5c656-3c9b-48bf-9872-dae67e39ab8a">

### Step 4)
Review the setting you have selected and click create.

<img width="480" alt="step 4" src="https://github.com/Programming-Project-SP1-2023/PlantIQ-REST-API/assets/79836947/296c6e13-d1b5-44ba-a876-a7f555993826">

### Step 5)
Microsoft Azure may take a few minutes to deploy your application, wait untill it confirms its completed.

<img width="480" alt="step 5" src="https://github.com/Programming-Project-SP1-2023/PlantIQ-REST-API/assets/79836947/09e0e54a-46bf-4ba7-909b-3796dbc03af6">

### Step 6)
Once deployed click into the Application, you will see an overview from this screen.

We can now connect via FTP, on the left hand side menu select "Deployment Center"

<img width="480" alt="step 6" src="https://github.com/Programming-Project-SP1-2023/PlantIQ-REST-API/assets/79836947/99cfc4d2-f505-4b3a-b4d6-b68abe1d5a0b">

### Step 7)
Once the deployment center opens select FTP details from its menu, copy these details into your FTP client of choice,
Our recommendation is File Zilla https://filezilla-project.org/.

<img width="480" alt="step 7" src="https://github.com/Programming-Project-SP1-2023/PlantIQ-REST-API/assets/79836947/006c534f-a219-4853-adbf-bdf50f9b6a8f">

### Step 8)
Once connected you will see the current folder is called wwwroot and the hostingstart.html file present.
This folder is where we upload our runnable builds to, to perform an upload drag the file in and File Zilla will upload it.

<img width="480" alt="step 8" src="https://github.com/Programming-Project-SP1-2023/PlantIQ-REST-API/assets/79836947/74347b68-d029-4aa3-bd5b-0ff6afeb4e94">

### Step 9)
Once the file is uploaded close File Zilla and from the left and menu select "Configuration" then from its menu select "General Settings".

This will now provide you with the java startup command, here we need to java the following, 

replacing MyJar.jar with the jar name including the extension,
```
java -jar /home/site/wwwroot/MyJar.jar
```
<img width="480" alt="step 9" src="https://github.com/Programming-Project-SP1-2023/PlantIQ-REST-API/assets/79836947/57111cfe-c76a-4dbe-9956-4f9e8d41f3b7">


### Step 10)
Now we have uploaded our jar and set it as the start-up command, navigate back to the overview screen and click the restart button
Azure will now use the jar and serve your spring boot application.

If at any point you wish to track the restart and troubleshoot errors simply use the live log provided the "Log Stream" 
option on the left hand side menu.

Sucess! the application is now deployed to Microsoft Azure!
