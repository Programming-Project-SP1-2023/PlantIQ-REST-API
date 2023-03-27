package com.plantiq.plantiqserver.core;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class ModelCollection<T> {
    private int limit;
    private int offset;
    private HashMap<String,String> where;
    private ArrayList<String> orderBy;
    private String query;
    private final Class<T> type;

    public ModelCollection(Class<T> type){
        this.where=new HashMap<>();
        this.limit=0;
        this.offset=0;
        this.type = type;
    }
    public ModelCollection<T> where(String key,String value){
        this.where.put(key,value);
        return this;
    }

    public ModelCollection<T>  orderBy(String value){
        this.orderBy.add(value);
        return this;
    }

    public ModelCollection<T>  limit(int value){
        this.limit=value;
        return this;
    }
    public ModelCollection<T>  offset(int value){
        this.offset=value;
        return this;
    }

    public ArrayList<T> get(){

        ArrayList<T> output = new ArrayList<>();

        String table = this.type.getSimpleName();

        this.query = "SELECT * FROM "+table;

        AtomicInteger whereCount = new AtomicInteger(0);

        this.where.forEach((key,value)->{

            if(whereCount.getAndIncrement() == 0){
                this.query += " WHERE "+key+"='"+value+"'";
            }else{
                this.query += " AND "+key+"='"+value+"'";
            }

        });
        
        //this.orderBy.forEach();

        if (this.limit!=0){
            this.query+=" LIMIT "+this.limit;
        }

        if (this.offset!=0){
            this.query+=" OFFSET "+this.offset;
        }


        Class[] cArg = new Class[1];

        HashMap<String,String> test = new HashMap<>();
        cArg[0] = test.getClass();

        Database.query(this.query).forEach((n -> {

            try {

           output.add(this.type.getDeclaredConstructor(cArg).newInstance(n));

           }catch(InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                e.printStackTrace();
           }

        }));


        return output;
    }

}
