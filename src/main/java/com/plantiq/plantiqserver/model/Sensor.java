package com.plantiq.plantiqserver.model;

import com.plantiq.plantiqserver.core.Model;
import com.plantiq.plantiqserver.core.ModelCollection;

import java.util.HashMap;


public class Sensor extends Model {

    //|================================================|
    //|              STATIC CRUD METHODS               |
    //|================================================|

    //Our collection method is the only method that has to be
    //present in the child classes as it must inject the child
    //class type to ensure they are returned correctly.

    //This method will create a ModelCollection object using
    //the current type via generics and by providing the class
    //directly into the constructor, this collection object
    //allows us to build our SQL query and return our values.

    public static ModelCollection<Sensor> collection(){
        return new ModelCollection<>(Sensor.class);
    }


    //|================================================|
    //|                  CLASS METHODS                 |
    //|================================================|

    //Constructor
    public Sensor(HashMap<String, Object> data){
        super(data);
    }

    //Get ID Method
    public String getId(){
        return (String)this.data.get("id");
    }
    //Get Name Method
    public String getName(){
        return (String)this.data.get("name");
    }
    //Get Location Method
    public String getLocation(){
        return (String)this.data.get("location");
    }
    //Get Registration Date Method
    public String getRegistrationDate(){
        return (String)this.data.get("registrationDate");
    }
}
