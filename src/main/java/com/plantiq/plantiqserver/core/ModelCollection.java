//**** PACKAGE ****\\
//Declares the specific package this class is part of.
package com.plantiq.plantiqserver.core;

//**** PACKAGE IMPORTS ****\\
//Imports the required classes from other packages.
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

//-----------------------------------------------------------------//
//                     ModelCollection Class                       //
//-----------------------------------------------------------------//

//Our ModelCollection is the backbone of our ORM and database link,
//the Model Collection object will be used to build up a query that
//can then be executed by mysql in order to retrieve data.

//The model collection is used by calling the static Model.Collection
//method, this will create a new model collection and return it each
//time the user adds a new query component to the chain.

//Example:

//Session.Collection.Where("user_id","123").OrderBy("dateRegistered","sort:desc").get();

public class ModelCollection<T> {

    //Limit variables limits how many results will be returned.
    private int limit;

    //Where "key" > "value" will be used to refine our search
    //results
    private HashMap<String,String> whereGreaterThan;

    //Where "key" >= "value" will be used to refine our search
    //results
    private HashMap<String,String> whereGreaterOrEqualThan;

    //Where "key" < "value" will be used to refine our search
    //results
    private HashMap<String,String> whereLessThan;

    //Where "key" <= "value" will be used to refine our search
    //results
    private HashMap<String,String> whereLessOrEqualThan;

    //This hashmap is used to collect the table name and the
    // two attributes that must match in order to perform a right join
    private HashMap<String,HashMap<String,String>> rightJoin;

    //Offset variable will offset the request by the number,
    //this is used for pagination.
    private int offset;

    //Where "key" = "value" will be used to refine our search
    //results
    private  HashMap<String,String> where;

    //OrderBy will tell SQL how we want the results order by, it
    //will accept a column and an order key.
    private LinkedHashMap<String,Sort> orderBy;

    //Query, this is our final string query that has been
    //build by this object.
    private String query;

    //Class Type, this variable will store the type of model that
    //the model collection will need to return.
    private final Class<T> type;

    //-----------------------------------------------------------------//
    //                          Constructor                            //
    //-----------------------------------------------------------------//

    //The constructor will accept a class type and bind it to the T value
    //its important this is set as it will ensure that we return the expected
    //model child type. Lastly the constructor will set our starting values
    //and perform and initialization.
    public ModelCollection(Class<T> type){
        this.rightJoin = new HashMap<>();
        this.where=new HashMap<>();
        this.whereGreaterThan=new HashMap<>();
        this.whereGreaterOrEqualThan=new HashMap<>();
        this.whereLessThan=new HashMap<>();
        this.whereLessOrEqualThan=new HashMap<>();
        this.orderBy = new LinkedHashMap<>();
        this.limit=-1;
        this.offset=-1;
        this.type = type;
    }

    //-----------------------------------------------------------------//
    //                      Right Join Method                          //
    //-----------------------------------------------------------------//

    //This method is used to perform a right join between two tables
    //Its accepts the table name it will joining as a String, and each table name
    //will have a hashmap containing the two attributes that must match to
    //perform the right join.
    // The final picture will see injected in query 'RIGHT JOIN tablename ON key = value'
    public ModelCollection<T> rightJoin(String table,String key,String value){
        HashMap<String,String> on =new HashMap<>();
        on.put(key, value);
        this.rightJoin.put(table,on);
        return this;
    }




    //-----------------------------------------------------------------//
    //                         Where Method                            //
    //-----------------------------------------------------------------//

    //This method accepts a key and a value and will add them to the where
    //hashmap, these will then be translated later into the were filter
    //in our sql query.
    public ModelCollection<T> where(String key,String value){
        this.where.put(key,value);
        return this;
    }

    //-----------------------------------------------------------------//
    //                   WhereGreaterThan Method                       //
    //-----------------------------------------------------------------//

    //This method accepts a key and a value and will add them to the whereGreaterThan
    // which will than translate into the sql expression "WHERE KEY>VALUE"
    public ModelCollection<T> whereGreaterThan(String key, String value){
        this.whereGreaterThan.put(key, value);
        return this;
    }

    //-----------------------------------------------------------------//
    //               WhereGreaterOrEqualThan Method                   //
    //-----------------------------------------------------------------//

    //This method accepts a key and a value and will add them to the whereGreaterAndEqualThan
    // which will than translate into the sql expression "WHERE KEY>=VALUE"
    public ModelCollection<T> whereGreaterOrEqualThan(String key, String value){
        this.whereGreaterOrEqualThan.put(key, value);
        return this;
    }

    //-----------------------------------------------------------------//
    //                     WhereLessThan Method                        //
    //-----------------------------------------------------------------//

    //This method accepts a key and a value and will add them to the whereLessThan
    // which will than translate into the sql expression "WHERE KEY<VALUE"
    public ModelCollection<T> whereLessThan(String key, String value){
        this.whereLessThan.put(key, value);
        return this;
    }

    //-----------------------------------------------------------------//
    //                 WhereLessAndEqualThan Method                    //
    //-----------------------------------------------------------------//

    //This method accepts a key and a value and will add them to the WhereLessAndEqualThan
    // which will than translate into the sql expression "WHERE KEY<=VALUE"
    public ModelCollection<T> whereLessAndEqualThan(String key, String value){
        this.whereLessOrEqualThan.put(key, value);
        return this;
    }

    //-----------------------------------------------------------------//
    //                        OrderBy Method                           //
    //-----------------------------------------------------------------//

    //Our OrderBy method will accept a column and a sort type, this will
    //then be used to order the results.
    public ModelCollection<T>  orderBy(String column,Sort sort){
        this.orderBy.put(column,sort);
        return this;
    }

    //-----------------------------------------------------------------//
    //                         Limit Method                            //
    //-----------------------------------------------------------------//

    //The limit method will limit our returned results to this at most
    //this number.
    public ModelCollection<T>  limit(int value){
        this.limit=value;
        return this;
    }

    //-----------------------------------------------------------------//
    //                         Offset Method                           //
    //-----------------------------------------------------------------//

    //Our offset method will offset the results by the number provided,
    //this allows us to add in pagination when this is used in conjunction
    //with the limit variable.
    public ModelCollection<T>  offset(int value){
        this.offset=value;
        return this;
    }

    //-----------------------------------------------------------------//
    //                        Get First Method                         //
    //-----------------------------------------------------------------//

    //The get first method will only return a single object to the caller
    //instead of the standard arraylist returned via the get call.
    public T getFirst(){
        ArrayList<T> outcome = this.get();
        if(outcome.size() > 0){
            return outcome.get(0);
        }else{
            return null;
        }
    }


    //-----------------------------------------------------------------//
    //                           Get Method                            //
    //-----------------------------------------------------------------//

    //The get method will build a SQL query based on the criteria set
    //using the setters above, it will then use Java's reflection to
    //build an array list of the correct type of objects before returning
    //them to the caller.
    public ArrayList<T> get(){

        //Create our output array list
        ArrayList<T> output = new ArrayList<>();

        //Get our target table from the current type name
        String table = this.type.getSimpleName();

        //Declare the first part of our query.
        this.query = "SELECT * FROM [dbo].["+table+"]";

        //Looping through the right join, inserting each junction with tables.
        //The output will be 'RIGHT JOIN tablename ON attribute = attribute'
        this.rightJoin.forEach((nextTable,on)->{
            this.query += " RIGHT JOIN "+nextTable+" ON ";
            //The hashmap on will contain only one key and one value
            on.forEach((key,value)->{
                this.query += key + " = " + value;
            });
        });


        //Create an atomic integer to act as a counter for us
        AtomicInteger counter = new AtomicInteger(0);
        //For each of the where criteria present add them to
        //the query.
        this.where.forEach((key,value)->{

            if(counter.getAndIncrement() == 0){
                this.query += " WHERE "+key+"='"+value+"'";
            }else{
                this.query += " AND "+key+"='"+value+"'";
            }

        });

        //If there was a where condition previously, instead of
        // re-inserting the key word WHERE, the key word AND
        // will be added to the query, followed by the key>value
        this.whereGreaterThan.forEach((key,value)->{
            if(counter.get() == 0){
                this.query += " WHERE "+key+" > "+value;
            }else{
                this.query += " AND "+key+" > "+value;
            }
            counter.getAndIncrement();
        });
        //If there was a where condition previously, instead of
        // re-inserting the key word WHERE, the key word AND
        // will be added to the query, followed by the key>=value
        this.whereGreaterOrEqualThan.forEach((key,value)->{
            if(counter.get() == 0){
                this.query += " WHERE "+key+" >= "+value;
            }else{
                this.query += " AND "+key+" >= "+value;
            }
            counter.getAndIncrement();
        });
        //If there was a where condition previously, instead of
        // re-inserting the key word WHERE, the key word AND
        // will be added to the query, followed by the key<value
        this.whereLessThan.forEach((key,value)->{
            if(counter.get() == 0){
                this.query += " WHERE "+key+" < "+value;
            }else{
                this.query += " AND "+key+" < "+value;
            }
            counter.getAndIncrement();
        });
        //If there was a where condition previously, instead of
        // re-inserting the key word WHERE, the key word AND
        // will be added to the query, followed by the key<=value
        this.whereLessOrEqualThan.forEach((key,value)->{
            if(counter.get() == 0){
                this.query += " WHERE "+key+" <= "+value;
            }else{
                this.query += " AND "+key+" <= "+value;
            }
            counter.getAndIncrement();
        });

        //Counter is set to 0 in order to insert multiple columns
        //for the sorting. Each time a column is inserted,
        //it will be followed by the sort type (ASC or Desc)
//        counter.set(0);
//        this.orderBy.forEach((key,value)->{
//            if(counter.getAndIncrement() == 0){
//                this.query += " ORDER BY "+key+" "+value+", ";
//            }else{
//               this.query+=key+" "+value+", ";
//            }
//
//        });
//        //Since the code to concatenate multiple attributes,
//        //it add ', ' at the end of each sorting type. Once
//        // all the attributes have been added, these last two
//        // characters will need to be deleted
//        this.query=this.query.substring(0,this.query.length()-2);


        //If we have a limit set add it!
        if (this.limit!=-1){
            this.query+=" LIMIT "+this.limit;
        }

        //If we have an offset set add it!
        if (this.offset!=-1){
            this.query+=" OFFSET "+this.offset;
        }



        //Create our cArguments.
        Class[] cArg = new Class[1];

        //Create a temp hashmap, this must match the response
        //that the database will give for all entires and is
        //used to ensure we are passing the correct constructor
        //parameter by Java's reflection.
        HashMap<String,String> test = new HashMap<>();

        //Set our first parameter to the class of our test response.
        cArg[0] = test.getClass();

        //For each of the entires returned from the database run
        //this loop!
        Database.query(this.query).forEach((n -> {

            try {
                //Create our object and add it to the output
                output.add(this.type.getDeclaredConstructor(cArg).newInstance(n));

            }catch(InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                //Catch and print any errors!
                e.printStackTrace();
            }

        }));

        //lastly return our output to the caller.
        return output;
    }

}
