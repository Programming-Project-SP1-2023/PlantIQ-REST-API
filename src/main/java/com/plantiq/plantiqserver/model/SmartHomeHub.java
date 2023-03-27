package com.plantiq.plantiqserver.model;

import com.plantiq.plantiqserver.core.Model;
import com.plantiq.plantiqserver.core.ModelCollection;

import java.util.HashMap;

public class SmartHomeHub extends Model {

    //******************* STATIC CRUD OPERATIONS *******************\\

    public static ModelCollection<SmartHomeHub> collection(){
        return new ModelCollection<>(SmartHomeHub.class);
    }

    //******************* Class Methods *******************\\


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
