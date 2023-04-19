package com.plantiq.plantiqserver.model;

import com.plantiq.plantiqserver.core.Model;
import com.plantiq.plantiqserver.core.ModelCollection;

import java.util.HashMap;

public class AwaitingRegistration extends Model {

    public AwaitingRegistration(HashMap<String, Object> data) {
        super(data);
    }

    public static ModelCollection<AwaitingRegistration> collection(){
        return new ModelCollection<>(AwaitingRegistration.class);
    }

    public String getId(){
        return (String)this.data.get("id");
    }

    public int getDate(){
        return Integer.parseInt((String)this.data.get("date"));
    }

}
