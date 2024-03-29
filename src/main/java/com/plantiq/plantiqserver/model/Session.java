package com.plantiq.plantiqserver.model;

import com.plantiq.plantiqserver.core.Model;
import com.plantiq.plantiqserver.core.ModelCollection;

import java.util.HashMap;


public class Session extends Model{

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

    public static ModelCollection<Session> collection(){
        return new ModelCollection<>(Session.class);
    }

    //|================================================|
    //|                  CLASS METHODS                 |
    //|================================================|

    //Constructor
    public Session(HashMap<String, Object> data){
        super(data);
    }

    //Get token method
    public String getToken(){
        return (String)this.data.get("token");
    }

    //Get user_id method
    public String getUserId(){
        return (String)this.data.get("user_id");
    }

    //Get expiry date method
    public int getExpiry(){
        return Integer.parseInt((String)this.data.get("expiry"));
    }

}
