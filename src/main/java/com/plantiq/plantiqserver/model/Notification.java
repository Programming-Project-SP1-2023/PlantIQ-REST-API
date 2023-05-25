package com.plantiq.plantiqserver.model;

import com.plantiq.plantiqserver.core.Model;
import com.plantiq.plantiqserver.core.ModelCollection;
import com.plantiq.plantiqserver.service.TimeService;

import java.util.HashMap;
public class Notification extends Model{

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

    public static ModelCollection<Notification> collection(){
        return new ModelCollection<>(Notification.class);
    }

    public static void create(User user, String message){
        HashMap<String, Object> data = new HashMap<>();

        data.put("user_id",user.getId());
        data.put("message",message);
        data.put("timestamp", TimeService.now());

        Notification.insert("Notification",data);
    }

    //|================================================|
    //|                  CLASS METHODS                 |
    //|================================================|

    //Constructor
    public Notification(HashMap<String, Object> data){
        super(data);
    }

    //Get ID method
    public String getId(){
        return (String)this.data.get("id");
    }

    //Get user_id method
    public String getUserID(){
        return (String)this.data.get("user_id");
    }

    //Get timestamp method
    public int getTimestamp(){return Integer.parseInt((String)this.data.get("timestamp"));}

    public String getMessage(){
        return this.data.get("message").toString();
    }
}
