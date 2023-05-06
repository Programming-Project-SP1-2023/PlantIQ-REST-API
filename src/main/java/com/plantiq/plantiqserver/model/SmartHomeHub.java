package com.plantiq.plantiqserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.plantiq.plantiqserver.core.Model;
import com.plantiq.plantiqserver.core.ModelCollection;

import java.util.ArrayList;
import java.util.HashMap;

public class SmartHomeHub extends Model {

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

    public static ModelCollection<SmartHomeHub> collection(){

        return new ModelCollection<>(SmartHomeHub.class);
    }

    //|================================================|
    //|                  CLASS METHODS                 |
    //|================================================|

    //Constructor
    public SmartHomeHub(HashMap<String, Object> data) {
        super(data);
    }

    //Get ID method
    public String getId(){
        return (String)this.data.get("id");
    }


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getDeviceSpecificPassword(){
       return (String)this.data.get("deviceSpecificPassword");
    }

    //Method to get when the device posted last
    public int getLastPosted(){
        return Integer.parseInt((String)this.data.get("lastPosted"));
    }

    // Get Name Method
    public String getName(){
        return (String)this.data.get("name");
    }

    //Get Post Frequency Method
    public int getPostFrequency(){
        return Integer.parseInt((String)this.data.get("postFrequency"));
    }

    //Method that returns if the smartHomeHub is virtual or not
    public boolean getVirtual(){
        return this.data.get("virtual").equals("true");
    }

    //Get user_id method
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getUser_id(){ return (String)this.data.get("user_id");}


}
