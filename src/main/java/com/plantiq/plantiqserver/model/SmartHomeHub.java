package com.plantiq.plantiqserver.model;

import com.plantiq.plantiqserver.core.Model;
import com.plantiq.plantiqserver.core.ModelCollection;

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

    //Our class methods are used by the child model class
    //to provide all the functionality to it.

    //The date

    //Constructor
    public SmartHomeHub(HashMap<String, String> data) {
        super(data);
    }

    public String getId(){
        return this.data.get("id");
    }

    public String getUserId(){
        return this.data.get("user_id");
    }

    public int getDataRegistred(){
        return Integer.parseInt(this.data.get("date_registred"));
    }

    public String getName(){
        return this.data.get("name");
    }

}
