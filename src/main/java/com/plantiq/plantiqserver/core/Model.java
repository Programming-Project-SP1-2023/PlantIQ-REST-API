package com.plantiq.plantiqserver.core;

import java.util.HashMap;


public class Model {

    protected HashMap<String,Object> data;

    public Model(HashMap<String, Object> data){
        this.data = data;
    }


    //Our static CRUD methods map to the crud actions of
    //create, read, update and delete, these are all assigned
    //to a static that and will wrap around any other classes

    //------------------ UPDATE METHOD -----------------

    //This method accepts a hash map of data and will validate
    //that the keys are present in the model object, if so
    //then it will build a query to update then
    public boolean update(HashMap<String,Object> data){

        //Create our query string and initialize it to the starting value
        StringBuilder query = new StringBuilder("UPDATE "+this.data.get("_table")+" SET");

        data.forEach((key,value)->{

            //Check if the data in our data hashmap matches a key present
            //in this object, if so then append the key value to the query.
            if(this.data.containsKey(key)){
                query.append(" "+key+"='"+value+"'");
            }

        });

        //Next append our where query to update this record only
        query.append(" WHERE id="+this.data.get("id"));

        //Finally return our result as a boolean
        return Database.getAndResetRowsAffected() != 0;
    }

    public void delete(){

    }

}
