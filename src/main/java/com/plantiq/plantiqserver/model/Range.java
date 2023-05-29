package com.plantiq.plantiqserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.plantiq.plantiqserver.core.Model;
import com.plantiq.plantiqserver.core.ModelCollection;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;

public class Range extends Model {
    //Following are the 4 default healthy ranges for each field of a plant.
    public final static String DEFAULT_TEMPERATURE_RANGE = "4,29";
    public final static String DEFAULT_HUMIDITY_RANGE ="40,60";
    public final static String DEFAULT_LIGHT_RANGE ="8,16";
    public final static String DEFAULT_MOISTURE_RANGE ="21,80";

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

    public static Range getDefault(){
        HashMap<String,Object> data = new HashMap<>();
        data.put("range_humidity",Range.DEFAULT_HUMIDITY_RANGE);
        data.put("range_light",Range.DEFAULT_LIGHT_RANGE);
        data.put("range_temperature",Range.DEFAULT_TEMPERATURE_RANGE);
        data.put("range_moisture",Range.DEFAULT_MOISTURE_RANGE);
        return new Range(data);
    }

    public static HashMap<String,Object> evaluateWithSetValuesOrDefault(String id, HttpServletRequest request)
    {

        Range range = Range.collection().where("smarthub_id",id).getFirst();

        if(range == null){
            range = Range.getDefault();
        }

        float temp = Float.parseFloat(request.getParameter("temperature"));
        float light = Float.parseFloat(request.getParameter("light"));
        float humid = Float.parseFloat(request.getParameter("humidity"));
        float moisture = Float.parseFloat(request.getParameter("moisture"));

        HashMap<String,Object> errors = new HashMap<>();

        if(!range.isWithInRange(range.getTemperature(),temp)){
            errors.put("temperature",
                    "<p>Temperature is out of range "+
                    "<strong>"+temp+"c</strong><br>Its target range is "
                    +range.getTemperature()+" Celsius</p>");
        }

        if(!range.isWithInRange(range.getLight(),light)){
            errors.put("light",
                    "<p>Light is out of range " +
                            "<strong>"+light+"lm</strong>" +
                            "<br>Its target range is "
                            +range.getLight()+" Luminus</p>");
        }

        if(!range.isWithInRange(range.getHumidity(),humid)){
            errors.put("humidity",
                    "<p>Humidity is out of range " +
                            "<strong>"+humid+"%</strong>" +
                            "<br>Its target range is "
                            +range.getHumidity()+" Percent</p>");
        }

        if(!range.isWithInRange(range.getHumidity(),humid)){
            errors.put("moisture",
                    "<p>Moisture is out of range " +
                            "<strong>"+moisture+"%</strong>" +
                            "<br>Its target range is "
                            +range.getMoisture()+" Percent</p>");
        }

        return errors;
    }

    //|================================================|
    //|                  CLASS METHODS                 |
    //|================================================|

    //Constructor
    public Range(HashMap<String, Object> data){
        super(data);
    }
    //Get ID method
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getId(){
        return (String)this.data.get("id");
    }

    //Get smarthub_id method
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getSmartHubId() { return (String)this.data.get("smarthub_id");}

    //Method to get the humidity range
    public String getHumidity(){
        return (String)this.data.get("humidity");
    }

    //Method to get the light range
    public String getLight(){
        return (String)this.data.get("light");
    }

    //Method to get the temperature range
    public String getTemperature(){
        return (String)this.data.get("temperature");
    }
    //Method to get the moisture range
    public String getMoisture(){
        return (String)this.data.get("moisture");
    }

    private boolean isWithInRange(String range, float value){
        String[] rangeValues = range.split(",");
        float min = Float.parseFloat(rangeValues[0]);
        float max = Float.parseFloat(rangeValues[1]);

        return value >= min && value <= max;
    }



}
