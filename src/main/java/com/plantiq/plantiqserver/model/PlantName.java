package com.plantiq.plantiqserver.model;

import com.plantiq.plantiqserver.core.Model;
import com.plantiq.plantiqserver.core.ModelCollection;

import java.util.HashMap;


public class PlantName extends Model {
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


    public static ModelCollection<PlantName> collection() {
        return new ModelCollection<>(PlantName.class);
    }
    //|================================================|
    //|                  CLASS METHODS                 |
    //|================================================|

    //Constructor
    public PlantName(HashMap<String, Object> data) {
        super(data);
    }

    //Get name
    public String getName(){
        return this.data.get("name").toString();
    }
}
