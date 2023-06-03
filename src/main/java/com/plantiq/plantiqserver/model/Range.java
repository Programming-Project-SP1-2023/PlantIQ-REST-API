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
    //This method is used to retrieve and return the default 
    //ranges of the four analysed fields.
    public static Range getDefault(){
        HashMap<String,Object> data = new HashMap<>();
        data.put("humidity",Range.DEFAULT_HUMIDITY_RANGE);
        data.put("light",Range.DEFAULT_LIGHT_RANGE);
        data.put("temperature",Range.DEFAULT_TEMPERATURE_RANGE);
        data.put("moisture",Range.DEFAULT_MOISTURE_RANGE);
        return new Range(data);
    }
    //This method is used to check if a plant has a value out
    //of range. It will compare the given values with the set 
    //range (if present), and return an error if the value 
    // is not between the healthy range.
    public static HashMap<String,Object> evaluateWithSetValuesOrDefault(String id, HttpServletRequest request)
    {
        //Retrieving the ranges for the given smarthub.
        Range range = Range.collection().where("smarthub_id",id).getFirst();
        // If no range was found, the user has not personalised
        //their ranges. In this case, the default ranges will be
        //used.
        if(range == null){
            range = Range.getDefault();
        }
        //Retrieving the current values for each field
        float temp = Float.parseFloat(request.getParameter("temperature"));
        float light = Float.parseFloat(request.getParameter("light"));
        float humid = Float.parseFloat(request.getParameter("humidity"));
        float moisture = Float.parseFloat(request.getParameter("moisture"));
        //Creating the hashmap that will contain the error messages
        //for each value out of range.
        HashMap<String,Object> errors = new HashMap<>();
        //If statement that ensures the temperature value is in
        //range. If not, an error message will be saved within
        //the error hashmap.
        if(!range.isWithInRange(range.getTemperature(),temp)){
            errors.put("temperature",
                    "<p>Temperature is out of range "+
                    "<strong>"+temp+"c</strong><br>Its target range is "
                    +range.getTemperature()+" Celsius</p>");
        }
        //If statement that ensures the light value is in
        //range. If not, an error message will be saved within
        //the error hashmap.
        if(!range.isWithInRange(range.getLight(),light)){
            errors.put("light",
                    "<p>Light is out of range " +
                            "<strong>"+light+"lm</strong>" +
                            "<br>Its target range is "
                            +range.getLight()+" Luminus</p>");
        }
        //If statement that ensures the humidity value is in
        //range. If not, an error message will be saved within
        //the error hashmap.
        if(!range.isWithInRange(range.getHumidity(),humid)){
            errors.put("humidity",
                    "<p>Humidity is out of range " +
                            "<strong>"+humid+"%</strong>" +
                            "<br>Its target range is "
                            +range.getHumidity()+" Percent</p>");
        }
        //If statement that ensures the moisture value is in
        //range. If not, an error message will be saved within
        //the error hashmap.
        if(!range.isWithInRange(range.getMoisture(),moisture)){
            errors.put("moisture",
                    "<p>Moisture is out of range " +
                            "<strong>"+moisture+"%</strong>" +
                            "<br>Its target range is "
                            +range.getMoisture()+" Percent</p>");
        }
        //All the errors and messages are now returned.
        //If errors is empty, there are no values out of range.
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
    //This method is used to check if the values of 
    //A field are within its range. The parameters 
    //required are range and the current value.
    private boolean isWithInRange(String range, float value){
        //Splitting the range to obtain the min 
        //and max value.
        String[] rangeValues = range.split(",");
        float min = Float.parseFloat(rangeValues[0]);
        float max = Float.parseFloat(rangeValues[1]);
        //If the value is out of range, this method
        //will return a false result. A true result 
        //instead will be returned if the value is
        //within its range
        return value >= min && value <= max;
    }



}
