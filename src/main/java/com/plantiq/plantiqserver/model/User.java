package com.plantiq.plantiqserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.plantiq.plantiqserver.core.Model;
import com.plantiq.plantiqserver.core.ModelCollection;

import java.util.HashMap;

public class User extends Model {

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

    public static ModelCollection<User> collection(){
        return new ModelCollection<>(User.class);
    }

    //|================================================|
    //|                  CLASS METHODS                 |
    //|================================================|

    //Constructor
    public User(HashMap<String, Object> data) {
        super(data);
    }

    public String getId(){
        return (String) this.data.get("id");
    }
    //Get Name Method
    public String getName(){
        return (String)this.data.get("name");
    }

    //Get Email Method
    public String getEmail(){
        return (String)this.data.get("email");
    }

    //Get Password Method
    public String getPassword(){
        return (String)this.data.get("password");
    }



}
