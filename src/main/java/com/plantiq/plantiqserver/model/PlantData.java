package com.plantiq.plantiqserver.model;

import com.plantiq.plantiqserver.core.Model;
import com.plantiq.plantiqserver.core.ModelCollection;

import java.util.HashMap;

public class PlantData extends Model {

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

    public static ModelCollection<PlantData> collection() {
        return new ModelCollection<>(PlantData.class);
    }

    //|================================================|
    //|                  CLASS METHODS                 |
    //|================================================|

    //Constructor
    public PlantData(HashMap<String, Object> data) {
        super(data);
    }

    //Get ID method
    public int getId(){
        return Integer.parseInt((String)this.data.get("id"));
    }

    //Get smarthub_id method
    public String getSmartHomeHubId(){
        return (String) this.data.get("smarthomehub_Id");
    }
    //Get sensor_id method
    public String getSensorId(){
        return (String) this.data.get("sensor_Id");
    }

    //Get timestamp method
    public int getTimestamp(){
        return Integer.parseInt((String)this.data.get("timestamp"));
    }

    //Get temperature method
    public float getTemperature(){
        return Float.parseFloat((String)this.data.get("temperature"));
    }

    //Get humidity method
    public float getHumidity(){
        return Float.parseFloat((String)this.data.get("humidity"));
    }

    //Get light method
    public float getLight(){
        return Float.parseFloat((String)this.data.get("light"));
    }

    //Get moisture method
    public float getMoisture(){
        return Float.parseFloat((String)this.data.get("moisture"));
    }

}
