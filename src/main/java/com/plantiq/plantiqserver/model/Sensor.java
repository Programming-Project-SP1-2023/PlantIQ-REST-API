package com.plantiq.plantiqserver.model;

import com.plantiq.plantiqserver.core.Model;
import com.plantiq.plantiqserver.core.ModelCollection;

import java.util.HashMap;


public class Sensor extends Model {

    //******************* STATIC CRUD OPERATIONS *******************\\

    public static ModelCollection<Sensor> collection(){
        return new ModelCollection<>(Sensor.class);
    }


    //******************* Class Methods *******************\\

    //Constructor
    public Sensor(HashMap<String, String> data){
        super(data);
    }

    //Get ID Method
    public String getId(){
        return this.data.get("id");
    }

    //Get Smart Hub ID Method
    public String getSmartHubId(){
        return this.data.get("smarthub_id");
    }

    //Get User Method
    public String getUser_id(){
        return this.data.get("user_id");
    }

    //Get Sensor Type Method
    public String getSensorType(){
        return this.data.get("sensor_type");
    }
}
