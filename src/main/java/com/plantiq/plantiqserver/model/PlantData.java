package com.plantiq.plantiqserver.model;

import com.plantiq.plantiqserver.core.Model;
import com.plantiq.plantiqserver.core.ModelCollection;

import java.util.HashMap;

public class PlantData extends Model {

    public PlantData(HashMap<String, Object> data) {
        super(data);
    }

    public static ModelCollection<PlantData> collection() {
        return new ModelCollection<>(PlantData.class);
    }

    public int getId(){
        return Integer.parseInt((String)this.data.get("id"));
    }

    public String getSmartHomeHubId(){
        return (String) this.data.get("smarthomehub_Id");
    }

    public String getSensorId(){
        return (String) this.data.get("sensor_Id");
    }

    public int getTimestamp(){
        return Integer.parseInt((String)this.data.get("timestamp"));
    }

    public float getTemperature(){
        return Float.parseFloat((String)this.data.get("temperature"));
    }

    public float getHumidity(){
        return Float.parseFloat((String)this.data.get("humidity"));
    }

    public float getLight(){
        return Float.parseFloat((String)this.data.get("light"));
    }

    public float getMoisture(){
        return Float.parseFloat((String)this.data.get("moisture"));
    }

}
