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

    //Get Smart Hub ID Method
    public String getSmartHubId(){
        return (String)this.data.get("smarthub_id");
    }

    //Get User Method
    public String getUser_id(){
        return (String)this.data.get("user_id");
    }

    //Get Sensor Type Method
    public String getSensorType(){
        return (String)this.data.get("sensor_type");
    }
}
