package com.plantiq.plantiqserver.core;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;


public class Model {

    protected HashMap<String,Object> data;

    public Model(HashMap<String, Object> data){
        this.data = data;
    }

    //------------------ INSERT INTO METHOD -----------------

    public static boolean insert(String table,HashMap<String,Object> data){
    //Create our query string and initialize it to the starting value
        StringBuilder query = new StringBuilder("INSERT INTO [dbo].["+table+"] (");

        //For each loop to add all the column names of the table into the query, with the correct SQL syntax
        data.forEach((key,value)->{
            query.append(key+",");
        });

        //From the previous loop, a comma has been added to the last column.
        //This needs to be removed for the query to run
        query.deleteCharAt(query.length()-1);
        query.append(") VALUES (");

        //For each loop to add all the values of the table into the query, with the correct SQL syntax
        data.forEach((key,value)->{
            query.append("'"+value+"',");
        });
        query.deleteCharAt(query.length()-1);
        query.append(");");

        Database.query(query.toString());

        return Database.getAndResetRowsAffected() != 0;
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
        StringBuilder query = new StringBuilder("UPDATE [dbo].["+this.data.get("_table")+"] SET");

        AtomicBoolean validUpdate = new AtomicBoolean(false);

        data.forEach((key,value)->{

            //Check if the data in our data hashmap matches a key present
            //in this object, if so then append the key value to the query.
            if(this.data.containsKey(key)){

                query.append(" "+key+"='"+value+"',");
                validUpdate.set(true);
            }

        });

        if(validUpdate.get()){
            //Next append our where query to update this record only
            query.deleteCharAt(query.length()-1);
            query.append(" WHERE id='").append(this.data.get("id")).append("'");

            Database.query(query.toString());
        }

        //Finally return our result as a boolean
        return Database.getAndResetRowsAffected() != 0;
    }

    public void delete(){

        String query = "DELETE FROM [dbo].["+this.data.get("_table")+"] WHERE id='"+this.data.get("id")+"'";

        Database.query(query);
    }

    public void delete(String column){

        String query = "DELETE FROM [dbo].["+this.data.get("_table")+"] WHERE "+column+"='"+this.data.get(column)+"'";

        Database.query(query);
    }

}
