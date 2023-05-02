package com.plantiq.plantiqserver.model;

import com.plantiq.plantiqserver.core.Model;
import com.plantiq.plantiqserver.core.ModelCollection;

import java.util.HashMap;

public class Range extends Model {

    public final static String DEFAULT_TEMPERATURE_RANGE = "4-29";
    public final static String DEFAULT_HUMIDITY_RANGE ="40-60";
    public final static String DEFAULT_LIGHT_RANGE ="8-16";
    public final static String DEFAULT_MOISTURE_RANGE ="21-80";

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

    public static ModelCollection<Range> collection(){
        return new ModelCollection<>(Range.class);
    }


    //|================================================|
    //|                  CLASS METHODS                 |
    //|================================================|

    //Constructor
    public Range(HashMap<String, Object> data){
        super(data);
    }

    //Getters
    public String getId(){
        return (String)this.data.get("id");
    }

    public String getRangeHumidity(){
        return (String)this.data.get("range_humidity");
    }

    public String getRangeLight(){
        return (String)this.data.get("range_light");
    }

    public String getRangeTemperature(){
        return (String)this.data.get("range_temperature");
    }

    public String getRangeMoisture(){
        return (String)this.data.get("range_moisture");
    }

}
