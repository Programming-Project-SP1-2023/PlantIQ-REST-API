package com.plantiq.plantiqserver.model;

import com.plantiq.plantiqserver.core.Model;
import com.plantiq.plantiqserver.core.ModelCollection;

import java.util.HashMap;

public class PlantName extends Model {

    public static ModelCollection<PlantName> collection() {
        return new ModelCollection<>(PlantName.class);
    }

    public PlantName(HashMap<String, Object> data) {
        super(data);
    }

    public String getName(){
        return this.data.get("name").toString();
    }
}
