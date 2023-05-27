[Back to documentation home](https://github.com/Programming-Project-SP1-2023/Backend-REST-API)

# Handling Plants
PlantIQ's PlantService is a class designed to simplify the interaction with plants. The three functionalities offered by the class are the following:
- Get All Plants
- Check validity of the Plant's ID
- Retrieve the Plant's name

Each one of these functionalities are resolved by their own static method. This allows the other classes to use these functionalities without the need of creating the PlantService object.
However, to use the PlantService functionalities, the class must be imported as following:
```java
import com.plantiq.plantiqserver.service.PlantService;
```
##How to get all plants
The method getAll() is used to retrieve all the plants of a single user, from each one of their SmartHomeHubs. This method requires a user to be logged-in, in order to identify who's plants are going to be listed.
Each Plant will be displayed with 4 of its attributes:

- sensor_id
- smarthomehub_id
- name
- postFrequency

```java
//EXAMPLE USAGE
response.put("data", PlantService.getAll());
//EXAMPLE RESULT
        "data": [
        {
        "sensor_id": "3211233211231233",
        "smarthomehub_id": "123321123322",
        "name": "3211233211231233",
        "postFrequency": 3600
        }
        ],
```

##How to ensure a plant is valid
Working with plants that don't actually exist within the database can generate various errors. To avoid similar issues, the method validPlantId() has been designed to validate the existence of a specific plant within the database by searching for its ID. Calling the method and passing the ID as a
parameter will return a boolean with a true or false value depending on the result of the research. If no record in the database has a sensor_id matching the parameter's value,
the outcome will be false, while if there is a match the outcome will be true. 
```java
//EXAMPLE
 if(!PlantService.validPlantId(id)){
         ...
 }
```

##How to retrieve a plants name
In order to enhance user-friendliness, users have the possibility to assign to each sensor_id  a name. In order to retrieve the plant's name, the method getNameOrID() must be called, passing the sensor_id as parameter. 
If the user has set a name for that specific plant, it will be returned as result of the method. If no name was found, the sensor_id will be returned to the user, so that it can still be distinguished from the other plants.
```java
//EXAMPLE
data.put("name", PlantService.getNameOrId(sensor_id));
```